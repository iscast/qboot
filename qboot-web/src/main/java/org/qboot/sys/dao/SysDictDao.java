package org.qboot.sys.dao;

import org.qboot.sys.dto.SysDictDto;
import org.qboot.common.dao.CrudDao;

import java.util.List;

/**
 * <p>Title: SysDictDao</p>
 * <p>Description: 字段参数</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysDictDao extends CrudDao<SysDictDto>{
	
    List<SysDictDto> findTypes(String type);

	int setStatus(SysDictDto sysDict);
}