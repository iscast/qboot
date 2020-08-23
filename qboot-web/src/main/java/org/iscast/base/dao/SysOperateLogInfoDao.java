package org.iscast.base.dao;

import java.util.List;
import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysOperateLogInfoDto;

/**
 * <p>Title: SysOperateLogInfoDao</p>
 * <p>Description: 操作日志管理</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysOperateLogInfoDao extends CrudDao<SysOperateLogInfoDto> {
	
	/**
	 * 通过Uri请求日志定义
	 * @param uri
	 * @return
	 */
	List<SysOperateLogInfoDto> findByUri(SysOperateLogInfoDto operationLogInfoDto);

}