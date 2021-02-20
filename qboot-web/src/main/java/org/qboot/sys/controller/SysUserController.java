package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.IpUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.ValidateUtils;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.exception.errorcode.SysUserErrTable;
import org.qboot.sys.service.SysRoleService;
import org.qboot.sys.service.SysUserService;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysUserErrTable.*;

/**
 * 系统用户
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/sys/user")
public class SysUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private LoginSecurityService loginSecurityService;

    final String initPwdStr = "initPwdSuccess, your password is %s";

	@PreAuthorize("hasAuthority('sys:user:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysUserDto user) {
		if(!SecurityUtils.isSuperAdmin()) {
			user.setCreateBy(SecurityUtils.getLoginName());
		}
		PageInfo<SysUserDto> page = sysUserService.findByPage(user);
		MyAssertTools.notNull(page, SYS_USER_QUERY_FAIL);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam String id, HttpServletRequest request) {
		SysUserDto sysUser = sysUserService.findById(id);
		if(null == sysUser) {
            return ResponeModel.error(SysUserErrTable.SYS_USER_NOTEXISTS);
        }

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
        ValidateUtils.checkBind(bindingResult);
		boolean user = sysUserService.checkLoginName(null, sysUser.getLoginName());
		if(user) {
			return ResponeModel.error(SysUserErrTable.SYS_USER_DUPLICATE);
		}
        sysUser.setStatus(SysConstants.SYS_ENABLED);
		sysUser.setCreateBy(SecurityUtils.getLoginName());
		String password = RandomStringUtils.randomAlphanumeric(8);
		sysUser.setPassword(password);
		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}
		sysUser.setCreateDate(new Date());
		if(sysUserService.save(sysUser) > 0) {
			return ResponeModel.ok(String.format(initPwdStr, password));
		}
		return ResponeModel.error(SYS_USER_SAVE_FAIL);
	}


    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysUserDto sysUser, BindingResult bindingResult, HttpServletRequest request) {
        ValidateUtils.checkBind(bindingResult);
		boolean user = sysUserService.checkLoginName(sysUser.getId(), sysUser.getLoginName());
		if(user) {
			return ResponeModel.error(SYS_USER_LOGINNAME_DUPLICATE);
		}

		Object updateId = request.getSession().getAttribute("user_update_id_"+request.getRequestedSessionId());
		if(updateId == null) {
			return ResponeModel.error(SYS_USER_FAIL_UPDATE);
		}
		if(!String.valueOf(sysUser.getId()).equals(String.valueOf(updateId))) {
			return ResponeModel.error(SYS_USER_UPDATE_OVER_RIGHT);
		}

		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}

		sysUser.setUpdateBy(SecurityUtils.getLoginName());
		sysUser.setUpdateDate(new Date());
		int cnt = sysUserService.update(sysUser);
		if(cnt > 0) {
			loginSecurityService.clearUserSessions(sysUser.getLoginName());
			return ok();
		}
		return ResponeModel.error(SYS_USER_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam String id) {
		SysUserDto user = sysUserService.findById(id);
        MyAssertTools.notNull(user, SYS_USER_NOTEXISTS);

		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error(SYS_USER_DEL_NO_ADMIN);
		}

		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error(SYS_USER_DEL_NO_SELF);
		}
		int cnt = sysUserService.deleteById(id);
		if(cnt > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_USER_DELETE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/setStatus")
	public ResponeModel setStatus(@RequestParam String id, @RequestParam Integer status) {
		SysUserDto user = sysUserService.findById(id);
		MyAssertTools.notNull(user, SYS_USER_NOTEXISTS);
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_ADMIN);
		}
		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_SELF);
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(id);
		sysUser.setStatus(status);
        sysUser.setUpdateDate(new Date());
        sysUser.setUpdateBy(SecurityUtils.getLoginName());
		if(sysUserService.setStatus(sysUser) > 0) {
			loginSecurityService.clearUserSessions(sysUser.getLoginName());
			return ok();
		}
		return ResponeModel.error(SYS_USER_STATUS_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/initPwd")
	public ResponeModel initPwd(@RequestParam String id, HttpServletRequest request) {
		SysUserDto user = sysUserService.findById(id);
        MyAssertTools.notNull(user, SYS_USER_NOTEXISTS);
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_ADMIN);
		}
		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error(SYS_USER_INIT_PWD_DENIED);
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(id);
		String password = RandomStringUtils.randomAlphanumeric(8);
		sysUser.setPassword(password);

		int cnt = this.sysUserService.initPwd(sysUser, SysConstants.SYS_USER_PWD_STATUS_INIT, IpUtils.getIpAddr(request));
		if(cnt > 0) {
			return ResponeModel.ok(String.format(initPwdStr, password));
		}
		return ResponeModel.error(SYS_USER_PWD_UPDATE_FAIL);
	}

    @AccLog
	@PostMapping("/resetPwd")
	public ResponeModel resetPwd(@RequestParam String oldPsw, @RequestParam String newPsw, HttpServletRequest request) {
		SysUserDto user = sysUserService.findById(SecurityUtils.getUserId());
        MyAssertTools.notNull(user, SYS_USER_NOTEXISTS);
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_ADMIN);
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
            return ResponeModel.error(SYS_USER_PWD_UPDATE_FAIL);
		}
		return ResponeModel.error(SYS_USER_ORIGINAL_PWD_INCORRECT);
	}
}
