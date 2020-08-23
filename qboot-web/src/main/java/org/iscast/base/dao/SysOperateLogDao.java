package org.iscast.base.dao;

import java.util.List;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysOperateLogDto;

/**
 * <p>Title: SysOperateLogDao</p>
 * <p>Description: 操作日志</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysOperateLogDao extends CrudDao<SysOperateLogDto> {
	
	/**
	 * 日志插入
	 * @param operationLogList
	 */
	int insert(SysOperateLogDto operationLog);
	
	/**
	 * 日志批量插入
	 * @param operationLogList
	 */
	int batchInsert(List<SysOperateLogDto> operationLogList);
	
	/**
	 * 数据明细
	 * @param operationLogDto
	 * @return
	 */
	List<SysOperateLogDto> pagingQuery(SysOperateLogDto operationLogDto);
	
	/**
	 * 单个详情
	 * @param List<OperationLogDto>
	 * @return
	 */
	SysOperateLogDto queryPageDetail(SysOperateLogDto operationLogDto);
	
}