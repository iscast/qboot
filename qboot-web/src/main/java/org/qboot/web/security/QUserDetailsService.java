package org.qboot.web.security;

import org.apache.commons.lang3.StringUtils;
import org.qboot.sys.dto.SysMenu;
import org.qboot.sys.dto.SysRole;
import org.qboot.sys.dto.SysUser;
import org.qboot.sys.service.impl.SysMenuService;
import org.qboot.sys.service.impl.SysRoleService;
import org.qboot.sys.service.impl.SysUserService;
import org.qboot.common.constants.SysConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Title: LoginUserService</p>
 * <p>Description: 登录用户信息 Service</p>
 * @author history
 * @date 2018-09-08
 */
@Service
public class QUserDetailsService implements UserDetailsService{

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Assert.hasLength(username, "username为空");
		SysUser sysUser = sysUserService.findByLoginName(username);
		if (null == sysUser) {
			throw new UsernameNotFoundException(username + "账号不存在");
		}
		QUser user = new QUser(sysUser.getId(), sysUser.getDeptId(), sysUser.getDeptName(),
				sysUser.getLoginName(), sysUser.getName(), sysUser.getStatus(),sysUser.getFldN1(),sysUser.getFldN2(),sysUser.getFldS1(),sysUser.getFldS2(),sysUser.getLang());
		List<SysRole> roles = sysRoleService.findByUserId(sysUser.getId());
		user.setRoles(roles);
		QUserDetails adminUserDetails = new QUserDetails(user, sysUser.getPassword(), getAuthorities(sysUser.getId(), sysUser.getLoginName()), false);
		return adminUserDetails;
	}
	
	private List<GrantedAuthority> getAuthorities(Long userId,String username){
		Assert.hasLength(username, "username 为空");
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<SysMenu> menus = null;
		if (SecurityUtils.isSuperAdmin(username)) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(userId);
		}
		if (null != menus) {
			menus.forEach((v)->{
				String permissions = v.getPermission();
				if (StringUtils.isNotEmpty(permissions)) {
					List<String> asList = Arrays.asList(StringUtils.split(permissions.trim(), SysConstants.SEPARATOR));
					for (String p :asList) {
						authorities.add(new QPermission(p));
					}
				}
			});
		}
		return authorities;
	}

}
