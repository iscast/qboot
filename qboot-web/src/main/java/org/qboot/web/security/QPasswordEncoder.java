package org.qboot.web.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * <p>Title: LoginPasswordEncoder</p>
 * <p>Description: 自定义登录密码加密</p>
 * 
 * @author history
 * @date 2018-09-11
 */
public class QPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		Assert.notNull(rawPassword, "密码为空");
		return DigestUtils.sha256Hex(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
