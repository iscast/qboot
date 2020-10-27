package org.qboot;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**	
 * <p>Title: QBootApplication</p>
 * <p>Description: 程序入口</p>
 * @author history
 * @date 2018-08-08
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.qboot","com"}, includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
                classes = {org.springframework.web.bind.annotation.RestController.class, org.springframework.stereotype.Controller.class})})
@MapperScan({"org.qboot.**.dao", "com.**.dao"})
@EnableTransactionManagement(proxyTargetClass=true)
@EnableRedissonHttpSession(maxInactiveIntervalInSeconds=2*60*60, keyPrefix="qsession")
@RestController
@EnableCaching
public class QBootApplication extends SpringBootServletInitializer{
	
	/**
	 * 启动服务main
	 */
	public static void main(String[] args) {
		SpringApplication.run(QBootApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(QBootApplication.class);
    }
	
	/**
	 * 适配Spring Cache
	 */
	@Autowired
	RedissonClient redissonClient;
	@Bean
	protected CacheManager cacheManager() {
		return new RedissonSpringCacheManager(redissonClient);
    }
}
