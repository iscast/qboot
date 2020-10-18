package org.qboot.common.controller;

import org.apache.commons.text.StringEscapeUtils;
import org.qboot.common.exception.QExceptionCode;
import org.qboot.common.exception.ResponeException;
import org.qboot.common.utils.DateUtils;
import org.qboot.web.dto.ResponeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * <p>Title: QControllerAdvice</p>
 * <p>Description: 自定义逻辑处理、异常捕获处理</p>
 * 
 * @author history
 * @date 2018-09-08
 */

@Component
@ControllerAdvice
public class QControllerAdvice {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(QControllerAdvice.class);
	
	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击   2. 将字段中Date类型转换为String类型
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
	@ExceptionHandler(ResponeException.class)
	public ResponeModel responeException(ResponeException e) {
		logger.error("respone exception ", e);
		ResponeModel responeModel = ResponeModel.error(e.getResponeCode(), e.getResponeMsg());
		return responeModel;
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	public ResponeModel bindException(BindException e) {
		logger.error("bind exception ", e);
		ResponeModel responeModel = ResponeModel.error(QExceptionCode.PARAM_BIND_ERROR, "参数绑定错误:{0}",e.getMessage());
		return responeModel;
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponeModel exception(Exception e) {
		logger.error("global exception ", e);
		ResponeModel responeModel = ResponeModel.error(QExceptionCode.UNKNOWN, e.getMessage());
		return responeModel;
	}
}
