package org.qboot.sys.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.qboot.sys.dao.SysUserDao;
import org.qboot.sys.dto.SysUser;
import org.qboot.sys.dto.SysUserRole;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: SysUserService</p>
 * <p>Description: 系统用户service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysUserService extends CrudService<SysUserDao, SysUser> {

    @Autowired
    private SysLoginLogService sysLoginLogService;

	public SysUser findByLoginName(String loginName) {
		Assert.hasLength(loginName, "loginNameIsEmpty");
		List<SysUser> list = this.d.findByLoginName(loginName);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public boolean checkLoginName(Long userId, String loginName) {
		Assert.hasLength(loginName, "loginNameIsEmpty");
		List<SysUser> list = this.d.findByLoginName(loginName);
		
		if(userId != null && list.isEmpty()) {
			return false;
		}
		if(list.isEmpty()) {
			return false;
		}
		if(userId != null && list.get(0).getId().equals(userId)) {
			return false;
		}
		return true;
	}

	@Override
	public int save(SysUser t) {
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
	public int update(SysUser t) {
		SysUser sysUser = this.findById(t.getId());
		Assert.notNull(sysUser, "userNotExists");
		return super.update(t);
	}
	
	public int updateUserRoleSelective(SysUser t) {
		SysUser sysUser = this.findById(t.getId());
		Assert.notNull(sysUser, "userNotExists");
		int cnt = super.update(t);
		if(cnt > 0) {
			// 删除user关联的所有role
			this.d.deleteUserRoleByUserId(t.getId());
			this.saveUserRole(t.getRoleIds(), t.getId());
		}
		return cnt;
	}
	
	public int updateSelect(SysUser t) {
		SysUser sysUser = this.findById(t.getId());
		Assert.notNull(sysUser, "userNotExists");

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
		List<SysUserRole> userRoleList =  new ArrayList<>();
		for (String roleId : roleIds) {
			userRoleList.add(new SysUserRole(String.valueOf(userId), roleId));
		}
		return this.d.insertUserRole(userRoleList);
	}
	
	public String encryptPwd(String password,String salt) {
		Assert.hasLength(password, "pwdIsEmpty");
		Assert.hasLength(salt,"saltIsEmpty");
		return CodecUtils.sha256(password + salt);
	}
	
	public boolean validatePwd(String password, Long userId) {
		Assert.hasLength(password, "pwdIsEmpty");
		Assert.notNull(userId, "userIdIsEmpty");
		SysUser user = this.findById(userId);
		Assert.notNull(user, "userNotExists");
		return user.getPassword().equals(CodecUtils.sha256(password + user.getSalt()));
	}
	
	public int initPwd(SysUser t, int initFlag, String ip) {
		SysUser sysUser = this.findById(t.getId());
		Assert.notNull(sysUser, "userNotExists");
		sysUser.setPassword(encryptPwd(t.getPassword(), sysUser.getSalt()));
		if(initFlag == SysConstants.SYS_USER_PWD_STATUS_CHANGED) {
			sysUser.setFldN1(1);
		}else {
            //首次登录需要修改密码
			sysUser.setFldN1(0);
		}
		int cnt = d.initPwd(sysUser);
		sysLoginLogService.loginLogByLoginName(SysConstants.SYS_USER_LOGIN_STATUS_SUCCESS, sysUser.getLoginName(), ip, "", "", "", "", initFlag);
		return cnt;
	}
	
	public int setStatus(SysUser t) {
		SysUser sysUser = this.findById(t.getId());
		Assert.notNull(sysUser, "userNotExists");
		sysUser.setStatus(t.getStatus());;
		return d.setStatus(sysUser);
	}
	
	public boolean selectFirstLoginUser(Long userId){
		SysUser sysUser = this.findById(userId);
    	if(sysUser.getFldN1() == null || sysUser.getFldN1() != 1) {
    		return true;
    	}
    	return false;
	}
}
