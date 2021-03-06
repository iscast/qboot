package org.qboot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * custom spring context
 * @Author: iscast
 * @Date: 2020/8/22 19:15
 */
@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    public SpringContextHolder() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        logger.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);
        if (applicationContext != null) {
            logger.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + applicationContext);
        }
        this.applicationContext = applicationContext;
    }

    public void destroy() throws Exception {
        clear();
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        assertContextInjected();
        T t = null;
        try {
            t = applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            logger.info("springContext can't find {} no exist, err msg:{}  return null", clazz.getName(), e.getMessage());
        }
        return t;
    }

    public static void clear() {
        logger.debug("clear ApplicationContext in the SpringContextHolder");
        applicationContext = null;
    }

    /**
     * get spring.profiles.active
     */
    public static String getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext not exist,plz config SpringContextHolder in applicationContext.xml");
        }
    }
}

