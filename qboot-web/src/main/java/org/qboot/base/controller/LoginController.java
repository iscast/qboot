package org.qboot.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.qboot.base.dto.SysMenu;
import org.qboot.base.dto.SysUser;
import org.qboot.base.service.impl.SysLoginLogService;
import org.qboot.base.service.impl.SysMenuService;
import org.qboot.base.service.impl.SysUserService;
import org.qboot.common.controller.BaseController;
import org.qboot.common.utils.TreeHelper;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;

/**
 * <p>Title: LoginController</p>
 * <p>Description: 系统登录</p>
 * 
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
	
	private TreeHelper<SysMenu> treeHelper = new TreeHelper<SysMenu>();
	
	@PostMapping("/getUserMenus")
	public ResponeModel getUserMenus() {
		QUser user = SecurityUtils.getUser();
		List<SysMenu> menus = null;
		// 系统管理员，拥有最高权限
		if (SecurityUtils.isSuperAdmin()) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(user.getUserId());
		}
		List<SysMenu> treeMenus = treeHelper.treeGridList(menus);
		return ResponeModel.ok(treeMenus);
	}

	@GetMapping("/getUserInfo")
	public ResponeModel getUserInfo() {
		QUser sysUser = SecurityUtils.getUser();
		if(sysUser != null) {
			return ResponeModel.ok(sysUser);
		}else {
			return ResponeModel.error("sys.response.msg.failToFindUserInfo");
		}
	}

	@PostMapping("/updateInfo")
	public ResponeModel updateInfo(SysUser user) {
		Assert.hasLength(user.getName(), "sys.response.msg.nameIsEmpty");
		SysUser sysUser = new SysUser();
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
        		return ResponeModel.error("sys.response.msg.loginFirstTimeHint");
        	}
		}
		return ResponeModel.ok();
	}

	@PostMapping("/updatePwd")
	public ResponeModel updatePwd(@RequestParam String password, @RequestParam String oldPassword) {
		Long userId = SecurityUtils.getUserId();
		if (!sysUserService.validatePwd(oldPassword, userId)) {
			return ResponeModel.error("sys.response.msg.originalPasswordIncorrect");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(userId);
		sysUser.setPassword(password);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

	@GetMapping("/switchLanguage")
	public ResponeModel switchLanguage(@RequestParam String lang){
		Long userId = SecurityUtils.getUserId();
		if (StringUtils.isEmpty(lang)) {
			return ResponeModel.error("sys.response.msg.langIncorrect");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(userId);
		sysUser.setLang(lang);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

}
