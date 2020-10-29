package org.qboot.sys.service.impl;

import org.qboot.sys.dao.SysTaskLogDao;
import org.qboot.sys.dto.SysTaskLog;
import org.springframework.stereotype.Service;

import org.qboot.common.service.CrudService;

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
