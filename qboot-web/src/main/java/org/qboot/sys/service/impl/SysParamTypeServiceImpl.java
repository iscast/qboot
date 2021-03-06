package org.qboot.sys.service.impl;

import org.qboot.common.constants.SysConstants;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysParamTypeDao;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
import org.qboot.sys.service.SysParamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.SYS_PARAM_TYPE_CLASS_NULL;

/**
 * 系统类型
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysParamTypeServiceImpl extends CrudService<SysParamTypeDao, SysParamTypeDto> implements SysParamTypeService {

	@Autowired
    RedisTools redisTools;

	@Override
    public SysParamTypeDto findByDto(SysParamTypeDto sysParamType) {
        return d.findByDto(sysParamType);
    }

    @Override
	public List<SysParamTypeDto> findParamTypes(String paramTypeClass) {
        MyAssertTools.hasLength(paramTypeClass, SYS_PARAM_TYPE_CLASS_NULL);
        String key = CacheConstants.CACHE_PREFIX_SYS_PARAMTYPE_KEY + paramTypeClass;
		List<SysParamTypeDto> list = redisTools.get(key);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}

		SysParamTypeDto sysParam = new SysParamTypeDto();
        sysParam.setPhysicsFlag(SysConstants.SYS_DELFLAG_NORMAL);
		sysParam.setParamTypeClass(paramTypeClass);
		list = this.findList(sysParam);
		redisTools.set(key, list, 300);
		return list;
	}
}
