package org.qboot.web.security.handle.impl;

import org.qboot.common.strategy.enums.OptionServiceEnum;
import org.qboot.web.security.QUserDetails;
import org.qboot.web.security.handle.ILoginInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Title: DefaultLoginInServiceImpl.java</p>
 * <p>Description: 默认的登录后置处理实现</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: qboot</p>
 * @author history
 * @date 2019年4月22日
 */
@Service
public class DefaultLoginInServiceImpl implements ILoginInService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getBussNumber() {
		// TODO Auto-generated method stub
		return OptionServiceEnum.DEFAULT.getCode();
	}
	
	@Override
	public void loginInAfter(QUserDetails adminUserDetails) {
		// TODO Auto-generated method stub
		logger.info("default invoke loginInAfter");
	}
}
