package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysRoleDao;
import org.qboot.sys.dto.SysRoleDto;

import java.io.Serializable;
import java.util.List;

/**
 * 系统角色service
 */
public interface SysRoleService extends ICrudService<SysRoleDao, SysRoleDto> {

	int save(SysRoleDto t);

	int deleteById(Serializable id);

	int update(SysRoleDto t);

	List<SysRoleDto> findByUserId(Long userId);
	
	List<String> findDeptIdsByRoleId(String roleId);

	int removeUsersByRoleId(String roleId, List<Long> userIds);
	
	int addUsersByRoleId(String roleId, List<Long> userIds);

	List<String> selectMenuIdsByRoleId(Serializable roleId);

	SysRoleDto findByName(String name);

}
