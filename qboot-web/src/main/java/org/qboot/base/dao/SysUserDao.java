package org.qboot.base.dao;

import java.util.List;

import org.qboot.base.dto.SysUser;
import org.qboot.base.dto.SysUserRole;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysUserDao</p>
 * <p>Description: 系统用户</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysUserDao extends CrudDao<SysUser> {

	public int deleteUserRoleByUserId(Long userId);
	
	public int insertUserRole(List<SysUserRole> list);
	
	public List<SysUser> findByLoginName(String loginName);
	
	public int updateSelect(SysUser sysUser);

	public int initPwd(SysUser sysUser);
	public int setStatus(SysUser sysUser);
	
}