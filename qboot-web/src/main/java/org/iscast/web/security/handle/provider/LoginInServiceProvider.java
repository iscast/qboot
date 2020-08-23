package org.iscast.web.security.handle.provider;

import org.iscast.common.strategy.OptionalServiceProvider;
import org.springframework.stereotype.Service;

import org.iscast.web.security.handle.ILoginInService;
import org.iscast.web.security.handle.impl.DefaultLoginInServiceImpl;

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
