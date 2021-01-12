package org.qboot.common.security;

/**
 * HTTP返回状态码
 * @author iscast
 * @date 2020-09-25
 */
public enum SecurityStatus {
	/**
	 * 没有登录
	 */
	NO_LOGIN(600, "no login"),
	
	/**
	 * 没有权限
	 */
	NO_PERMISSION(601, "no permission"),
	
	/**
	 * 重复登录
	 */
	REPEAT_LOGIN(602, "repeat logins");


	private final int value;

	private final String reasonPhrase;

	SecurityStatus(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int getValue() {
		return value;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}
}
