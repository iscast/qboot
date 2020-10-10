package org.qboot.web.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.base.dto.SysLoginLog;
import org.qboot.base.service.impl.LoginSecurityService;
import org.qboot.base.service.impl.SysLoginLogService;
import org.qboot.common.utils.IpUtils;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.web.dto.ResponeModel;
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
import java.util.HashMap;

/**
 * <p>Title: LoginResultHandler</p>
 * <p>Description: 登录结果处理器</p>
 *
 * @author history
 * @date 2018-09-11
 */
public class LoginResultHandler implements AuthenticationSuccessHandler,AuthenticationFailureHandler,LogoutSuccessHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Autowired
	private SysLoginLogService sysLoginLogService;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String loginName = this.obtainUsername(request);
		ResponeModel result = ResponeModel.error(exception.getMessage());
		if (exception instanceof UsernameNotFoundException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setMsg("账户不存在");
			logger.error("账户不存在");
			loginLog(SysLoginLog.PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof BadCredentialsException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setMsg("账户密码错误,连续错误5次将锁定24小时");
			logger.error("账户密码错误");
			loginLog(SysLoginLog.PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof LockedException) {
			result.setMsg("账户已被锁定");
			logger.error("账户已被锁定");
			loginLog(SysLoginLog.LOCK_24, loginName, request);
		} else if (exception instanceof DisabledException) {
			result.setMsg(exception.getMessage());
			logger.error(exception.getMessage());
			loginLog(SysLoginLog.USER_STAUTS_STOP, loginName, request);
		} 
		this.print(response, JSON.toJSONString(result));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		ResponeModel ok = ResponeModel.ok();
		String loginName = this.obtainUsername(request);
		loginName = RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), loginName.trim());
		loginLog(SysLoginLog.SUCCESS, loginName, request);
		logger.info("{}, 登录成功！", loginName);
		String sessionId = request.getSession().getId();
		loginSecurityService.setUserSessionId(loginName, sessionId);
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
		return request.getParameter("username");
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("anonymous", "anonymous", new ArrayList());
        SecurityContextHolder.getContext().setAuthentication(anonymous);
		ResponeModel ok = ResponeModel.ok();
		String userName = this.obtainUsername(request);
		ok.setData(userName);
		this.print(response, JSON.toJSONString(ok));
	}

	/**
	 * 登录日志
	 * @param status
	 * @param loginName
	 * @param request
	 */
	private void loginLog(String status,String loginName,HttpServletRequest request) {
		String ip = IpUtils.getIpAddr(request);
		String userAgentStr = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);  
		Browser browser = userAgent.getBrowser();
		OperatingSystem os = userAgent.getOperatingSystem();
		String deviceName = os.getName() + " "+ os.getDeviceType();
		String area = "";
		String browserStr = browser.getName() +" "+ browser.getVersion(userAgentStr);
		try {
			if (StringUtils.isNotEmpty(ip)) {
				HashMap<String, String> params = new HashMap<>();
				params.put("ip",ip);
				String ipInfo = ""; 
				if (StringUtils.isNotEmpty(ipInfo)) {
					JSONObject parseObject = JSON.parseObject(ipInfo);
					if (parseObject.containsKey("data")) {
						JSONObject data = parseObject.getJSONObject("data");
						area = data.getString("region") + data.getString("city");
					}
				}
			}
		} catch (Exception e) {
			logger.error("登录日记记录异常，{}....", ExceptionUtils.getStackTrace(e));
		}
		sysLoginLogService.loginLogByLoginName(SysLoginLog.SUCCESS, loginName, ip, userAgentStr, browserStr, deviceName, area, 0);
	}
}
