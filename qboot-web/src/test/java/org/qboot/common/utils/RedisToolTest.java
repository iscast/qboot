package org.qboot.common.utils;

import org.junit.Test;
import org.qboot.RapidDevelopmentPlatformApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class RedisToolTest extends RapidDevelopmentPlatformApplicationTests{
	
	@Autowired
    RedisTools redisTools;
	
	@Test
	public void set(){
		redisTools.set("name", "iscast");
	}
	
	@Test
	public void get(){
		logger.debug(redisTools.get("name"));
	}
	
	@Test
	public void del(){
		redisTools.del("name");
	}
	
	@Test
	public void setList(){
		List<String> list = Arrays.asList("a","b","c");
		redisTools.setList("list_test", list);
	}
	
	
}
