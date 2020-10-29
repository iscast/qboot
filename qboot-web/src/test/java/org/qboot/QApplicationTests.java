package org.qboot;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**	
 * <p>Title: RapidDevelopmentPlatformApplicationTests</p>
 * <p>Description: 程序测试入口</p>
 * @author history
 * @date 2018-10-22
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class QApplicationTests {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    
    //设置mockMvc
    @Before
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
