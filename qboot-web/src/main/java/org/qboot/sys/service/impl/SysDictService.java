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

import java.io.Serializable;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * system dict service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysDictService extends CrudService<SysDictDao, SysDictDto> {

	@Autowired
	private RedisTools redisTools;

    @Override
    public int deleteById(Serializable id) {
        SysDictDto sysDict = this.findById(id);
        MyAssertTools.notNull(sysDict, SYS_DICT_NO_EXIST);
        int isDelete = super.deleteById(id);
        if(isDelete > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + sysDict.getType());
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE_SINGLE + sysDict.getType());
        }
        return isDelete;
    }

	public int editStatus(SysDictDto t) {
		SysDictDto sysDict = this.findById(t.getId());
        MyAssertTools.notNull(sysDict, SYS_DICT_NO_EXIST);
		sysDict.setStatus(t.getStatus());
		sysDict.setUpdateBy(t.getUpdateBy());
		sysDict.setUpdateDate(t.getUpdateDate());
		int isUpdate = d.editStatus(sysDict);
        if(isUpdate > 0) {
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + sysDict.getType());
            redisTools.del(CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE_SINGLE + sysDict.getType());
        }
        return isUpdate;
	}

	public List<SysDictDto> findTypes(String type) {
	    MyAssertTools.hasLength(type, SYS_DICT_TYPE_NULL);
        String key = CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE + type;
        List<SysDictDto> sdlist = redisTools.get(key);
        if(CollectionUtils.isEmpty(sdlist)) {
            sdlist = this.d.findTypes(type);
            if(!CollectionUtils.isEmpty(sdlist)) {
                redisTools.set(key, sdlist, 60*60);
            }
        }
		return sdlist;
	}

	public SysDictDto findType(String type) {
	    MyAssertTools.hasLength(type, SYS_DICT_TYPE_NULL);
        String key = CacheConstants.CACHE_PREFIX_SYS_DICT_TYPE_SINGLE + type;
        SysDictDto target = redisTools.get(key);
        if(null == target) {
            List<SysDictDto> sdlist = this.d.findTypes(type);
            if(!CollectionUtils.isEmpty(sdlist)) {
                target = sdlist.get(0);
            }
            redisTools.set(key, target, 60*60);
        }
		return target;
	}
}
