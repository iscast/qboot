package org.iscast.base.service.impl;

import java.util.List;

import org.iscast.base.dao.SysOperateLogDao;
import org.iscast.base.dto.SysOperateLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.iscast.common.service.CrudService;

/**
 * <p>Title: SysOperateLogService</p>
 * <p>Description: 操作日志service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysOperateLogService extends CrudService<SysOperateLogDao, SysOperateLogDto>{

	private static Logger logger = LoggerFactory.getLogger(SysOperateLogService.class);

	@Transactional
	public void insertOperationLog(SysOperateLogDto operationLogDto) {
		this.d.insert(operationLogDto);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void batchInsertOperationLog(List<SysOperateLogDto> operationLogList) {
		if(CollectionUtils.isEmpty(operationLogList)) {
			return ;
		}
		this.d.batchInsert(operationLogList);
	}
	
}
