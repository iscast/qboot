package org.qboot.common.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.qboot.sys.service.impl.SysLoginLogService;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.utils.IpUtils;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.entity.ResponeModel;
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
import java.util.List;

/**
 * <p>Title: LoginResultHandler</p>
 * <p>Description: 登录结果处理器</p>
 * @author history
 * @date 2018-09-11
 */
public class WebLoginResultHandler implements AuthenticationSuccessHandler,AuthenticationFailureHandler,LogoutSuccessHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Autowired
	private SysLoginLogService sysLoginLogService;
	@Override

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String loginName = this.obtainUsername(request);
		logger.warn("userName:[{}] login fail msg:[{}]", loginName, exception.getMessage());
		ResponeModel result = ResponeModel.error();
		if (exception instanceof UsernameNotFoundException) {
			result.setMsg("user or pwd error");
			logger.warn("user:{} or pwd error", loginName);
			loginLog(SysConstants.SYS_USER_LOGIN_STATUS_PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof BadCredentialsException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setMsg("pwd error, fail 5 times will lock your account");
			logger.warn("user:{} pwd error", loginName);
			loginLog(SysConstants.SYS_USER_LOGIN_STATUS_PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof LockedException) {
			result.setMsg("user is lock");
			logger.warn("user is lock loginName:{}", loginName);
			loginLog(SysConstants.SYS_USER_LOGIN_STATUS_LOCK_24, loginName, request);
		} else if (exception instanceof DisabledException) {
			result.setMsg(exception.getMessage());
			logger.warn("user:{} login fail {}" , loginName, exception.getMessage());
			loginLog(SysConstants.SYS_USER_LOGIN_STATUS_STAUTS_STOP, loginName, request);
		} 
		this.print(response, JSON.toJSONString(result));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		ResponeModel ok = ResponeModel.ok();
		String loginName = this.obtainUsername(request);
		loginLog(SysConstants.SYS_USER_LOGIN_STATUS_SUCCESS, loginName, request);
		logger.info("user:[{}] login sucess ！", loginName);
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
        String loginName = request.getParameter("username");
        if(StringUtils.isNotBlank(loginName)) {
            loginName = loginName.trim();
        }
        return RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), loginName);
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		List list = new ArrayList<>();
		list.add(new CustomPermission("anonymous"));
		AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("anonymous", "anonymous", list);
		SecurityContextHolder.getContext().setAuthentication(anonymous);

		String userName = request.getParameter("username");
		if(StringUtils.isNotBlank(userName)) {
			loginSecurityService.clearUserSessions(userName);
		}
		this.print(response, JSON.toJSONString(ResponeModel.ok("logout sucess")));
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
            new LogThread(status, loginName, ip, userAgentStr, browserStr, deviceName, area).run();
		} catch (Exception e) {
			logger.error("登录日记记录异常，{}....", ExceptionUtils.getStackTrace(e));
		}
	}

	private class LogThread extends Thread {


        private String status;
        private String loginName;
        private String ip;
        private String userAgentStr;
        private String browserStr;
        private String deviceName;
        private String area;

        public LogThread(String status, String loginName, String ip, String userAgentStr, String browserStr, String deviceName, String area) {
            super();
            this.status = status;
            this.loginName = loginName;
            this.ip = ip;
            this.userAgentStr = userAgentStr;
            this.browserStr = browserStr;
            this.deviceName = deviceName;
            this.area = area;
        }

        @Override
        public void run() {
            sysLoginLogService.loginLogByLoginName(status, loginName, ip, userAgentStr, browserStr, deviceName, area, SysConstants.SYS_USER_PWD_STATUS_NORMAL);
        }
    }

}
