package org.qboot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * <p>Title: ConfigurableSessionLocaleResolver</p>
 * <p>Description: locale解析器</p>
 * @author history
 * @date 2019-04-03 14:42
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
