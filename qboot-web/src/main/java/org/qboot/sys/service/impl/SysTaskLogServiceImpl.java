package org.qboot.sys.service.impl;

import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysTaskLogDao;
import org.qboot.sys.dto.SysTaskLogDto;
import org.qboot.sys.service.SysTaskLogService;
import org.springframework.stereotype.Service;

/**
 * 任务日志服务层
 * @author history
 */
@Service
public class SysTaskLogServiceImpl extends CrudService<SysTaskLogDao, SysTaskLogDto> implements SysTaskLogService {
	
	/**
	 * 根据taskId删除日志
	 * @param taskId
	 */
    @Override
	public void deleteByTaskId(String taskId) {
		this.d.deleteByTaskId(taskId);
	}
}
