package org.iscast.base.dao;

import org.apache.ibatis.annotations.Param;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysTaskLog;

/**
 * 任务日志Dao
 * @author history
 *
 */
public interface SysTaskLogDao extends CrudDao<SysTaskLog> {
	
	/**
	 * 删除某任务的所有日志
	 * @param taskId
	 */
	void deleteByTaskId(@Param("taskId") Long taskId);

	
	
}