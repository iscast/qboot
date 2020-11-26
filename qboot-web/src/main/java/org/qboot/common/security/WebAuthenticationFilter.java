package org.qboot.common.security;

import org.qboot.common.filter.ILoginProcessFilter;
import org.qboot.common.filter.LoginProcessFailException;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.utils.SpringContextHolder;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
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
    private ILoginProcessFilter customFilter;

    public WebAuthenticationFilter(String loginPath, SpringContextHolder springContextHolder) {
        super(new AntPathRequestMatcher(loginPath, "POST"));

        // 初始化项目的自定义登陆流程处理过滤器
//        Set<Class<? extends ILoginProcessFilter>> classes = null;
//        if (null != classes || classes.size() >= 0) {
//            Iterator<Class<? extends ILoginProcessFilter>> iterator = classes.iterator();
//
//            if(iterator.hasNext()) {
//                Class aClass = iterator.next();
//                try {
//                    ILoginProcessFilter customFilter = (ILoginProcessFilter) aClass.newInstance();
//                    customFilterList.add(customFilter);
//                } catch (Exception e) {
//                    logger.error("init custom auth Filter error", e);
//                }
//            }
//
//            Collections.sort(customFilterList, new Comparator<ILoginProcessFilter>() {
//                @Override
//                public int compare(ILoginProcessFilter a, ILoginProcessFilter b) {
//                    return Integer.compare(a.order(), b.order());
//                }
//            });
//        }
        customFilter = springContextHolder.getBeanByClass(ILoginProcessFilter.class);
    }

	public WebAuthenticationFilter(String loginPath) {
		super(new AntPathRequestMatcher(loginPath, "POST"));
	}

	 @Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

         if(null != customFilter && !customFilter.doBusiness(request, response)) {
             throw new LoginProcessFailException("process login auth fail");
         }

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = RSAsecurity.getInstance().decrypt(String.valueOf(request.getSession().getAttribute("privateKey")), username.trim());
		SysUserDto sysUser = sysUserService.findByLoginName(username);
		if (sysUser == null) {
			throw new UsernameNotFoundException(username + "account not exist");
		}
		if (sysUser.getStatus().equals("0")) {
			throw new DisabledException(username + "账号已锁定，请联系管理员！");
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
