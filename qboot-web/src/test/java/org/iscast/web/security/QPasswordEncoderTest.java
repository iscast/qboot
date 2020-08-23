package org.iscast.web.security;

import org.junit.Test;

public class QPasswordEncoderTest {
	
	@Test
	public void encode() {
		QPasswordEncoder encoder = new QPasswordEncoder();
		System.out.println(encoder.encode("123456rNBdNtjuefmwLGzXjHoN"));
	}
}
