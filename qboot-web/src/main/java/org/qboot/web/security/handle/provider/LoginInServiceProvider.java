package org.qboot.web.security.handle.provider;

import org.qboot.common.strategy.OptionalServiceProvider;
import org.springframework.stereotype.Service;

import org.qboot.web.security.handle.ILoginInService;
import org.qboot.web.security.handle.impl.DefaultLoginInServiceImpl;

/**
 * 
 * <p>Title: DefaultLoginInServiceImpl.java</p>
 * <p>Description:登录成功后的定制入口服务提供者 </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: qboot</p>
 * @author history
 * @date 2019年4月22日
 */
@Service
public class LoginInServiceProvider extends OptionalServiceProvider<ILoginInService, DefaultLoginInServiceImpl> {
	
}
