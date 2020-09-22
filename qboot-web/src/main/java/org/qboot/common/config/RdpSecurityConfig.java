package org.qboot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 国际化配置
 * @author history
 */
@Configuration
@ConfigurationProperties(prefix="rdp.security")
public class RdpSecurityConfig implements WebMvcConfigurer{

    /**
     * 是否控制重复登录
     */
    private Boolean singleSessionControl = true;
    /**
     * token有效期
     */
	private Integer tokenValidateSeconds = 2 * 60 * 60;

    /**
     * SecurityConfigurer名称集合
     */
	private List<String> securityConfigurerNames;

	//getters and setters

    public List<String> getSecurityConfigurerNames() {
        return securityConfigurerNames;
    }
    public void setSecurityConfigurerNames(List<String> securityConfigurerNames) {
        this.securityConfigurerNames = securityConfigurerNames;
    }
    public Integer getTokenValidateSeconds() {
        return tokenValidateSeconds;
    }
    public void setTokenValidateSeconds(Integer tokenValidateSeconds) {
        this.tokenValidateSeconds = tokenValidateSeconds;
    }
    public Boolean getSingleSessionControl() {
        return singleSessionControl;
    }
    public void setSingleSessionControl(Boolean singleSessionControl) {
        this.singleSessionControl = singleSessionControl;
    }
}
