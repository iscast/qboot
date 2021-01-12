package org.qboot.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * locale解析器
 * @author iscast
 * @date 2020-09-25
 */
public class ConfigurableSessionLocaleResolver extends SessionLocaleResolver {
    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    private String language_country ;

    public void setLanguage_country(String language_country) {
        this.language_country = language_country;
        if(StringUtils.isEmpty(this.language_country)){
            this.language_country = "zh_CN" ;
        }
        String[] langCtry = this.language_country.split("_") ;
        if(langCtry.length==2){
            Locale locale = new Locale(langCtry[0], langCtry[1]) ;
            this.setDefaultLocale(locale);
            logger.info("set default locale成功,language_country={}.",language_country);
            return ;
        }
        logger.warn("set default locale fail,language_country={}!",language_country);
    }

}
