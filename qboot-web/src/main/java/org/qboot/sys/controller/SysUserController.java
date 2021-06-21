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
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.utils.ValidateUtils;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.exception.errorcode.SysUserErrTable;
import org.qboot.mon.service.MonLoginLogService;
import org.qboot.sys.service.SysRoleService;
import org.qboot.sys.service.SysUserService;
import org.qboot.sys.service.impl.LoginSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    @Autowired
    private MonLoginLogService sysLoginLogService;

    final String initPwdStr = "user:%s password is %s";

	@PreAuthorize("hasAuthority('sys:user:qry')")
	@PostMapping("/qryPage")
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
		boolean isExist = sysUserService.checkLoginName(null, sysUser.getLoginName());
		if(isExist) {
			return ResponeModel.error(SysUserErrTable.SYS_USER_DUPLICATE);
		}
        sysUser.setStatus(SysConstants.SYS_USER_STATUS_INIT);
		sysUser.setCreateBy(SecurityUtils.getLoginName());
		String password = RandomStringUtils.randomAlphanumeric(8);
		sysUser.setPassword(password);
		if(StringUtils.isNotBlank(sysUser.getRoleId())) {
			sysUser.setRoleIds(Arrays.asList(sysUser.getRoleId().split(",")));
		}
		sysUser.setCreateDate(new Date());
		if(sysUserService.save(sysUser) > 0) {
			return ResponeModel.ok(String.format(initPwdStr, sysUser.getName(), password));
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

//    @AccLog
//    @PreAuthorize("hasAuthority('sys:user:update')")
//    @PostMapping("/updateInfo")
//    public ResponeModel updateInfo(SysUserDto user) {
//        MyAssertTools.hasLength(user.getName(), SYS_USER_NAME_EMPTY);
//        SysUserDto sysUser = new SysUserDto();
//        sysUser.setName(user.getName());
//        sysUser.setEmail(user.getEmail());
//        sysUser.setMobile(user.getMobile());
//        sysUser.setId(SecurityUtils.getUserId());
//        if(sysUserService.updateInfo(user) > 0) {
//            return ok();
//        }
//        return ResponeModel.error();
//    }

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
	@PostMapping("/switchStatus")
	public ResponeModel switchStatus(@RequestParam String id) {
		SysUserDto user = sysUserService.findById(id);
		MyAssertTools.notNull(user, SYS_USER_NOTEXISTS);
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_ADMIN);
		}
		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error(SYS_USER_UPDATE_NO_SELF);
		}
        Integer currentStatus = SysConstants.SYS_USER_STATUS_NORMAL;
        if(SysConstants.SYS_USER_STATUS_NORMAL.equals(user.getStatus())) {
            currentStatus = SysConstants.SYS_USER_STATUS_FORBIDDEN;
        }
        user.setStatus(currentStatus);
        user.setUpdateDate(new Date());
        user.setUpdateBy(SecurityUtils.getLoginName());
		if(sysUserService.setStatus(user) > 0) {
			loginSecurityService.clearUserSessions(user.getLoginName());
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
		} else if (!SecurityUtils.isSuperAdmin() && !SecurityUtils.getUserId().equals(id)) {
            return ResponeModel.error(SYS_USER_INIT_PWD_DENIED);
        }

		String password = RandomStringUtils.randomAlphanumeric(8);
        user.setPassword(password);
		if(sysUserService.changePwd(user) > 0) {
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_CHANGE_PWD, user.getLoginName(), request);
            loginSecurityService.clearUserSessions(user.getLoginName());
			return ResponeModel.ok(String.format(initPwdStr, user.getLoginName(), password));
		}
		return ResponeModel.error(SYS_USER_PWD_UPDATE_FAIL);
	}

    @AccLog
	@PostMapping("/resetPwd")
	public ResponeModel resetPwd(@RequestParam String oldPsw, @RequestParam String newPsw, HttpServletRequest request, HttpSession session) {
		SysUserDto existUser = sysUserService.findById(SecurityUtils.getUserId());
        MyAssertTools.notNull(existUser, SYS_USER_NOTEXISTS);

		if(StringUtils.isBlank(oldPsw) || StringUtils.isBlank(newPsw)) {
            return ResponeModel.error(SYS_USER_PWD_EMPTY);
        }

        Object privateKeyObj = session.getAttribute("privateKey");
        session.removeAttribute("privateKey");
        String privateKey = privateKeyObj.toString();

        RSAsecurity instance = RSAsecurity.getInstance();
        String oldPwdDecode = instance.decrypt(privateKey, oldPsw);
        String newPwdDecode = instance.decrypt(privateKey, newPsw);

        if(!sysUserService.validatePwd(oldPwdDecode, SecurityUtils.getUserId())) {
            return ResponeModel.error(SYS_USER_ORIGINAL_PWD_INCORRECT);
        }

        existUser.setId(SecurityUtils.getUserId());
        existUser.setPassword(newPwdDecode);
        existUser.setUpdateBy(SecurityUtils.getLoginName());
        existUser.setUpdateDate(new Date());
        existUser.setStatus(SysConstants.SYS_USER_STATUS_NORMAL);
        if(this.sysUserService.changePwd(existUser) > 0) {
            sysLoginLogService.saveLog(SysConstants.SYS_USER_LOGIN_STATUS_INIT_PWD, existUser.getLoginName(), request);
            loginSecurityService.clearUserSessions(existUser.getLoginName());
            return ResponeModel.ok();
        }
        return ResponeModel.error(SYS_USER_PWD_UPDATE_FAIL);
	}
}
