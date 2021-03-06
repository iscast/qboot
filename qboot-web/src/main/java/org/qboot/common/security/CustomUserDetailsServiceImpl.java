package org.qboot.common.security;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.sys.dto.SysMenuDto;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.SysMenuService;
import org.qboot.sys.service.SysRoleService;
import org.qboot.sys.service.SysUserService;
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
 * 登录用户信息 Service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Assert.hasLength(username, "name is null");

		// check superadmin
		if(SecurityUtils.isSuperAdmin(username)) {
            SysUserDto admin = SecurityUtils.getAdminUser(username);
            CustomUser user = new CustomUser(admin);
            return new CustomUserDetails(user, admin.getPassword(), getAuthorities(admin.getId(), username), false);
        }

		SysUserDto sysUser = sysUserService.findByLoginName(username);
		if (null == sysUser) {
			throw new UsernameNotFoundException(username + "not exist");
		}
		CustomUser user = new CustomUser(sysUser);
		List<SysRoleDto> roles = sysRoleService.findByUserId(sysUser.getId());
		user.setRoles(roles);
        SysUserDto userSecretInfo = sysUserService.findSecretInfo(sysUser);
		CustomUserDetails adminUserDetails = new CustomUserDetails(user, userSecretInfo.getPassword(), getAuthorities(sysUser.getId(), sysUser.getLoginName()), false);
		return adminUserDetails;
	}
	
	private List<GrantedAuthority> getAuthorities(String userId,String username){
		Assert.hasLength(username, "username is null");
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<SysMenuDto> menus = null;
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
						authorities.add(new CustomPermission(p));
					}
				}
			});
		}
		return authorities;
	}

}
