package org.qboot.common.security;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.config.SysSecurityConfig;
import org.qboot.common.exception.errorcode.SystemErrTable;
import org.qboot.common.utils.SpringContextHolder;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Title: WebSecurityConfig</p>
 * <p>Description: WEB safe config:</p>
 * @author history
 * @EnableWebSecurity enable Web saft function
 * configure(WebSecurity) 配置Spring Security的Filter链
 * configure(HttpSecurity) 配置如何通过拦截器保护请求
 * configure(AuthenticationManagerBuilder) 配置user-detail服务
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass=true,prePostEnabled=true,securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * cookie 有效期
	 */
	public static int TOKEN_VALIDITY_SECONDS = 2 * 60 * 60;
	
	@Value("${admin.path}")
	private String adminPath;
    @Value("${admin.loginUrl:}")
    private String adminLoginUrl;
    @Autowired
    private SpringContextHolder springContextHolder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        String loginPage = getLoginPage();
		http.authorizeRequests()
		.antMatchers("/public/**",
				"/module/encrypt/jsencrypt.js",
				"/assets/**",
				"/module/i18n/*",
                loginPage,
				"/user/getLoginPage",
				"/user/getPublicKey",
				"/module/_config.js").permitAll()
		.antMatchers((adminPath + "/**")).authenticated()
		// 允许跨域
		.antMatchers(HttpMethod.OPTIONS).permitAll() 
		.and().formLogin().loginProcessingUrl(adminPath + "/login").permitAll()
		.and().logout().logoutUrl(adminPath + "/logout").logoutSuccessHandler(loginResultHandler())

		.and().csrf().disable()
		.headers().xssProtection().disable().frameOptions().disable()
		.and().addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		/** 重复登录  */
		http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredSessionStrategy(customSessionInformationExpiredStrategy());
		/** 登录异常处理  */
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				String requestURI = request.getRequestURI();
				logger.warn("request url :[{}] fail, no login!", requestURI);

				if("/".equals(requestURI)) {
				    if(StringUtils.isNotBlank(adminLoginUrl)) {
					    response.sendRedirect(adminLoginUrl);
                    }else {
					    response.sendRedirect("/login_pc.html");
                    }
				} else {
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().print(JSON.toJSONString(ResponeModel.error(SystemErrTable.AUTH_FAIL)));
				}
			}
		}).accessDeniedHandler(new WebAccessDeniedHandler());
	}

	private String getLoginPage() {
        // 判断是否自定义了登陆页面
        String loginPage = "/login_pc.html";
        if(StringUtils.isNotBlank(adminLoginUrl) && adminLoginUrl.contains("html") && adminLoginUrl.contains("/")) {
            loginPage = adminLoginUrl.substring(adminLoginUrl.lastIndexOf("/"));
        }
        return loginPage;
    }

	/**
	 * 配置user-detail服务
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService()).passwordEncoder(new CustomPasswordEncoder());
	}
	
	/**
	 * 自定义登录过滤器 customAuthenticationFilter
	 */
	@Bean
	public WebAuthenticationFilter customAuthenticationFilter() throws Exception {
		WebAuthenticationFilter customAuthenticationFilter = new WebAuthenticationFilter(adminPath + "/login", springContextHolder);
		customAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
		customAuthenticationFilter.setAuthenticationSuccessHandler(loginResultHandler());
		customAuthenticationFilter.setAuthenticationFailureHandler(loginResultHandler());
		customAuthenticationFilter.setRememberMeServices(tokenBasedRememberMeServices());
		return customAuthenticationFilter;
	}
	
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices("org.qboot", customUserDetailsService());
        // 设置cookie过期时间
        tbrms.setTokenValiditySeconds(TOKEN_VALIDITY_SECONDS);
        tbrms.setParameter("rememberMe");
        return tbrms;
    }

	@Bean
	public CustomUserDetailsService customUserDetailsService() {
		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
		return customUserDetailsService;
	}
	
	@Bean
	public WebSessionInformationExpiredStrategy customSessionInformationExpiredStrategy() {
		WebSessionInformationExpiredStrategy customSessionInformationExpiredStrategy = new WebSessionInformationExpiredStrategy();
		return customSessionInformationExpiredStrategy;
	}
	
	@Bean
	public WebLoginResultHandler loginResultHandler() {
		return new WebLoginResultHandler();
	}
}
