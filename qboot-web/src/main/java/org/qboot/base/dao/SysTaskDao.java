package org.qboot.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.qboot.base.dto.SysTask;
import org.qboot.common.dao.CrudDao;

/**
 * 任务Dao
 * @author history
 *
 */
public interface SysTaskDao extends CrudDao<SysTask> {
	
	/**
	 * 查询所有任务
	 * @return
	 */
	List<SysTask> findAll() ;
	
	/**
	 * 根据任务名称查询任务
	 * @param taskName
	 * @return
	 */
	SysTask findByName(@Param("taskName") String taskName);
	
	/**
	 * 更新启用状态
	 * @param sysTask
	 * @return
	 */
	int updateStatus(SysTask sysTask);
	
	/**
	 * 统计同名任务
	 * @param sysTask
	 * @return
	 */
	Long countByTaskName(SysTask sysTask);
	
	/**
	 * 更新最后一次执行成功结果
	 * @param systask
	 * @return
	 */
	int updateResult(SysTask systask);
	
	
	
}