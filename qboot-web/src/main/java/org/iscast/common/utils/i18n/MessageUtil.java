package org.iscast.common.utils.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title: MessageUtil</p>
 * <p>Description: 国际化工具</p>
 * @author history
 * @date 2019-04-03 15:05
 */
public class MessageUtil {
    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class) ;
    protected static MessageSourceAccessor accessor;
    protected static MessageSource messageSource;
    public static Set<String> set = new HashSet<String>();
    static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    //静态块初始化资源
    static{ addLocation("classpath*:i18n/*"); }
    //初始化资源文件的存储器
    protected static void initMessageSourceAccessor(){
        String[] basenames = new String[set.size()];
        set.toArray(basenames);
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames(basenames);
        reloadableResourceBundleMessageSource.setCacheSeconds(5);
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
        messageSource = reloadableResourceBundleMessageSource;
    }
    //删除指定位置的资源
    public static void removeLocation(String locationPattern){
        try {
            Resource[] resources = resourcePatternResolver.getResources(locationPattern);
            for (int i = 0; i < resources.length; i++) {
                String url = resources[i].getURL().toString();
                int lastIndex = url.lastIndexOf("/");
                String prefix = url.substring(0,lastIndex + 1);
                String suffix = url.substring(lastIndex+1);
                suffix = suffix.split("\\.")[0].split("_")[0];
                set.remove(prefix + suffix);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initMessageSourceAccessor();
    }
    //加载指定位置的资源文件 
    public static void addLocation(String locationPattern){
        try {
            Resource[] resources = resourcePatternResolver.getResources(locationPattern);
            for (int i = 0; i < resources.length; i++) { String url = resources[i].getURL().toString();
                int lastIndex = url.lastIndexOf("/");
                String prefix = url.substring(0,lastIndex + 1);
                String suffix = url.substring(lastIndex+1);
                suffix = suffix.split("\\.")[0].split("_")[0];
                set.add(prefix + suffix);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initMessageSourceAccessor();
    }
    private MessageUtil(){ }

    /**
     *  获取国际化语言信息
     * @param code 国际化key值
     * @return
     */
    public static String getMessage(String code){
        return accessor.getMessage(code, LocaleContextHolder.getLocale());
    }

    /**
     *  获取国际化语言信息
     * @param code 国际化key值
     * @param defaultMessage 如果key无对应的value,返回此默认消息
     * @return
     */
    public static String getMessage(String code,String defaultMessage){
        return accessor.getMessage(code,defaultMessage,LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object[] args){
        return accessor.getMessage(code,args,LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object[] args,String defaultMessage){
        return accessor.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }
}
