package org.iscast.base.dao;

import java.util.List;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysUser;
import org.iscast.base.dto.SysUserRole;

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