package org.qboot.sys.service.impl;

import org.qboot.common.constants.SysConstants;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysParamTypeDao;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.SYS_PARAM_TYPE_CLASS_NULL;

/**
 * 系统类型service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysParamTypeService extends CrudService<SysParamTypeDao, SysParamTypeDto>{

	@Autowired
    RedisTools redisTools;

	public int changeById(SysParamTypeDto sysParamType) {
		return d.changeById(sysParamType);
	}

    public SysParamTypeDto findByDto(SysParamTypeDto sysParamType) {
        return d.findByDto(sysParamType);
    }

	public List<SysParamTypeDto> findParamTypes(String paramKey) {
        MyAssertTools.hasLength(paramKey, SYS_PARAM_TYPE_CLASS_NULL);
        String key = CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramKey;
		List<SysParamTypeDto> list = redisTools.get(key);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}

		SysParamTypeDto sysParam = new SysParamTypeDto();
        sysParam.setPhysicsFlag(SysConstants.SYS_DELFLAG_NORMAL);
		sysParam.setParamTypeClass(paramKey);
		list = this.findList(sysParam);
		redisTools.set(key, list, 60*5);
		return list;
	}
}
