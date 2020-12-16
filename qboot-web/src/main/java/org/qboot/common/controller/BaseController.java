package org.qboot.common.controller;

import org.qboot.common.entity.ResponeModel;
import org.qboot.common.exception.errorcode.SystemErrTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * <p>Title: BaseController</p>
 * <p>Description: 基础 Controller</p>
 * 
 * @author history
 * @date 2018-09-08
 */
public class BaseController {
	/*** 日志对象 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected MessageSource messageSource;

	/**
	 * 获取国际化配置
	 * @param key
	 * @return
	 */
	protected String getMsgSource(String key){
		String msg = key;
		try {
			Locale locale = LocaleContextHolder.getLocale();
			msg = messageSource.getMessage(key, null,locale);
			if(StringUtils.isEmpty(msg)){
				msg = key;
				logger.warn("获取国际化资源失败,请检查国际化文件,key={}!",key);
			}
		} catch (NoSuchMessageException e) {
			logger.error("获取国际化资源异常.",e);
		}
		return msg ;
	}

	protected ResponeModel ok(){ return ResponeModel.ok(SystemErrTable.SUCCESS); }

	protected ResponeModel err(){ return ResponeModel.error(SystemErrTable.ERR); }
}