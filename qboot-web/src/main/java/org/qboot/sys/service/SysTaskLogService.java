package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysTaskLogDao;
import org.qboot.sys.dto.SysTaskLogDto;
import org.springframework.stereotype.Service;

/**
 * 任务日志服务层
 */
public interface SysTaskLogService extends ICrudService<SysTaskLogDao, SysTaskLogDto> {
	
	/**
	 * 根据taskId删除日志
	 * @param taskId
	 */
	void deleteByTaskId(String taskId);

}
