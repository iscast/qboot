package org.qboot.common.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * CustomPermission
 * @author iscast
 * @date 2020-09-25
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
