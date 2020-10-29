package org.qboot.common.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * <p>Title: CustomPermission</p>
 * <p>Description: 权限ID</p>
 * @author history
 * @date 2018-09-08
 */
public class CustomPermission implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	private String permission;
	
	public CustomPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String getAuthority() {
		return permission;
	}

}
