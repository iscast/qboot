package org.qboot.common.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.exception.ErrorCodeException;
import org.qboot.common.exception.errorcode.SystemErrTable;
import org.qboot.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.Date;

import static org.qboot.common.exception.errorcode.SystemErrTable.*;

/**
 * 异常捕获处理QControllerAdvice
 * @author iscast
 * @date 2020-09-25
 */
@Component
@ControllerAdvice
public class WebControllerAdvice {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);
	
	/**
	 * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	@ResponseBody
	@ExceptionHandler(ErrorCodeException.class)
	public ResponeModel responeException(ErrorCodeException e, HttpServletRequest request) {
        logRequestBusiness(request, e);
		return ResponeModel.error(e);
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	public ResponeModel bindException(BindException e, HttpServletRequest request) {
        logRequest(request, e);
		return ResponeModel.error(SYS_PARAM_BIND_ERROR);
	}

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponeModel IllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        logRequest(request, e);
        return ResponeModel.error(SYS_PARAM_ILLEGAL);
    }

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public ResponeModel NullPointerException(NullPointerException e, HttpServletRequest request) {
        logRequest(request, e);
        return ResponeModel.error(SYS_NULL_POINT_ERROR);
    }
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponeModel exception(Exception e, HttpServletRequest request) {
        logRequest(request, e);
		return ResponeModel.error(SystemErrTable.ERR);
	}

	private void logRequest(HttpServletRequest request, Exception e) {
	    if(null == request) {
	        logger.error("error msg: ", e);
	        return;
        }

        String remoteIp = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        String eMsg = e.getMessage();
        logger.error("error msg: client ip:[{}] requestURI:[{}], errorMsg:[{}], stackTrace:{}", remoteIp, requestURI, eMsg, ExceptionUtils.getStackTrace(e));
    }

    /**
     * 业务逻辑异常
     * @author: iscast
     * @date: 2020/12/11 10:36
     */
    private void logRequestBusiness(HttpServletRequest request, ErrorCodeException e) {
        String[] stackFrames = ExceptionUtils.getStackFrames(e);
        int errorCode = e.getErrorCode();
        String errorInfo = e.getErrorInfo();
        String stack0 = stackFrames[0];
        String stack1 = stackFrames[1];
        String stack2 = stackFrames[2];
        if(null == request) {
            logger.error("businessError code:[{}] errMsg:[{}] \n {} \n {} \n {}", errorCode, errorInfo, stack0, stack1, stack2);
            return;
        }

        String remoteIp = request.getRemoteAddr();
        String method = request.getMethod();
        logger.error("businessError client ip:[{}] request method:[{}] code:[{}] errMsg:[{}] \n {} \n {} \n {}", remoteIp, method, errorCode, errorInfo, stack0, stack1, stack2);
    }

}
