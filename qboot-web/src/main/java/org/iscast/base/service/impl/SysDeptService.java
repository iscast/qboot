package org.iscast.base.service.impl;

import java.io.Serializable;
import java.util.List;

import org.iscast.base.dao.SysDeptDao;
import org.iscast.base.dto.SysDept;
import org.iscast.common.constant.QConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.iscast.common.service.CrudService;

/**
 * <p>Title: SysDeptService</p>
 * <p>Description: 部门service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysDeptService extends CrudService<SysDeptDao, SysDept>{

	public List<SysDept> findByParentIds(String parentId){
		//Assert.hasLength(parentIds,"parentIds 不能为空");
		SysDept sysDept = new SysDept();
		sysDept.setSortField("sort");
		sysDept.setDirection(QConstants.ASC);
		sysDept.setParentId(parentId==null?"0":parentId);
		return this.findList(sysDept);
	}
	
	@Override
	public int deleteById(Serializable id) {
		//删除下级部门
		SysDept sysDept = this.findById(id);
		Assert.notNull(sysDept,"部门不能为空");
		List<SysDept> list = this.findByParentIds(sysDept.getParentIds() + sysDept.getId());
		for (SysDept sDept : list) {
			super.deleteById(sDept.getId());
			this.d.deleteRoleDeptByDeptId(sDept.getId());
		}
		this.d.deleteRoleDeptByDeptId(id);
		return super.deleteById(id);
	}
}
