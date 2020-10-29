package org.qboot.common.security;

import org.junit.Test;

public class QPasswordEncoderTest {
	
	@Test
	public void encode() {
		CustomPasswordEncoder encoder = new CustomPasswordEncoder();
		System.out.println(encoder.encode("123456rNBdNtjuefmwLGzXjHoN"));
	}
}
