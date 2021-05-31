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

	int deleteUserRoleByUserId(Long userId);
	
	int insertUserRole(List<SysUserRoleDto> list);
	
	int updateSelect(SysUserDto sysUser);

	int initPwd(SysUserDto sysUser);
	int setStatus(SysUserDto sysUser);

    SysUserDto findSecretInfo(SysUserDto qry);

    SysUserDto findByDto(SysUserDto qry);

    List<Long> findUserIds(SysUserDto qry);
}