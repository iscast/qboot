package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.qboot.sys.service.impl.SysRoleService;
import org.qboot.sys.service.impl.SysUserService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.utils.IpUtils;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>Title: UserController</p>
 * <p>Description: 系统用户</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private LoginSecurityService loginSecurityService;

    final String initPwdStr = "initPwdSuccess, your password is %s";

	@PreAuthorize("hasAuthority('sys:user:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysUserDto user, BindingResult bindingResult) {
		if(!SecurityUtils.isSuperAdmin()) {
			user.setCreateBy(SecurityUtils.getLoginName());
		}
		PageInfo<SysUserDto> page = sysUserService.findByPage(user);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@RequestMapping("/get")
	public ResponeModel getUser(@RequestParam Long id, HttpServletRequest request) {
		SysUserDto sysUser = sysUserService.findById(id);
		Assert.notNull(sysUser,"userNotExists");
		//用户所拥有的角色
		List<SysRoleDto> roleList = sysRoleService.findByUserId(sysUser.getId());
		List<String> roleIds = Lists.newArrayList();
		StringBuffer roleSB = new StringBuffer();
		for (SysRoleDto sysRole:roleList) {
			roleIds.add(sysRole.getId());
			roleSB.append(sysRole.getId()).append(",");
		}
		sysUser.setRoleId(roleSB.toString());
		sysUser.setRoleIds(roleIds);
		request.getSession().setAttribute("user_update_id_"+request.getRequestedSessionId(), id);
		return ResponeModel.ok(sysUser);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysUserDto sysUser, BindingResult bindingResult) {
		boolean user = sysUserService.checkLoginName(null, sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("userDuplicate");
		}
		sysUser.setStatus(SysConstants.SYS_ENABLE);
		sysUser.setCreateBy(SecurityUtils.getLoginName());
		
		int password = new Random().nextInt(999999);
	    if (password < 100000){
	    	password+= 100000;
	    }
	    
		sysUser.setPassword(String.valueOf(password));
		//密码为空则不更新
		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}
		sysUser.setCreateDate(new Date());
		int cnt = sysUserService.save(sysUser);
		if(cnt > 0) {
			return ResponeModel.ok(String.format(initPwdStr, password));
		}
		return ResponeModel.error();
		
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysUserDto sysUser, BindingResult bindingResult, HttpServletRequest request) {
		boolean user = sysUserService.checkLoginName(sysUser.getId(), sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("loginNameDuplicate");
		}
		
		Object updateId = request.getSession().getAttribute("user_update_id_"+request.getRequestedSessionId());
		if(updateId == null) {
			return ResponeModel.error("failToUpdate");
		}
		if(!String.valueOf(sysUser.getId()).equals(String.valueOf(updateId))) {
			return ResponeModel.error("dataUpdatedAlready");
		}
		
		if (SecurityUtils.isSuperAdmin(sysUser.getLoginName()) && SysConstants.SYS_DISABLE.equals(sysUser.getStatus())) {
			return ResponeModel.error("superAdminCannotBeInvalid");
		}
		//密码为空则不更新
		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}
		int cnt = sysUserService.updateUserRoleSelective(sysUser);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		loginSecurityService.clearUserSessions(sysUser.getLoginName());
		return ResponeModel.error();
	}


    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/updateSelect")
	public ResponeModel updateSelect(@Validated SysUserDto sysUser, BindingResult bindingResult) {
		boolean user = sysUserService.checkLoginName(sysUser.getId(), sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("userDuplicate");
		}
		
		if (SecurityUtils.isSuperAdmin(sysUser.getLoginName()) && SysConstants.SYS_DISABLE.equals(sysUser.getStatus())) {
			return ResponeModel.error("superAdminCannotBeInvalid");
		}
		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}
		sysUser.setUpdateBy(SecurityUtils.getLoginName());
		sysUser.setUpdateDate(new Date());
		int cnt = sysUserService.updateSelect(sysUser);
		if(cnt > 0) {
			loginSecurityService.clearUserSessions(sysUser.getLoginName());
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Long id) {
		SysUserDto user = sysUserService.findById(id);
		Assert.notNull(user,"userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("superAdminCannotBeDeleted");
		}
		if (SecurityUtils.getUserId() == id) {
			return ResponeModel.error("cannotDeleteYourself");
		}
		int cnt = sysUserService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/setStatus")
	public ResponeModel setStatus(@RequestParam Long id, @RequestParam String status) {
		SysUserDto user = sysUserService.findById(id);
		Assert.notNull(user,"userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("superAdminCannotBeModified");
		}
		if (SecurityUtils.getUserId() == id) {
			return ResponeModel.error("cannotUpdateYourOwnStatus");
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(id);
		sysUser.setStatus(status);
		int cnt = this.sysUserService.setStatus(sysUser);
		if(cnt > 0) {
			loginSecurityService.clearUserSessions(sysUser.getLoginName());
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/initPwd")
	public ResponeModel initPwd(@RequestParam Long id, HttpServletRequest request) {
		SysUserDto user = sysUserService.findById(id);
		Assert.notNull(user,"userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("superAdminCannotBeModified");
		}
		if (SecurityUtils.getUserId() ==  id) {
			return ResponeModel.error("initPwdDenied");
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(id);
		int password = new Random().nextInt(999999);
	    if (password < 100000){
	    	password+= 100000;
	    }
		sysUser.setPassword(String.valueOf(password));

		int cnt = this.sysUserService.initPwd(sysUser, 1, IpUtils.getIpAddr(request));
		if(cnt > 0) {
			return ResponeModel.ok(String.format(initPwdStr, password));
		}
		return ResponeModel.error();
	}

    @AccLog
	@PostMapping("/resetPwd")
	public ResponeModel resetPwd(@RequestParam String oldPsw, @RequestParam String newPsw, HttpServletRequest request) {
		SysUserDto user = sysUserService.findById(SecurityUtils.getUserId());
		Assert.notNull(user,"userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("superAdminNotAllowChangePwd");
		}
		boolean validate = sysUserService.validatePwd(oldPsw, SecurityUtils.getUserId());
		if(validate) {
			SysUserDto sysUser = new SysUserDto();
			sysUser.setId(SecurityUtils.getUserId());
			sysUser.setPassword(newPsw);
			int cnt = this.sysUserService.initPwd(sysUser, SysConstants.SYS_USER_PWD_STATUS_CHANGED, IpUtils.getIpAddr(request));
			if(cnt > 0) {
				loginSecurityService.clearUserSessions(sysUser.getLoginName());
				return ResponeModel.ok("changePwdSuccess");
			}
		}
		return ResponeModel.error("originalPwdIncorrect");
	}
	
	/**
	 * 账户锁定24小时 解锁
	 * 用户信息修改panel 双击图像解锁
	 * @param id
	 * @return
	 */
    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/unlock")
	public ResponeModel setStatus(@RequestParam Long id) {
		SysUserDto user = sysUserService.findById(id);
		Assert.notNull(user, "userUnlockNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("superAdminNotAllowChangePwd");
		}
		loginSecurityService.unLock(user.getLoginName());
		return ResponeModel.ok();
	}
	
	@GetMapping("/getPublicKey")
	public ResponeModel getPublicKey(HttpServletRequest request) {
		Tuple2<String, String> keyPair = RSAsecurity.getInstance().generateKeyPair();
		request.getSession().setAttribute("privateKey", keyPair.getT2());
		return ResponeModel.ok("GetPublicKeySuccess",keyPair.getT1());
	}
	
}
