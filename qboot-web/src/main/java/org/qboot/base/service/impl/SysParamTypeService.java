package org.qboot.base.service.impl;

import org.qboot.base.dao.SysParamTypeDao;
import org.qboot.base.dto.SysParamType;
import org.qboot.common.constant.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
import org.qboot.common.utils.i18n.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>Title: SysParamTypeService</p>
 * <p>Description: 系统类型service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysParamTypeService extends CrudService<SysParamTypeDao, SysParamType>{

	@Autowired
    RedisTools redisTools;

	public SysParamType findByParamKey(String paramKey) {
		Assert.hasLength("paramKey", "paramKeyIsEmpty");
		SysParamType sysParam = new SysParamType();
		sysParam.setParamTypeClass(paramKey);
		List<SysParamType> list = this.findList(sysParam);
		return list.isEmpty()?null:list.get(0);
	}
	
	public int changeById(SysParamType sysParamType) {
		return d.changeById(sysParamType);
	}
	
	public List<SysParamType> findParamTypes(String paramKey) {
		Assert.hasLength("paramKey", "paramKeyIsEmpty");
        String key = SysConstants.CACHE_PREFIX_PARAMTYPE_KEY + paramKey;
		List<SysParamType> list = redisTools.get(key);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}

		SysParamType sysParam = new SysParamType();
		sysParam.setParamTypeClass(paramKey);
		list = this.findList(sysParam);
		redisTools.set(key, list, 60*5);
		return list;
	}
}
