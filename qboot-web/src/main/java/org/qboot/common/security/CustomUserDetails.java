package org.qboot.common.security;

import java.util.Collection;
import java.util.List;

import org.qboot.common.constants.SysConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录用户详细信息
 * @author iscast
 * @date 2020-09-25
 */
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private CustomUser user;
	private String passowrd;
	private boolean locked; 
	private List<GrantedAuthority> authorities;
	/**
	 * 扩展属性字段
	 * 提供给业务系统进行定制保存值的字段
	 * added by history
	 */
	private Object otherValue;
	
	public CustomUserDetails(CustomUser user, String passowrd, List<GrantedAuthority> authorities, boolean locked) {
		this.user = user;
		this.passowrd = passowrd;
		this.authorities = authorities;
		this.locked = locked;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return passowrd;
	}

	@Override
	public String getUsername() {
		return user.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !SysConstants.SYS_DISABLED.equals(user.getStatus());
	}

	public CustomUser getUser() {
		return user;
	}

	public void setUser(CustomUser user) {
		this.user = user;
	}

	public Object getOtherValue() {
		return otherValue;
	}

	public void setOtherValue(Object otherValue) {
		this.otherValue = otherValue;
	}
	
}