package org.qboot.common.security;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * <p>Title: CustomSessionInformationExpiredStrategy</p>
 * <p>Description: 重复登录</p>
 * @author history
 * @date 2018-09-21
 */
public class WebSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		event.getResponse().setStatus(SecurityStatus.NO_PERMISSION.getValue());
	}

}
