package org.qboot.sys.service.impl;

import java.io.Serializable;
import java.util.List;

import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysDeptDao;
import org.qboot.sys.dto.SysDeptDto;
import org.qboot.common.constants.SysConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * 部门service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysDeptService extends CrudService<SysDeptDao, SysDeptDto> {

	public List<SysDeptDto> findByParentIds(String parentId){
		//Assert.hasLength(parentIds,"parentIds 不能为空");
		SysDeptDto sysDept = new SysDeptDto();
		sysDept.setParentId(parentId==null?"0":parentId);
		return this.findList(sysDept);
	}
	
	@Override
	public int deleteById(Serializable id) {
		//删除下级部门
		SysDeptDto sysDept = this.findById(id);
		Assert.notNull(sysDept,"部门不能为空");
		List<SysDeptDto> list = this.findByParentIds(sysDept.getParentIds() + sysDept.getId());
		for (SysDeptDto sDept : list) {
			super.deleteById(sDept.getId());
			this.d.deleteRoleDeptByDeptId(sDept.getId());
		}
		this.d.deleteRoleDeptByDeptId(id);
		return super.deleteById(id);
	}
}
