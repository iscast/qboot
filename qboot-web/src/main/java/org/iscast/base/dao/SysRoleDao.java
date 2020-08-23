package org.iscast.base.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysRole;
import org.iscast.base.dto.SysRoleDept;
import org.iscast.base.dto.SysRoleMenu;

/**
 * <p>Title: SysRoleDao</p>
 * <p>Description: 系统角色</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysRoleDao extends CrudDao<SysRole>{
    public int deleteRoleMenuByRoleId(Serializable roleId);
    public int deleteUserRoleByRoleId(Serializable roleId);
    public int deleteUserRoleByRoleIdAndUserId(@Param("roleId") String roleId,@Param("userId") Long userId);
    public int insertUserRole(@Param("roleId") String roleId,@Param("userId") Long userId);
    public int insertRoleMenu(List<SysRoleMenu> roleMenus);
    public List<SysRole> findByUserId(Long userId);
    public int deleteRoleDeptByRoleId(Serializable roleId);
    public int insertRoleDept(List<SysRoleDept> list);
    
    public List<String> selectMenuIdsByRoleId(Serializable roleId);
    public List<String> selectDeptIdsByRoleId(String roleId);
    
    public int delete(@Param("roleId") java.lang.String roleId);

	public SysRole findByName(String name);

}