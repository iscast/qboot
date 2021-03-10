package org.qboot.common.controller;

import org.apache.commons.lang.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.Locale;


/**
 * 国际化控制器
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("/i18n")
public class I18nController extends BaseController {

    @AccLog
    @GetMapping("/getLocale")
    public ResponeModel getLocale(HttpSession session, String lang){
        if(StringUtils.isNotBlank(lang) && !"null".equalsIgnoreCase(lang)) {
            String[] i18nStr = lang.split("_");
            Locale locale = new Locale(i18nStr[0],i18nStr[1]);
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
            return ResponeModel.ok(locale);
        }

        Object sessionAttr = session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        if(null != sessionAttr) {
            return ResponeModel.ok(sessionAttr);
        }

        CustomUser user = SecurityUtils.getUser();
        if(user!=null && StringUtils.isNotBlank(user.getLang())) {
            String[] i18nStr = user.getLang().split("_");
            Locale userLocale = new Locale(i18nStr[0],i18nStr[1]);
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, userLocale);
            return ResponeModel.ok(userLocale);
        }

        Locale locale = LocaleContextHolder.getLocale();
        return ResponeModel.ok(locale);
    }
}
