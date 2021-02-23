package org.qboot.sys.dao;

import org.apache.ibatis.annotations.Param;

import org.qboot.sys.dto.SysTaskLogDto;
import org.qboot.common.dao.CrudDao;

/**
 * 任务日志Dao
 * @author history
 *
 */
public interface SysTaskLogDao extends CrudDao<SysTaskLogDto> {
	
	/**
	 * 删除某任务的所有日志
	 * @param taskId
	 */
	void deleteByTaskId(@Param("taskId") String taskId);
}