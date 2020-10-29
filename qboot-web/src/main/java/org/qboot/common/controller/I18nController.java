package org.qboot.common.controller;

import org.qboot.sys.dto.SysParamType;
import org.qboot.sys.service.impl.SysParamTypeService;
import org.qboot.common.entity.ResponeModel;
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;


/**
 * <p>Title: I18nController</p>
 * <p>Description: 国际化控制器</p>
 * @author history
 * @date 2019-04-03 15:58
 */
@RestController
@RequestMapping("/i18n")
public class I18nController extends BaseController {

    @Autowired
    SysParamTypeService sysParamTypeService;

    @GetMapping("/getLocale")
    public ResponeModel getLocale(HttpSession session, String lang){
        QUser user = SecurityUtils.getUser();
        if(StringUtils.isBlank(lang)&&(user!=null&&StringUtils.isNotBlank(user.getLang()))){
            String[] i18nStr = user.getLang().split("_");
            if(i18nStr.length==2){
                Locale userLocale = new Locale(i18nStr[0],i18nStr[1]);
                session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, userLocale);
            }
        }
        Locale locale = LocaleContextHolder.getLocale();
        return ResponeModel.ok(locale);
    }

    @GetMapping("/getType")
    public ResponeModel getType(@RequestParam("paramKey") String paramKey){
        List<SysParamType> paramTypes = sysParamTypeService.findParamTypes(paramKey);
        return ResponeModel.ok(paramTypes);
    }
}
