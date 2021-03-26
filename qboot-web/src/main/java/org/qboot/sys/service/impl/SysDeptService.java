package org.qboot.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysDeptDao;
import org.qboot.sys.dto.SysDeptDto;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * 部门service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysDeptService extends CrudService<SysDeptDao, SysDeptDto> {

    public List<SysDeptDto> findDeptByParentId(String parentId){
        SysDeptDto sysMenu = new SysDeptDto();
        if(StringUtils.isNotBlank(parentId)) {
            sysMenu.setParentId(parentId);
        }else {
            sysMenu.setParentId("0");
        }
        List<SysDeptDto> list = this.findList(sysMenu);
        return list;
    }

	public List<SysDeptDto> findByParentIds(String parentId){
		//Assert.hasLength(parentIds,"parentIds 不能为空");
		SysDeptDto sysDept = new SysDeptDto();
		sysDept.setParentId(parentId==null?"0":parentId);
		return this.findList(sysDept);
	}
	
	
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

    
    public int checkNameUnique(SysDeptDto deptDto) {
        return d.checkNameUnique(deptDto);
    }

    public String getDeptIds(String id) {
        return d.getDeptIds(id);
    }

    /**
     * 方法表述: 获取受理部门上级部门名称
     */
    public String getParentName(String deptNo) {
        String parentName = "";
        if (StringUtils.isBlank(deptNo)) {
            return parentName;
        }
        try {
            SysDeptDto deptDto = this.d.findById(deptNo);
            if (null != deptDto) {
                SysDeptDto dto = this.d.findById(deptDto.getParentId());
                if (null != dto) {
                    parentName = dto.getName();
                } else {
                    parentName = "";
                }
            }
        } catch (Exception e) {
            parentName = "";
            e.printStackTrace();
        }
        return parentName;
    }

    public List<SysDeptDto> relationQueryDept(SysDeptDto dto) {
        List<SysDeptDto> list = d.relationQueryDept(dto);
        list.forEach(i -> {
            SysDeptDto deptDto = d.findById(i.getParentId());
            if (null != deptDto) {
                i.setParentName(deptDto.getName());
            }
        });
        return list;
    }
}
