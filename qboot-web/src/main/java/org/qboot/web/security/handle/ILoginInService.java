package org.qboot.web.security.handle;

import org.qboot.common.strategy.OptionalServiceSelector;
import org.qboot.web.security.QUserDetails;

/**
 * 
 * <p>Title: ILoginInService.java</p>
 * <p>Description: 登录成功后的定制入口，使用RDP的业务系统可以扩展此接口增加属于自己的业务逻辑</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: qboot</p>
 * @author history
 * @date 2019年4月22日
 */
public interface ILoginInService extends OptionalServiceSelector {

	/**
	 * 登录后的定制方法，给业务系统调用
	 * @param adminUserDetails 用户登录详细信息对象
	 */
	void loginInAfter(QUserDetails adminUserDetails);
}
