package org.qboot.sys.controller;

import org.qboot.sys.dto.SysMenuDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.impl.SysLoginLogService;
import org.qboot.sys.service.impl.SysMenuService;
import org.qboot.sys.service.impl.SysUserService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.utils.TreeHelper;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Title: LoginController</p>
 * <p>Description: 系统登录</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/user")
public class LoginController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysLoginLogService sysLoginLogService;
	
	private TreeHelper<SysMenuDto> treeHelper = new TreeHelper<SysMenuDto>();
	
	@PostMapping("/getUserMenus")
	public ResponeModel getUserMenus() {
		CustomUser user = SecurityUtils.getUser();
		List<SysMenuDto> menus = null;
		// 系统管理员，拥有最高权限
		if (SecurityUtils.isSuperAdmin()) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(user.getUserId());
		}
		List<SysMenuDto> treeMenus = treeHelper.treeGridList(menus);
		return ResponeModel.ok(treeMenus);
	}

	@GetMapping("/getUserInfo")
	public ResponeModel getUserInfo() {
		CustomUser sysUser = SecurityUtils.getUser();
		if(sysUser != null) {
			return ResponeModel.ok(sysUser);
		}else {
			return ResponeModel.error("failToFindUserInfo");
		}
	}

	@PostMapping("/updateInfo")
	public ResponeModel updateInfo(SysUserDto user) {
		Assert.hasLength(user.getName(), "userNameIsEmpty");
		SysUserDto sysUser = new SysUserDto();
		sysUser.setPhoto(user.getPhoto());
		sysUser.setName(user.getName());
		sysUser.setEmail(user.getEmail());
		sysUser.setMobile(user.getMobile());
		sysUser.setId(SecurityUtils.getUserId());
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

    @GetMapping("/checkFirstLogin")
    public ResponeModel checkFirstLogin() {
    	if (!SecurityUtils.isSuperAdmin(SecurityUtils.getLoginName())) {
    		boolean firstLogin = sysUserService.selectFirstLoginUser(SecurityUtils.getUserId());
        	if(firstLogin) {
        		return ResponeModel.error("loginFirstTimeHint");
        	}
		}
		return ResponeModel.ok();
	}

    @AccLog
	@PostMapping("/updatePwd")
	public ResponeModel updatePwd(@RequestParam String password, @RequestParam String oldPassword) {
		Long userId = SecurityUtils.getUserId();
		if (!sysUserService.validatePwd(oldPassword, userId)) {
			return ResponeModel.error("originalPasswordIncorrect");
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(userId);
		sysUser.setPassword(password);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

	@GetMapping("/switchLanguage")
	public ResponeModel switchLanguage(@RequestParam String lang){
		Long userId = SecurityUtils.getUserId();
		if (StringUtils.isEmpty(lang)) {
			return ResponeModel.error("langIncorrect");
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(userId);
		sysUser.setLang(lang);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

}