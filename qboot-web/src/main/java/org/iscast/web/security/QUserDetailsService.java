package org.iscast.web.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.iscast.web.security.handle.provider.LoginInServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.iscast.base.dto.SysMenu;
import org.iscast.base.dto.SysRole;
import org.iscast.base.dto.SysUser;
import org.iscast.base.service.impl.SysMenuService;
import org.iscast.base.service.impl.SysRoleService;
import org.iscast.base.service.impl.SysUserService;
import org.iscast.common.constant.QConstants;

/**
 * <p>Title: LoginUserService</p>
 * <p>Description: 登录用户信息 Service</p>
 * 
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
	@Autowired
	private LoginInServiceProvider loginInServiceProvider;
	@Autowired
	private Environment env;
	
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
		//从application.properties中获取login.in.bussNumber配置的值（定制的流水号，可为空）
		loginInServiceProvider.getService(env.getProperty("login.in.bussNumber")).loginInAfter(adminUserDetails);
		
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
					List<String> asList = Arrays.asList(StringUtils.split(permissions.trim(), QConstants.SEPARATOR));
					for (String p :asList) {
						authorities.add(new QPermission(p));
					}
				}
			});
		}
		return authorities;
	}

}
