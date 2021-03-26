package org.qboot.sys.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.CodecUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysUserDao;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.dto.SysUserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysUserErrTable.*;

/**
 * 系统用户service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysUserService extends CrudService<SysUserDao, SysUserDto> {

    @Autowired
    private SysLoginLogService sysLoginLogService;

	public SysUserDto findByLoginName(String loginName) {
        MyAssertTools.hasLength(loginName, SYS_USER_LOGINNAME_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setLoginName(loginName);
		return this.d.findByDto(qry);
	}

    public SysUserDto findByMobile(String mobile) {
        MyAssertTools.hasLength(mobile, SYS_USER_MOBILE_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setMobile(mobile);
        return this.d.findByDto(qry);
    }

    public SysUserDto findByDto(SysUserDto dto) {
        return this.d.findByDto(dto);
    }
	
	public boolean checkLoginName(Long userId, String loginName) {
        MyAssertTools.hasLength(loginName, SYS_USER_LOGINNAME_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setLoginName(loginName);
		SysUserDto exesitUser = this.d.findByDto(qry);
		
		if(exesitUser == null) {
			return false;
		}else if(userId != null && exesitUser.getId().equals(userId)) {
			return false;
		}
		return true;
	}

	@Override
	public int save(SysUserDto t) {
		String salt = RandomStringUtils.randomAlphanumeric(20);
		t.setSalt(salt);
		//处理密码
		t.setPassword(encryptPwd(t.getPassword(), salt));
		t.setFldN1(0);//首次登录需要修改密码
		int cnt = this.d.insert(t);
		if(cnt > 0) {
			// 插入角色
			this.saveUserRole(t.getRoleIds(), t.getId());
			return Integer.parseInt(String.valueOf(t.getId()));
		}else {
			return 0;
		}
			
	}
	
	@Override
	public int update(SysUserDto t) {
		SysUserDto sysUser = this.findById(t.getId());
        MyAssertTools.notNull(sysUser, SYS_USER_NOTEXISTS);
		return super.update(t);
	}


    @Override
    public int updateById(SysUserDto t) {
		SysUserDto sysUser = this.findById(t.getId());
        MyAssertTools.notNull(sysUser, SYS_USER_NOTEXISTS);

		t.setVersion(sysUser.getVersion());
		int cnt = this.d.updateSelect(t);
		if(cnt > 0) {
			// 删除user关联的所有role
			this.d.deleteUserRoleByUserId(t.getId());
			this.saveUserRole(t.getRoleIds(), t.getId());
		}
		return cnt;
	}
	
	public int deleteById(Long id) {
		// 删除用户角色
		// 删除user关联的所有role
		this.d.deleteUserRoleByUserId(id);
		return super.deleteById(id);
	}
	private int saveUserRole(List<String> roleIds, Long userId) {
		// 插入角色
		if (null == roleIds || roleIds.isEmpty()) {
			return 0;
		}
		List<SysUserRoleDto> userRoleList =  new ArrayList<>();
		for (String roleId : roleIds) {
			userRoleList.add(new SysUserRoleDto(String.valueOf(userId), roleId));
		}
		return this.d.insertUserRole(userRoleList);
	}
	
	public String encryptPwd(String password,String salt) {
	    MyAssertTools.hasLength(password, SYS_USER_PWD_EMPTY);
	    MyAssertTools.hasLength(salt, SYS_USER_SALT_EMPTY);
		return CodecUtils.sha256(password + salt);
	}
	
	public boolean validatePwd(String password, Long userId) {
        MyAssertTools.hasLength(password, SYS_USER_PWD_EMPTY);
        MyAssertTools.notNull(userId, SYS_USER_UERID_EMPTY);
        SysUserDto qry = new SysUserDto();
        qry.setId(userId);
        SysUserDto secretInfo = d.findSecretInfo(qry);
        MyAssertTools.notNull(secretInfo, SYS_USER_NOTEXISTS);
		return secretInfo.getPassword().equals(CodecUtils.sha256(password + secretInfo.getSalt()));
	}
	
	public int initPwd(SysUserDto t, int initFlag, String ip) {
        SysUserDto secretInfo = d.findSecretInfo(t);
        MyAssertTools.notNull(secretInfo, SYS_USER_NOTEXISTS);
        secretInfo.setPassword(encryptPwd(t.getPassword(), secretInfo.getSalt()));
		if(initFlag == SysConstants.SYS_USER_PWD_STATUS_CHANGED) {
            secretInfo.setFldN1(1);
		}else {
            //首次登录需要修改密码
            secretInfo.setFldN1(0);
		}
		int cnt = d.initPwd(secretInfo);
		sysLoginLogService.loginLogByLoginName(SysConstants.SYS_USER_LOGIN_STATUS_SUCCESS, secretInfo.getLoginName(), ip, "", "", "", "", initFlag);
		return cnt;
	}
	
	public int setStatus(SysUserDto t) {
        MyAssertTools.notNull(t, SYS_USER_NOTEXISTS);
		return d.setStatus(t);
	}
	
	public boolean selectFirstLoginUser(Long userId){
		SysUserDto sysUser = this.findById(userId);
    	if(sysUser.getFldN1() == null || sysUser.getFldN1() != 1) {
    		return true;
    	}
    	return false;
	}

	public SysUserDto findSecretInfo(SysUserDto qry){
        MyAssertTools.notNull(qry, SYS_USER_NOTEXISTS);
        return d.findSecretInfo(qry);
    }
}
