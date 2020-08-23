package org.iscast.base.dao;

import java.io.Serializable;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysDept;

/**
 * <p>Title: SysDeptDao</p>
 * <p>Description: 部门</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysDeptDao extends CrudDao<SysDept> {
	public int deleteRoleDeptByDeptId(Serializable deptId);
}