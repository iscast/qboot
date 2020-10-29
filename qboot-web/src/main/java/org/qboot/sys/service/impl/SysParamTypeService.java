package org.qboot.sys.service.impl;

import org.qboot.sys.dao.SysParamTypeDao;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
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
public class SysParamTypeService extends CrudService<SysParamTypeDao, SysParamTypeDto>{

	@Autowired
    RedisTools redisTools;

	public SysParamTypeDto findByParamKey(String paramKey) {
		Assert.hasLength("paramKey", "paramKeyIsEmpty");
		SysParamTypeDto sysParam = new SysParamTypeDto();
		sysParam.setParamTypeClass(paramKey);
		List<SysParamTypeDto> list = this.findList(sysParam);
		return list.isEmpty()?null:list.get(0);
	}
	
	public int changeById(SysParamTypeDto sysParamType) {
		return d.changeById(sysParamType);
	}
	
	public List<SysParamTypeDto> findParamTypes(String paramKey) {
		Assert.hasLength("paramKey", "paramKeyIsEmpty");
        String key = CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramKey;
		List<SysParamTypeDto> list = redisTools.get(key);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}

		SysParamTypeDto sysParam = new SysParamTypeDto();
		sysParam.setParamTypeClass(paramKey);
		list = this.findList(sysParam);
		redisTools.set(key, list, 60*5);
		return list;
	}
}
