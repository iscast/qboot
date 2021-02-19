package org.qboot.sys.service;

import com.github.pagehelper.PageInfo;
import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysTaskDao;
import org.qboot.sys.dto.SysTaskDto;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 系统任务
 */
public interface SysTaskService extends ICrudService<SysTaskDao, SysTaskDto> {

	PageInfo<SysTaskDto> findByPage(SysTaskDto sysTask);

	/**
	 * 查询所有任务
	 */
	List<SysTaskDto> findAll();
	
	/**
	 * 根据任务名称查询任务
	 */
	SysTaskDto findByName(String taskName);
	
	/**
	 * 更新启用状态
	 */
	int updateStatus(SysTaskDto task);

	int save(SysTaskDto task);

	int updateById(SysTaskDto task);

	/**
	 * 立即触发一次执行
	 */
	void runOnce(Long taskId);
	
	/**
	 * 初始化任务，将任务添加到schedule
	 */
	void initTask();

	/**
	 * 从调度中心删除任务
	 * @param task
	 * @throws SchedulerException
	 */
	void deleteScheduleJob(SysTaskDto task) throws SchedulerException;
	
	/**
	 * 暂停任务
	 * @param task
	 * @throws SchedulerException
	 */
	void pauseScheduleJob(SysTaskDto task) throws SchedulerException;
	
	/**
	 * 重启任务
	 * @param task
	 * @throws SchedulerException
	 */
	void resumeScheduleJob(SysTaskDto task) throws SchedulerException;
	
	/**
	 * 立即执行
	 * @param task
	 * @throws SchedulerException
	 */
	void runOnce(SysTaskDto task) throws SchedulerException;

	/**
	 * 删除任务时清理所有任务日志
	 * @param taskId
	 */
	int deleteTask(Long taskId);

	Long countByTaskName(SysTaskDto sysTask);

	/**
	 * 使任务信息更新部分生效到任务调度器 主要是时间表达式变更,这项变更必须重置任务调度器内对应的任务
	 *  注意,此处只更新有变更的任务
	 */
	void reloadTask();

	/**
	 * 更新上一次成功执行结果
	 * @param systask
	 */
	int updateResult(SysTaskDto systask);
}
