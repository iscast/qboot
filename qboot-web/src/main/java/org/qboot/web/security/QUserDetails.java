package org.qboot.web.security;

import java.util.Collection;
import java.util.List;

import org.qboot.base.dto.SysUser;
import org.qboot.common.constant.SysConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>Title: LoginUser</p>
 * <p>Description: 登录用户详细信息</p>
 * @author history
 * @date 2018-09-08
 */
public class QUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private QUser user;
	private String passowrd;
	private boolean locked; 
	private List<GrantedAuthority> authorities;
	/**
	 * 扩展属性字段
	 * 提供给业务系统进行定制保存值的字段
	 * added by history
	 */
	private Object otherValue;
	
	public QUserDetails(QUser user, String passowrd, List<GrantedAuthority> authorities, boolean locked) {
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
		return !SysConstants.SYS_DISABLE.equals(user.getStatus());
	}

	public QUser getUser() {
		return user;
	}

	public void setUser(QUser user) {
		this.user = user;
	}

	public Object getOtherValue() {
		return otherValue;
	}

	public void setOtherValue(Object otherValue) {
		this.otherValue = otherValue;
	}
	
}