package org.qboot.common.filter;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.qboot.common.error.ErrorCode;
import org.qboot.common.error.IError;
import org.qboot.common.exception.ErrorCodeException;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.IpUtils;
import org.qboot.sys.bo.AccLogBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

import static org.qboot.common.constants.SysConstants.GLOBAL_DEFAULT_ERROR;

/**
 * 通过切面记录操作日志
 * @author iscast
 * @date 2020-12-24
 */
@Aspect
@Component
public class AccLogAspect {
    private static Logger logger = LoggerFactory.getLogger(AccLogAspect.class);
    private static Logger ACC = LoggerFactory.getLogger("acc");
    private ThreadLocal<AccLogBO> tlocal = new ThreadLocal<>();

    @Pointcut("@annotation(org.qboot.common.annotation.AccLog)")
    public void webRequestLog() { }

    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {
        Date current = new Date();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Map<String, String[]> paramMap = request.getParameterMap();
            AccLogBO accLog = new AccLogBO();
            accLog.setRequestIP(IpUtils.getIpAddr(request));
            accLog.setUri(request.getRequestURI());
            accLog.setRequestParams(JSONObject.toJSONString(paramMap, true));
            accLog.setRequestTime(current);
            accLog.setUserId(SecurityUtils.getUserId());
            tlocal.set(accLog);
        } catch (Exception e) {
            logger.error("acc log fail before request errMsg:{}", e.getMessage());
        }
    }

    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            AccLogBO accLog = tlocal.get();
        	if(accLog != null) {
                tlocal.remove();
                if(null != result) {
                    accLog.setResponseParams(JSONObject.toJSONString(result, true));
                }
                accLog.setCostTime(System.currentTimeMillis() - accLog.getRequestTime().getTime());
                ACC.trace(accLog.toString());
        	}
        } catch (Exception e) {
            logger.error("acc log fail after request errMsg:{}", e.getMessage());
        }
    }

    @AfterThrowing(value = "webRequestLog()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        logger.debug("acc log occur exception:{}", e.getMessage());
        try {
            AccLogBO accLog = tlocal.get();
            if(accLog != null) {
                tlocal.remove();
                if(e instanceof ErrorCodeException) {
                    ErrorCodeException errCode = (ErrorCodeException) e;
                    IError error = errCode.getError();
                    accLog.setResponseParams(JSONObject.toJSONString(error, true));
                } else {
                    accLog.setResponseParams(JSONObject.toJSONString(new ErrorCode(GLOBAL_DEFAULT_ERROR, e.getMessage()), true));
                }
                accLog.setCostTime(System.currentTimeMillis() - accLog.getRequestTime().getTime());
                ACC.trace(accLog.toString());
            }
        } catch (Exception ex) {
            logger.error("acc log fail after request errMsg:{}", ex.getMessage());
        }
    }

}
