package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysDictDao;
import org.qboot.sys.dto.SysDictDto;

import java.io.Serializable;
import java.util.List;

/**
 * 系统字典
 */
public interface SysDictService extends ICrudService<SysDictDao, SysDictDto> {

    int deleteById(Serializable id);

	int editStatus(SysDictDto t);

	List<SysDictDto> findTypes(String type);

	SysDictDto findType(String type);
}
