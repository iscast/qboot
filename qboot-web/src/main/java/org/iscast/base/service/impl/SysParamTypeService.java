package org.iscast.base.service.impl;

import org.iscast.base.dao.SysParamTypeDao;
import org.iscast.base.dto.SysParamType;
import org.iscast.common.service.CrudService;
import org.iscast.common.utils.QRedisson;
import org.iscast.common.utils.i18n.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>Title: SysParamTypeService</p>
 * <p>Description: 系统类型service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysParamTypeService extends CrudService<SysParamTypeDao, SysParamType>{

	@Autowired
	QRedisson qRedisson;

	private final String PARAM_KEY_PREFIX = "PARAM_KEY_";

	public SysParamType findByParamKey(String paramKey) {
		Assert.hasLength("paramKey", MessageUtil.getMessage("sys.response.msg.paramKeyIsEmpty","paramKey不能为空"));
		SysParamType sysParam = new SysParamType();
		sysParam.setParamTypeClass(paramKey);
		List<SysParamType> list = this.findList(sysParam);
		return list.isEmpty()?null:list.get(0);
	}
	
	public int changeById(SysParamType sysParamType) {
		return d.changeById(sysParamType);
	}
	
	public List<SysParamType> findParamTypes(String paramKey) {
		Assert.hasLength("paramKey",MessageUtil.getMessage("sys.response.msg.paramKeyIsEmpty","paramKey不能为空"));
		List<SysParamType> list = qRedisson.get(PARAM_KEY_PREFIX+paramKey);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}

		SysParamType sysParam = new SysParamType();
		sysParam.setParamTypeClass(paramKey);
		list = this.findList(sysParam);
		qRedisson.set(PARAM_KEY_PREFIX+paramKey,list,60*5);
		return list;
	}
}
