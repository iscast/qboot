package org.qboot.sys.dao;

import org.qboot.sys.dto.SysDictDto;
import org.qboot.common.dao.CrudDao;

import java.util.List;

/**
 * 系统字典
 * @author iscast
 * @date 2020-09-25
 */
public interface SysDictDao extends CrudDao<SysDictDto>{
	
    List<SysDictDto> findTypes(String type);

	int editStatus(SysDictDto sysDict);
}