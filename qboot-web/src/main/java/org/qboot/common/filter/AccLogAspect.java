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
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AccLogAspect {

    private static Logger logger = LoggerFactory.getLogger(AccLogAspect.class);

    private ThreadLocal<SysOperateLogDto> tlocal = new ThreadLocal<>();
    
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

    @Autowired
    private OperLogReceiverService receiverService;
    @Autowired
    private SysOperateLogInfoService logInfoService;
	
	public AccLogAspect(){

		fixedPool =  new ThreadPoolExecutor(concurrentNum, concurrentNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity),new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) { 
				// 超出容量则输出告警日志，并放弃处理
				logger.warn("Exceeded the queue of operation log capacity...");
			}
		});

	}

    @Pointcut("@annotation(org.qboot.common.annotation.AccLog)")
    public void webRequestLog() { }

    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {

        SysOperateLogDto optLog = null;
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

            optLog = new SysOperateLogDto();
            optLog.setRequestUri(uri);
            optLog.setBeginTime(beginTime);
            optLog.setRequestDate(new Date());
            optLog.setRequestIP(remoteAddr);
            optLog.setRequestParams(paramSB.toString());

            QUser user = SecurityUtils.getUser();
            if(user != null) {
            	String loginName = user.getLoginName();
                optLog.setRequestUserId(loginName);
                tlocal.set(optLog);
            }

        } catch (Exception e) {
            if(null != optLog) {
                logger.error("acc log fail before request, optLog:{}", optLog.toString(), e);
            } else {
                logger.error("acc log fail before request  ", e);
            }
        }
    }

    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            // 处理完请求，返回内容
        	SysOperateLogDto optLog = tlocal.get();
        	if(optLog != null) {
                tlocal.remove();
        		optLog.setResponseTime((System.currentTimeMillis() - optLog.getBeginTime()));
            	Long logId = logInfoService.findLogIdByUri(optLog.getRequestUri());
            	optLog.setLogId(logId);
        		logger.debug("operateLog_doAfter Returning OperationLog queue used capacity:{}, remaining capacity:{}, optLog:{}",
                        optLog.toString(), fixedPool.getQueue().size(), fixedPool.getQueue().remainingCapacity());
        		fixedPool.execute(new LogTask(optLog));
        	}
        } catch (Exception e) {
            logger.error("acc log fail After Returning ", e);
        }
    }

    /**
     * get request user ip
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
     * isPsw
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
    	 * optlog
    	 */
	    private SysOperateLogDto optLog; 
	     
	    public LogTask(SysOperateLogDto optLog){
	        this.optLog=optLog;
	    }
	    
	    @Override
	    public void run() {
	    	try{
	            receiverService.receive(optLog);
	    	    logger.info("OperLogReceiverService  LogId: [{}] saveed", optLog.getLogId());
	    	}catch(Exception e){
	    		logger.error("OperLogReceiverService save log error:{}", e.getMessage());
	    	}
	    }
	}
}
