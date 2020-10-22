package org.qboot.base.service.impl;

import org.qboot.base.dao.SysDictDao;
import org.qboot.base.dto.SysDict;
import org.qboot.common.constant.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.QRedisson;
import org.qboot.common.utils.i18n.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>Title: SysDictService</p>
 * <p>Description: system dict service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysDictService extends CrudService<SysDictDao, SysDict> {

	@Autowired
	private QRedisson qRedisson;

	public int setStatus(SysDict t) {
		SysDict sysDict = this.findById(t.getId());
		Assert.notNull(sysDict,"字典不存在");
		sysDict.setStatus(t.getStatus());
		sysDict.setUpdateBy(t.getUpdateBy());
		sysDict.setUpdateDate(t.getUpdateDate());
		return d.setStatus(sysDict);
	}

	public List<SysDict> findTypes(String type) {
        Assert.notNull(type, MessageUtil.getMessage("sys.response.msg.typeIsEmpty","type 不能为空"));
        String key = SysConstants.CACHE_PREFIX_DICT_TYPE + type;
        List<SysDict> sdlist = qRedisson.get(key);
        if(CollectionUtils.isEmpty(sdlist)) {
            sdlist = this.d.findTypes(type);
            if(!CollectionUtils.isEmpty(sdlist)) {
                qRedisson.set(key, sdlist, 2*60*60);
            }
        }
		return sdlist;
	}
}
