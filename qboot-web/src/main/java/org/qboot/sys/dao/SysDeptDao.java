package org.qboot.sys.dao;

import java.io.Serializable;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysDeptDto;

/**
 * 部门
 * @author iscast
 * @date 2020-09-25
 */
public interface SysDeptDao extends CrudDao<SysDeptDto> {
	int deleteRoleDeptByDeptId(Serializable deptId);
}