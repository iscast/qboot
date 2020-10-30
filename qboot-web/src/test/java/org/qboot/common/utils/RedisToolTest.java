package org.qboot.common.utils;

import org.junit.Test;
import org.qboot.QApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class RedisToolTest extends QApplicationTests {
	
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

    public static void main(String[] args) {
        int original = 1100;

        double increate = 0.05;
        int year = 20;
        int cost = 0;

        int currentRent = original;
        for(int i=0; i<year; i++) {
            int rent = 0;
            if(i != 0) {
                rent = currentRent;
            }
        }

        int target = 400000;
    }

}
