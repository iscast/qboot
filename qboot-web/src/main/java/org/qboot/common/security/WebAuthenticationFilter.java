package org.qboot.common.security;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.filter.ILoginProcessFilter;
import org.qboot.common.filter.LoginProcessFailException;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.utils.SpringContextHolder;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.qboot.sys.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: LoginPasswordEncoder</p>
 * <p>Description: 自定义登录/p>
 * @author history
 * @date 2018-09-11
 */
public class WebAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private String usernameParameter = "username";
	private String passwordParameter = "password";
	private boolean postOnly = true;

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private LoginSecurityService loginSecurityService;

    private ILoginProcessFilter customFilter;
    private String customFilterName;

    public WebAuthenticationFilter(String loginPath, SpringContextHolder springContextHolder) {
        super(new AntPathRequestMatcher(loginPath, "POST"));
        // 初始化项目的自定义登陆流程处理过滤器
        customFilter = springContextHolder.getBeanByClass(ILoginProcessFilter.class);
        if(null != customFilter) {
            customFilterName = customFilter.getClass().getName();
        }
    }

	public WebAuthenticationFilter(String loginPath) {
		super(new AntPathRequestMatcher(loginPath, "POST"));
	}

	 @Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new UsernameNotFoundException("username or password can't be null");
		}
		username = RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), username.trim());
		 if(loginSecurityService.isLocked(username)) {
			 throw new LockedException("locked user");
		 }

		SysUserDto sysUser = sysUserService.findByLoginName(username);
		if (sysUser == null) {
			throw new UsernameNotFoundException(username + "account not exist");
		}

		if (SysConstants.SYS_DISABLE.equals(sysUser.getStatus())) {
			throw new LockedException("locked user");
		}

		// 处理自定义过滤器逻辑
        if(null != customFilter && !customFilter.doBusiness(request, sysUser)) {
            throw new LoginProcessFailException(String.format("customFilter:%s process login auth fail", customFilterName));
        }
		
		//解密传输的密文密码
		password = RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), password) + sysUser.getSalt();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	protected void setDetails(HttpServletRequest request,UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
