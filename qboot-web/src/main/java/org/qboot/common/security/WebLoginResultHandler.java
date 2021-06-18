package org.qboot.common.security;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.sys.service.SysLoginLogService;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理登录结果
 * @author iscast
 * @date 2020-09-25
 */
public class WebLoginResultHandler implements AuthenticationSuccessHandler,AuthenticationFailureHandler,LogoutSuccessHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Autowired
	private SysLoginLogService sysLoginLogService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		String loginName = this.obtainUsername(request);
		ResponeModel result = ResponeModel.error();
		if (exception instanceof UsernameNotFoundException) {
			result.setMsg("user or pwd error");
			logger.warn("user:{} or pwd error", loginName);
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof BadCredentialsException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setMsg("pwd error, fail 5 times will lock your account");
			logger.warn("user:{} pwd error", loginName);
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof LockedException) {
			result.setMsg("user is lock");
			logger.warn("user is lock loginName:{}", loginName);
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_LOCK_24, loginName, request);
		} else if (exception instanceof DisabledException) {
			result.setMsg(exception.getMessage());
			logger.warn("user:{} login fail {}" , loginName, exception.getMessage());
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_STAUTS_STOP, loginName, request);
		} else {
            result.setMsg(exception.getMessage());
            logger.warn("userName:[{}] login fail msg:[{}]", loginName, exception.getMessage());
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_SYS_ERR, loginName, request);
        }
		this.print(response, JSON.toJSONString(result));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		ResponeModel ok = ResponeModel.ok();
		String loginName = this.obtainUsername(request);
        sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_SUCCESS, loginName, request);
		logger.info("user:[{}] login success ！", loginName);
		loginSecurityService.setUserSessionId(loginName, request.getSession().getId());
		loginSecurityService.clearLoginFailTimes(loginName);
		this.print(response, JSON.toJSONString(ok));
	}

	private void print(HttpServletResponse response,String content) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(content);
		writer.close();
	}
	
	private String obtainUsername(HttpServletRequest request) {
        String loginName = request.getParameter("username");
        if(StringUtils.isNotBlank(loginName)) {
            loginName = loginName.trim();
        }
        return RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), loginName);
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
		List list = new ArrayList<>();
		list.add(new CustomPermission("anonymous"));
		AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("anonymous", "anonymous", list);
		SecurityContextHolder.getContext().setAuthentication(anonymous);

		String userName = request.getParameter("username");
		if(StringUtils.isNotBlank(userName)) {
			loginSecurityService.clearUserSessions(userName);
		}
		this.print(response, JSON.toJSONString(ResponeModel.ok("logout success")));
	}

}
