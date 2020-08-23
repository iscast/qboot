package org.iscast.common.strategy;

import org.iscast.common.strategy.enums.OptionServiceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:25
 */
public abstract class OptionalServiceProvider<T, S extends T> implements BeanFactoryAware {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConfigurableListableBeanFactory beanFactory;
    private Map<String, T> serviceMap;

    public OptionalServiceProvider() {
    }

    @PostConstruct
    public void init() {
        ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
        Type[] types = pt.getActualTypeArguments();
        Class<T> interfaceClazz = (Class)types[0];
        Class<S> defaultImplClazz = (Class)types[1];
        this.logger.info("可选服务初始化，服务接口为{}，默认实现为{}", interfaceClazz.getName(), defaultImplClazz.getName());
        Map<String, T> serviceBeanMap = this.beanFactory.getBeansOfType(interfaceClazz);
        this.serviceMap = new HashMap(serviceBeanMap.size());
        Iterator iterator = serviceBeanMap.values().iterator();

        while(iterator.hasNext()) {
            Object processor = iterator.next();
            if (!(processor instanceof OptionalServiceSelector)) {
                throw new RuntimeException("可选服务必须实现OptionalServiceSelector接口！");
            }

            OptionalServiceSelector selector = (OptionalServiceSelector)processor;
            if (null != this.serviceMap.get(selector.getBussNumber())) {
                throw new RuntimeException("已经存在业务流水【" + selector.getBussNumber() + "】的服务");
            }

            this.serviceMap.put(selector.getBussNumber(), (T) processor);
        }

    }

    public T getService() {
        return this.serviceMap.get(OptionServiceEnum.DEFAULT.getCode());
    }

    public T getService(String bussNumber) {
        return null != this.serviceMap.get(bussNumber) ? this.serviceMap.get(bussNumber) : this.serviceMap.get(OptionServiceEnum.DEFAULT.getCode());
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
        }

    }
}
