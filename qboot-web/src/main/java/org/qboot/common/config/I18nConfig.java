package org.qboot.common.config;


import org.qboot.common.utils.ConfigurableSessionLocaleResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 国际化配置
 * @author history
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer{

    @Value("${spring.messages.param_name}")
    private String paramName;
	@Bean
    @ConfigurationProperties(prefix="spring.i18n.default.locale")
    public LocaleResolver localeResolver() {
        return new ConfigurableSessionLocaleResolver() ;
    }
	
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
       LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
       if(!StringUtils.isBlank(paramName)) {
           lci.setParamName(paramName);
       }
       return lci;
   }


   @Override
   public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
   }
}
