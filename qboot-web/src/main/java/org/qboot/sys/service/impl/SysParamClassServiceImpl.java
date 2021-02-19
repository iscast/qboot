package org.qboot.sys.service.impl;

import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysParamClassDao;
import org.qboot.sys.dto.SysParamClassDto;
import org.qboot.sys.service.SysParamClassService;
import org.springframework.stereotype.Service;

/**
 * 系统类型service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysParamClassServiceImpl extends CrudService<SysParamClassDao, SysParamClassDto> implements SysParamClassService {
    @Override
	public int changeById(SysParamClassDto sysParamClass) {
		return d.changeById(sysParamClass);
	}
}
