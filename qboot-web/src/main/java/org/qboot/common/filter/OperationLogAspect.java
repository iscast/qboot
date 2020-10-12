package org.qboot.common.filter;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.qboot.base.dto.SysOperateLogDto;
import org.qboot.base.service.impl.OperLogReceiverService;
import org.qboot.base.service.impl.SysOperateLogInfoService;
import org.qboot.common.utils.SpringContextHolder;
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 操作日志监控
 * @author history
 *
 */
@Aspect
@Component
public class OperationLogAspect {

    private static Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    private ThreadLocal<SysOperateLogDto> tlocal = new ThreadLocal<SysOperateLogDto>();
    
	/**
	 * 日志上报线程并发数
	 */
	private static int concurrentNum = 5;
	/**
	 * 日志上报队列容量
	 */
	private static int queueCapacity = 1000;
	/**
	 * 日志上报线程池
	 */
	private static ThreadPoolExecutor fixedPool =  null;
	
	public OperationLogAspect(){

		fixedPool =  new ThreadPoolExecutor(concurrentNum, concurrentNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity),new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) { 
				// 超出容量则输出告警日志，并放弃处理
				logger.warn("Exceeded the queue of operation log capacity...");
			}
		});
		
	}

    @Pointcut("execution(public * org.qboot..controller.*.*(..))  || execution(* com.yoopay..controller.*.*(..))")
    public void webRequestLog() { }

    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {

            long beginTime = System.currentTimeMillis();

            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);

            Map<String, String[]> paramMap = request.getParameterMap();
        	StringBuffer paramSB = new StringBuffer("params:{");
            for(Entry<String, String[]> entry : paramMap.entrySet()) {
            	paramSB.append(entry.getKey()).append(":");
            	StringBuffer sb = new StringBuffer();
            	for(String val : entry.getValue()) {
                	if(StringUtils.isNotBlank(val) && isPassword(entry.getKey())) {
                		sb.append("******");
                	} else {
                		sb.append(val);
                	}
            	}
            	paramSB.append(sb.toString()).append(",");
            }
            paramSB.append("}");
            
            QUser user = SecurityUtils.getUser();
            if(user != null) {
            	String loginName = user.getLoginName();
                
                SysOperateLogDto optLog = new SysOperateLogDto();
                optLog.setRequestUri(uri);
                optLog.setBeginTime(beginTime);
                optLog.setRequestDate(new Date());
                optLog.setRequestIP(remoteAddr);
                optLog.setRequestUserId(loginName);
                optLog.setRequestParams(paramSB.toString());
                
                tlocal.set(optLog);
                logger.debug("operateLog_doBefore={}", optLog.toString());
            }else {
                logger.debug("operateLog_doBefore, getLoginUser is null.");
            }
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doBefore()***", e);
        }
    }

    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            // 处理完请求，返回内容
        	SysOperateLogDto optLog = tlocal.get();
        	if(optLog != null) {
        		optLog.setResponseTime((System.currentTimeMillis() - optLog.getBeginTime()));
            	
            	SysOperateLogInfoService logInfoService = SpringContextHolder.getBean("sysOperateLogInfoService", SysOperateLogInfoService.class);
                
            	Long logId = logInfoService.findLogIdByUri(optLog.getRequestUri());
            	optLog.setLogId(logId);
            	
        		// 放入线程池
        		logger.debug("doAfterReturning OperationLog queue used capacity:{}, remaining capacity:{}", fixedPool.getQueue().size()
        				, fixedPool.getQueue().remainingCapacity());
        		fixedPool.execute(new LogTask(optLog));
        	}
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doAfterReturning()***", e);
        }
    }


    /**
     * 获取登录用户远程主机ip地址
     * 
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 请求参数拼装
     * @param keyName
     * @return
     */
    private boolean isPassword(String keyName) {
    	keyName = keyName.toLowerCase();
        if (StringUtils.isNotBlank(keyName) && (keyName.indexOf("psw") > 0 || keyName.indexOf("pwd") > 0 || keyName.indexOf("password") > 0)) {
            return true;
        }
        return false;
    }

    class LogTask implements Runnable{
		
    	/**
    	 * 操作日志
    	 */
	    private SysOperateLogDto optLog; 
	     
	    public LogTask(SysOperateLogDto optLog){
	        this.optLog=optLog;
	    }
	    
	    @Override
	    public void run() {
	    	logger.info("Send operatioin Log: {}", optLog);
	    	try{
	            OperLogReceiverService receiverService = SpringContextHolder.getBean("operLogReceiverService", OperLogReceiverService.class);
	            logger.debug("OperationLogReceiverService.receive(operateLog)={}", optLog.toString());
	            receiverService.receive(optLog);
	    	}catch(Exception e){
	    		logger.error("log upload error:{}", e.getMessage());
	    	}
	    }
	}
}
