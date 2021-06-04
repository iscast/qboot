package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.dto.SysUserRoleDto;

import java.util.List;

/**
 * 系统用户
 * @author iscast
 * @date 2020-09-25
 */
public interface SysUserDao extends CrudDao<SysUserDto> {

	int deleteUserRoleByUserId(String userId);
	int insertUserRole(List<SysUserRoleDto> list);
	List<SysUserDto> findByLoginName(String loginName);
	int changePwd(SysUserDto sysUser);
	int setStatus(SysUserDto sysUser);
    SysUserDto findSecretInfo(SysUserDto qry);
}