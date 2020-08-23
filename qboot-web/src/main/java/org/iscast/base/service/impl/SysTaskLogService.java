package org.iscast.base.service.impl;

import org.iscast.base.dao.SysTaskLogDao;
import org.iscast.base.dto.SysTaskLog;
import org.springframework.stereotype.Service;

import org.iscast.common.service.CrudService;

/**
 * 任务日志服务层
 * @author history
 */
@Service
public class SysTaskLogService extends CrudService<SysTaskLogDao, SysTaskLog> {
	
	/**
	 * 根据taskId删除日志
	 * @param taskId
	 */
	public void deleteByTaskId(Long taskId) {
		this.d.deleteByTaskId(taskId);
	}

}
