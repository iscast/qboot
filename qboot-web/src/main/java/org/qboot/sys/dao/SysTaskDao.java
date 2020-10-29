package org.qboot.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import org.qboot.sys.dto.SysTaskDto;
import org.qboot.common.dao.CrudDao;

/**
 * 任务Dao
 * @author history
 *
 */
public interface SysTaskDao extends CrudDao<SysTaskDto> {
	
	/**
	 * 查询所有任务
	 * @return
	 */
	List<SysTaskDto> findAll() ;
	
	/**
	 * 根据任务名称查询任务
	 * @param taskName
	 * @return
	 */
	SysTaskDto findByName(@Param("taskName") String taskName);
	
	/**
	 * 更新启用状态
	 * @param sysTask
	 * @return
	 */
	int updateStatus(SysTaskDto sysTask);
	
	/**
	 * 统计同名任务
	 * @param sysTask
	 * @return
	 */
	Long countByTaskName(SysTaskDto sysTask);
	
	/**
	 * 更新最后一次执行成功结果
	 * @param systask
	 * @return
	 */
	int updateResult(SysTaskDto systask);
	
	
	
}