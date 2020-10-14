package org.qboot.base.service.impl;

import org.qboot.base.dao.SysDictDao;
import org.qboot.base.dto.SysDict;
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
 * <p>Description: 系统字典参数service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysDictService extends CrudService<SysDictDao, SysDict> {

	@Autowired
	private QRedisson qRedisson;

	public List<SysDict> findByType(String type) {
		Assert.hasLength(type, MessageUtil.getMessage("sys.response.msg.typeIsEmpty","type 不能为空"));
		SysDict sysDict = new SysDict();
		sysDict.setType(type);
		
		List<SysDict> sdlist = qRedisson.get(type);
		if(CollectionUtils.isEmpty(sdlist)) {
			sdlist = this.findList(sysDict);
			if(!CollectionUtils.isEmpty(sdlist)) {
				qRedisson.set(type, sdlist, 2*60*60);
			}
		}
		return sdlist;
	}

	public int setStatus(SysDict t) {
		SysDict sysDict = this.findById(t.getId());
		Assert.notNull(sysDict,"字典不存在");
		sysDict.setStatus(t.getStatus());
		sysDict.setUpdateBy(t.getUpdateBy());
		sysDict.setUpdateDate(t.getUpdateDate());
		return d.setStatus(sysDict);
	}

	public List<SysDict> findTypes(String type) {
		return this.d.findTypes(type);
	}
}
