package org.qboot.base.dao;

import java.io.Serializable;

import org.qboot.common.dao.CrudDao;
import org.qboot.base.dto.SysDept;

/**
 * <p>Title: SysDeptDao</p>
 * <p>Description: 部门</p>
 * @author history
 * @date 2018-08-08
 */
public interface SysDeptDao extends CrudDao<SysDept> {
	int deleteRoleDeptByDeptId(Serializable deptId);
}