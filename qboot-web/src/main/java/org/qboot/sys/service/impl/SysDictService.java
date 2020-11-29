package org.qboot.sys.service.impl;

import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysDictDao;
import org.qboot.sys.dto.SysDictDto;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.RedisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * <p>Title: SysDictService</p>
 * <p>Description: system dict service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysDictService extends CrudService<SysDictDao, SysDictDto> {

	@Autowired
	private RedisTools redisTools;

	public int setStatus(SysDictDto t) {
		SysDictDto sysDict = this.findById(t.getId());
        MyAssertTools.notNull(sysDict, SYS_DICT_NO_EXIST);
		sysDict.setStatus(t.getStatus());
		sysDict.setUpdateBy(t.getUpdateBy());
		sysDict.setUpdateDate(t.getUpdateDate());
		return d.setStatus(sysDict);
	}

	public List<SysDictDto> findTypes(String type) {
	    MyAssertTools.hasLength(type, SYS_DICT_TYPE_NULL);
        String key = CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + type;
        List<SysDictDto> sdlist = redisTools.get(key);
        if(CollectionUtils.isEmpty(sdlist)) {
            sdlist = this.d.findTypes(type);
            if(!CollectionUtils.isEmpty(sdlist)) {
                redisTools.set(key, sdlist, 2*60*60);
            }
        }
		return sdlist;
	}
}
