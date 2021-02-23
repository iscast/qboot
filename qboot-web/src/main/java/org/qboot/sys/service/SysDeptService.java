package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysDeptDao;
import org.qboot.sys.dto.SysDeptDto;

import java.io.Serializable;
import java.util.List;


/**
 * 部门service
 */
public interface SysDeptService extends ICrudService<SysDeptDao, SysDeptDto> {

	List<SysDeptDto> findByParentIds(String parentId);
	
	int deleteById(Serializable id);
}
