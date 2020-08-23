package org.iscast.base.service.impl;

import java.util.List;

import org.iscast.base.dao.SysParamClassDao;
import org.iscast.base.dto.SysParamClass;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.iscast.common.service.CrudService;

/**
 * <p>Title: SysParamClassService</p>
 * <p>Description: 系统类型service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysParamClassService extends CrudService<SysParamClassDao, SysParamClass>{

	public int changeById(SysParamClass sysParamClass) {
		return d.changeById(sysParamClass);
	}
	
	/**
	 * class唯一判断
	 * @param sysParamClass
	 * @return
	 */
	public boolean classIsUsed(SysParamClass sysParamClass) {
		List<SysParamClass> list = d.classIsUsed(sysParamClass);
		if(!CollectionUtils.isEmpty(list)) {
			SysParamClass spc = list.get(0);
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
