package org.iscast;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**	
 * <p>Title: RapidDevelopmentPlatformApplication</p>
 * <p>Description: 程序入口</p>
 * @author history
 * @date 2018-08-08
 */

@SpringBootApplication
@MapperScan("org.iscast.**.dao")
@ComponentScan(basePackages="org.iscast")
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource(value= {"classpath:/qboot.properties"})
@EnableRedissonHttpSession(maxInactiveIntervalInSeconds=2*60*60, keyPrefix="qsession")
@RestController
@EnableCaching
public class RapidDevelopmentPlatformApplication extends SpringBootServletInitializer{
	
	/**
	 * 启动服务main
	 */
	public static void main(String[] args) {
		SpringApplication.run(RapidDevelopmentPlatformApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RapidDevelopmentPlatformApplication.class);
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
