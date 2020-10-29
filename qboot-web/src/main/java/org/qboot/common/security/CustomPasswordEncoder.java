package org.qboot.common.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * <p>Title: LoginPasswordEncoder</p>
 * <p>Description: custom password encode</p>
 * @author history
 * @date 2018-09-11
 */
public class CustomPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		Assert.notNull(rawPassword, "password is empty");
		return DigestUtils.sha256Hex(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
