package org.qboot.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.IdGen;
import org.qboot.sys.dao.SysDeptDao;
import org.qboot.sys.dto.SysDeptDto;
import org.qboot.sys.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * 部门service
 * @author iscast
 * @date 2020-09-25
 */
@Service
public class SysDeptServiceImpl extends CrudService<SysDeptDao, SysDeptDto> implements SysDeptService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(SysDeptDto sysDept) {
        SysDeptDto parent = null;
        if (StringUtils.isNotEmpty(sysDept.getParentId())) {
            parent = this.d.findById(sysDept.getParentId());
        }
        if (StringUtils.isEmpty(sysDept.getParentId()) || parent == null) {
            sysDept.setParentId("0");
            sysDept.setParentIds("0");
        } else {
            sysDept.setParentId(parent.getId());
            sysDept.setParentIds(parent.getParentIds() + SysConstants.SEPARATOR + parent.getId());
        }
        sysDept.setId(IdGen.uuid());
        if(StringUtils.isBlank(sysDept.getCreateBy())) {
            sysDept.setCreateBy(SecurityUtils.getLoginName());
        }
        if(StringUtils.isBlank(sysDept.getUpdateBy())) {
            sysDept.setUpdateBy(SecurityUtils.getLoginName());
        }
        return super.save(sysDept);
    }


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
            logger.error("dept getParentName ", e);
        }
        return parentName;
    }

    @Override
    public List<SysDeptDto> relationQueryDept(SysDeptDto dto) {
        List<SysDeptDto> list = d.relationQueryDept(dto);
        return list;
    }
}