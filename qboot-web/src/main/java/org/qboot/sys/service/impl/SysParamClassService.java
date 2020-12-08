package org.qboot.sys.service.impl;

import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysParamClassDao;
import org.qboot.sys.dto.SysParamClassDto;
import org.springframework.stereotype.Service;

/**
 * <p>Title: SysParamClassService</p>
 * <p>Description: 系统类型service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysParamClassService extends CrudService<SysParamClassDao, SysParamClassDto>{

	public int changeById(SysParamClassDto sysParamClass) {
		return d.changeById(sysParamClass);
	}
}
