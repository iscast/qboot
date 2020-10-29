package org.qboot.sys.dao;

import java.util.List;

import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.dto.SysUserRoleDto;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysUserDao</p>
 * <p>Description: 系统用户</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysUserDao extends CrudDao<SysUserDto> {

	public int deleteUserRoleByUserId(Long userId);
	
	public int insertUserRole(List<SysUserRoleDto> list);
	
	public List<SysUserDto> findByLoginName(String loginName);
	
	public int updateSelect(SysUserDto sysUser);

	public int initPwd(SysUserDto sysUser);
	public int setStatus(SysUserDto sysUser);
	
}