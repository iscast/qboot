package org.qboot.sys.dao;

import java.util.List;

import org.qboot.sys.dto.SysOperateLogInfoDto;
import org.qboot.common.dao.CrudDao;

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