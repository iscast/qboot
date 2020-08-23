package org.iscast.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iscast.common.config.RdpSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurer;
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

import org.iscast.common.utils.SpringContextHolder;

/**
 * <p>Title: WebSecurityConfig</p>
 * <p>Description: WEB安全配置:</p>
 * <p>1.</p>
 * <p>2.</p>
 * <p>3.</p>
 * <p>4.</p>
 * 
 * @author history
 * @date 2018-09-07
 * 
 * @EnableWebSecurity 启用Web安全功能
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
	@Autowired
    private RdpSecurityConfig rdpSecurityConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//将自定义的登录配置放入链中
        if(!CollectionUtils.isEmpty(rdpSecurityConfig.getSecurityConfigurerNames())){
            for (String confName : rdpSecurityConfig.getSecurityConfigurerNames()) {
                SecurityConfigurer securityConfigurer = SpringContextHolder.getBean(confName, SecurityConfigurer.class);
                http.apply(securityConfigurer);
            }
        }
		http.authorizeRequests().antMatchers("/public/**").permitAll()
		.antMatchers("/**/*.html").permitAll()
		.antMatchers((adminPath + "/sys/user/getPublicKey")).permitAll()
		.antMatchers((adminPath + "/**")).authenticated()
		// 允许跨域
		.antMatchers(HttpMethod.OPTIONS).permitAll() 
		.and().formLogin().loginProcessingUrl(adminPath + "/login").permitAll()
		.and().logout().logoutUrl(adminPath + "/logout").logoutSuccessHandler(loginResultHandler())

		.and().csrf().disable()
		.headers().xssProtection().disable().frameOptions().disable()
		.and().addFilterBefore(rdpAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		/** 重复登录  */
		http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredSessionStrategy(rdpSessionInformationExpiredStrategy());
		/** 登录异常处理  */
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				logger.info("{} no login!", request.getRequestURI());
				//response.setStatus(SecurityStatus.NO_LOGIN.getValue());
				response.setStatus(401);
			}
		}).accessDeniedHandler(new QAccessDeniedHandler());

	}
	
	/**
	 * 配置user-detail服务
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(rdpUserDetailsService()).passwordEncoder(new QPasswordEncoder());
	}
	
	/**
	 * 自定义登录过滤器 RdpAuthenticationFilter
	 * @return
	 * @throws Exception
	 */
	@Bean
	public QAuthenticationFilter rdpAuthenticationFilter() throws Exception {
		QAuthenticationFilter rdpAuthenticationFilter = new QAuthenticationFilter(adminPath + "/login");
		rdpAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
		rdpAuthenticationFilter.setAuthenticationSuccessHandler(loginResultHandler());
		rdpAuthenticationFilter.setAuthenticationFailureHandler(loginResultHandler());
		rdpAuthenticationFilter.setRememberMeServices(tokenBasedRememberMeServices());
		return rdpAuthenticationFilter;
	}
	
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices("cn.swiftpass", rdpUserDetailsService());
        // 设置cookie过期时间
        tbrms.setTokenValiditySeconds(TOKEN_VALIDITY_SECONDS);
        tbrms.setParameter("rememberMe");
        return tbrms;
    }

	@Bean
	public QUserDetailsService rdpUserDetailsService() {
		QUserDetailsService rdpUserDetailsService = new QUserDetailsService();
		return rdpUserDetailsService;
	}
	
	@Bean
	public QSessionInformationExpiredStrategy rdpSessionInformationExpiredStrategy() {
		QSessionInformationExpiredStrategy rdpSessionInformationExpiredStrategy = new QSessionInformationExpiredStrategy();
		return rdpSessionInformationExpiredStrategy;
	}
	
	@Bean
	public LoginResultHandler loginResultHandler() {
		return new LoginResultHandler();
	}
}
