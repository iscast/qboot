package org.qboot.sys.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysRoleDeptDto;
import org.qboot.sys.dto.SysRoleMenuDto;
import org.qboot.common.dao.CrudDao;

/**
 * 系统角色
 * @author iscast
 * @date 2020-09-25
 */
public interface SysRoleDao extends CrudDao<SysRoleDto>{
    int deleteRoleMenuByRoleId(Serializable roleId);
    int deleteUserRoleByRoleId(Serializable roleId);
    int deleteUserRoleByRoleIdAndUserId(@Param("roleId") String roleId,@Param("userId") String userId);
    int insertUserRole(@Param("roleId") String roleId,@Param("userId") String userId);
    int insertRoleMenu(List<SysRoleMenuDto> roleMenus);
    List<SysRoleDto> findByUserId(String userId);
    int deleteRoleDeptByRoleId(Serializable roleId);
    int insertRoleDept(List<SysRoleDeptDto> list);
    
    List<String> selectMenuIdsByRoleId(Serializable roleId);
    List<String> selectDeptIdsByRoleId(String roleId);
    
    int delete(@Param("roleId") java.lang.String roleId);

	SysRoleDto findByName(String name);

}