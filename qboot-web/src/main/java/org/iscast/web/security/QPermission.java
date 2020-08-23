package org.iscast.web.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * <p>Title: RdpPermission</p>
 * <p>Description: 权限ID</p>
 * 
 * @author history
 * @date 2018-09-08
 */
public class QPermission implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	private String permission;
	
	public QPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String getAuthority() {
		return permission;
	}

}
