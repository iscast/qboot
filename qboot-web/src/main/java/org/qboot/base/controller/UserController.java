package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.qboot.base.dto.SysRole;
import org.qboot.base.dto.SysUser;
import org.qboot.base.service.impl.LoginSecurityService;
import org.qboot.base.service.impl.SysRoleService;
import org.qboot.base.service.impl.SysUserService;
import org.qboot.common.constant.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.utils.IpUtils;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.SecurityUtils;
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
 * 
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
	
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysUser user, BindingResult bindingResult) {
		if(!SecurityUtils.isSuperAdmin()) {
			user.setCreateBy(SecurityUtils.getLoginName());
		}
		PageInfo<SysUser> page = sysUserService.findByPage(user);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@RequestMapping("/get")
	public ResponeModel getUser(@RequestParam Long id, HttpServletRequest request) {
		SysUser sysUser = sysUserService.findById(id);
		Assert.notNull(sysUser,"sys.response.msg.userNotExists");
		//用户所拥有的角色
		List<SysRole> roleList = sysRoleService.findByUserId(sysUser.getId());
		List<String> roleIds = Lists.newArrayList();
		StringBuffer roleSB = new StringBuffer();
		for (SysRole sysRole:roleList) {
			roleIds.add(sysRole.getId());
			roleSB.append(sysRole.getId()).append(",");
		}
		sysUser.setRoleId(roleSB.toString());
		sysUser.setRoleIds(roleIds);
		request.getSession().setAttribute("user_update_id_"+request.getRequestedSessionId(), id);
		return ResponeModel.ok(sysUser);
	}
	
	@PreAuthorize("hasAuthority('sys:user:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysUser sysUser, BindingResult bindingResult) {
		boolean user = sysUserService.checkLoginName(null, sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("sys.response.msg.userDuplicate");
		}
		sysUser.setStatus(SysUser.STATUS_NORMAL);
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
			return ResponeModel.ok("sys.response.msg.initPwd",new Object[]{password+""});
		}
		return ResponeModel.error();
		
	}
	
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysUser sysUser, BindingResult bindingResult, HttpServletRequest request) {
		boolean user = sysUserService.checkLoginName(sysUser.getId(), sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("sys.response.msg.loginNameDuplicate");
		}
		
		Object updateId = request.getSession().getAttribute("user_update_id_"+request.getRequestedSessionId());
		if(updateId == null) {
			return ResponeModel.error("sys.response.msg.failToUpdate");
		}
		if(!String.valueOf(sysUser.getId()).equals(String.valueOf(updateId))) {
			return ResponeModel.error("sys.response.msg.dataUpdatedAlready");
		}
		
		if (SecurityUtils.isSuperAdmin(sysUser.getLoginName()) && SysUser.STATUS_STOP.equals(sysUser.getStatus())) {
			return ResponeModel.error("sys.response.msg.superAdminCannotBeInvalid");
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
	
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/updateSelect")
	public ResponeModel updateSelect(@Validated SysUser sysUser, BindingResult bindingResult) {
		boolean user = sysUserService.checkLoginName(sysUser.getId(), sysUser.getLoginName());
		if(user) {
			return ResponeModel.error("sys.response.msg.userDuplicate");
		}
		
		if (SecurityUtils.isSuperAdmin(sysUser.getLoginName()) && SysUser.STATUS_STOP.equals(sysUser.getStatus())) {
			return ResponeModel.error("sys.response.msg.superAdminCannotBeInvalid");
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
	
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Long id) {
		SysUser user = sysUserService.findById(id);
		Assert.notNull(user,"sys.response.msg.userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("sys.response.msg.superAdminCannotBeDeleted");
		}
		if (SecurityUtils.getUserId() == id) {
			return ResponeModel.error("sys.response.msg.cannotDeleteYourself");
		}
		int cnt = sysUserService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/setStatus")
	public ResponeModel setStatus(@RequestParam Long id, @RequestParam String status) {
		SysUser user = sysUserService.findById(id);
		Assert.notNull(user,"sys.response.msg.userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("sys.response.msg.superAdminCannotBeModified");
		}
		if (SecurityUtils.getUserId() == id) {
			return ResponeModel.error("sys.response.msg.cannotUpdateYourOwnStatus");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		sysUser.setStatus(status);
		int cnt = this.sysUserService.setStatus(sysUser);
		if(cnt > 0) {
			loginSecurityService.clearUserSessions(sysUser.getLoginName());
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/initPwd")
	public ResponeModel initPwd(@RequestParam Long id, HttpServletRequest request) {
		SysUser user = sysUserService.findById(id);
		Assert.notNull(user,"sys.response.msg.userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("sys.response.msg.superAdminCannotBeModified");
		}
		if (SecurityUtils.getUserId() ==  id) {
			return ResponeModel.error("sys.response.msg.initPwdDenied");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		int password = new Random().nextInt(999999);
	    if (password < 100000){
	    	password+= 100000;
	    }
		sysUser.setPassword(String.valueOf(password));
		
		int cnt = this.sysUserService.initPwd(sysUser, 1, IpUtils.getIpAddr(request));
		if(cnt > 0) {
			return ResponeModel.ok("sys.response.msg.initPwdSuccess",new Object[]{password+""});
		}
		return ResponeModel.error();
	}
	
	@PostMapping("/resetPwd")
	public ResponeModel resetPwd(@RequestParam String oldPsw, @RequestParam String newPsw, HttpServletRequest request) {
		SysUser user = sysUserService.findById(SecurityUtils.getUserId());
		Assert.notNull(user,"sys.response.msg.userNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("sys.response.msg.superAdminNotAllowChangePwd");
		}
		boolean validate = sysUserService.validatePwd(oldPsw, SecurityUtils.getUserId());
		if(validate) {
			SysUser sysUser = new SysUser();
			sysUser.setId(SecurityUtils.getUserId());
			sysUser.setPassword(newPsw);
			int cnt = this.sysUserService.initPwd(sysUser, SysConstants.SYS_USER_PWD_STATUS_CHANGED, IpUtils.getIpAddr(request));
			if(cnt > 0) {
				loginSecurityService.clearUserSessions(sysUser.getLoginName());
				return ResponeModel.ok("sys.response.msg.changePwdSuccess");
			}
		}
		return ResponeModel.error("sys.response.msg.originalPwdIncorrect");
	}
	
	/**
	 * 账户锁定24小时 解锁
	 * 用户信息修改panel 双击图像解锁
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/unlock")
	public ResponeModel setStatus(@RequestParam Long id) {
		SysUser user = sysUserService.findById(id);
		Assert.notNull(user, "sys.response.msg.userUnlockNotExists");
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error("sys.response.msg.superAdminNotAllowChangePwd");
		}
		loginSecurityService.unLock(user.getLoginName());
		return ResponeModel.ok();
	}
	
	@GetMapping("/getPublicKey")
	public ResponeModel getPublicKey(HttpServletRequest request) {
		Tuple2<String, String> keyPair = RSAsecurity.getInstance().generateKeyPair();
		request.getSession().setAttribute("privateKey", keyPair.getT2());
		return ResponeModel.ok("sys.response.msg.GetPublicKeySuccess",keyPair.getT1());
	}
	
}
