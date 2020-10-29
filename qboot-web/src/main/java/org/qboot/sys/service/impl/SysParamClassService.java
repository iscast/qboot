package org.qboot.sys.service.impl;

import java.util.List;

import org.qboot.sys.dao.SysParamClassDao;
import org.qboot.sys.dto.SysParamClassDto;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.qboot.common.service.CrudService;

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
	
	/**
	 * class唯一判断
	 * @param sysParamClass
	 * @return
	 */
	public boolean classIsUsed(SysParamClassDto sysParamClass) {
		List<SysParamClassDto> list = d.classIsUsed(sysParamClass);
		if(!CollectionUtils.isEmpty(list)) {
			SysParamClassDto spc = list.get(0);
			if(sysParamClass.getId() != null && spc.getId() != sysParamClass.getId()) {
				return true;
			}
			if(sysParamClass.getId() == null) {
				return true;
			}
		}

		return false;
	}
}
