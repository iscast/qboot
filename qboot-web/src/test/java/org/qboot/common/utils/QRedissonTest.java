package org.qboot.common.utils;

import org.qboot.RapidDevelopmentPlatformApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class QRedissonTest extends RapidDevelopmentPlatformApplicationTests{
	
	@Autowired
	QRedisson redisson;
	
	@Test
	public void set(){
		redisson.set("name", "iscast");
	}
	
	@Test
	public void get(){
		logger.debug(redisson.get("name"));
	}
	
	@Test
	public void del(){
		redisson.del("name");
	}
	
	@Test
	public void setList(){
		List<String> list = Arrays.asList("a","b","c");
		redisson.setList("list_test", list);
	}
	
	
}