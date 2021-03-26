package org.qboot.sys.controller;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.TreeHelper;
import org.qboot.sys.dto.SysDeptDto;
import org.qboot.sys.service.impl.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@Controller
@RequestMapping("${admin.path}/sys/dept")
public class SysDeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;
	private TreeHelper<SysDeptDto> treeHelper = new TreeHelper();

	/**
	 * 用户管理那里的部门查询,只能看到自己及以下部门
	 * @param sysDept
	 * @return
	 */
	@PostMapping("/qryAllWithScope")
	public ResponeModel qryAllWithScope(SysDeptDto sysDept) {
		CustomUser user = SecurityUtils.getUser();
		String deptId = user.getDeptId();
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return this.qryAll(sysDept);
		}
		//如果是非超级管理员返回自己部门及以下
		SysDeptDto currentDept = sysDeptService.findById(deptId);
		Assert.notNull(currentDept, "当前所属部门为空");
		List<SysDeptDto> list = sysDeptService.findByParentIds(currentDept.getParentIds() + deptId);
		list.add(currentDept);
		return ResponeModel.ok(list);
	}


    @PreAuthorize("hasAuthority('sys:dept:qry')")
    @GetMapping({"/qryAll"})
    public ResponeModel qryAll(SysDeptDto sysDept) {
        List<SysDeptDto> list = this.sysDeptService.findList(sysDept);
        return ResponeModel.ok(this.treeHelper.treeGridList(list));
    }

    /**
     * 方法表述: 查询部门
     * @Author zoujiulong
     * @Date 10:54 2019/9/18
     * @param       parentId
     * @return cn.swiftpass.rdp.web.dto.ResponeModel
     */
    @PreAuthorize("hasAuthority('sys:dept:qry')")
    @GetMapping({"/qryDepts"})
    public ResponeModel qryParentMenus(@RequestParam(required = false) String parentId) {
        List<SysDeptDto> list = this.sysDeptService.findDeptByParentId(parentId);
        return ResponeModel.ok(list);
    }

    /**
     * 查询所有部门
     * @param dto
     * @return
     */
    @PreAuthorize("hasAuthority('sys:dept:qry')")
    @GetMapping({"/queryAllDept"})
    public ResponeModel queryAllDept(SysDeptDto dto) {
        List<SysDeptDto> list = sysDeptService.relationQueryDept(dto);
        Map<String,Object> map = new HashMap<>();
        //查询所有部门信息，id ： name 存入map
        list.forEach(i -> {
            map.put(i.getId(),i.getName());
        });
        return ResponeModel.ok(map);
    }

    @PreAuthorize("hasAuthority('sys:dept:qry')")
    @RequestMapping({"/get"})
    public ResponeModel get(@RequestParam Serializable id) {
        SysDeptDto sysDept = (SysDeptDto)this.sysDeptService.findById(id);
        if (sysDept != null) {
            return ResponeModel.ok(sysDept);
        } else {
            return ResponeModel.error("");
        }
    }

    @AccLog
    @PreAuthorize("hasAuthority('sys:dept:save')")
    @PostMapping({"/save"})
    @Transactional(rollbackFor = Exception.class)
    public ResponeModel save(@Validated SysDeptDto sysDept) {
        try {
            int count = sysDeptService.checkNameUnique(sysDept);
            if(count > 0 ){
                return ResponeModel.error("部门名称重复");
            }
            SysDeptDto parent = null;
            if (StringUtils.isNotEmpty(sysDept.getParentId())) {
                parent = (SysDeptDto)this.sysDeptService.findById(sysDept.getParentId());
            }
            if (StringUtils.isEmpty(sysDept.getParentId())) {
                sysDept.setParentId("0");
                sysDept.setParentIds("0");
            } else if (parent != null && parent.getParentId().equals("0")) {
                sysDept.setParentId((String)parent.getId());
                sysDept.setParentIds("1");
            } else if (parent != null && parent.getParentIds().contains("1")) {
                sysDept.setParentId((String)parent.getId());
                sysDept.setParentIds("2");
            }
            sysDept.setCreateBy(SecurityUtils.getLoginName());
            sysDept.setUpdateBy(SecurityUtils.getLoginName());
            int cnt = this.sysDeptService.save(sysDept);
            if (cnt > 0) {
                return ResponeModel.ok();
            } else {
                return ResponeModel.error("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponeModel.error("");
        }
    }

    @AccLog
    @PreAuthorize("hasAuthority('sys:dept:update')")
    @PostMapping({"/update"})
    @Transactional(rollbackFor = Exception.class)
    public ResponeModel update(@Validated SysDeptDto sysDept) {
        try {
            if (sysDept.getId().equals(sysDept.getParentId())) {
                return ResponeModel.error("父级部门不能选择自己");
            }
            int count = sysDeptService.checkNameUnique(sysDept);
            if(count > 0 ){
                return ResponeModel.error("部门名称重复");
            }
            sysDept.setUpdateBy(SecurityUtils.getLoginName());
            if (StringUtils.isBlank(sysDept.getParentId())) {
                sysDept.setParentId("0");
                sysDept.setParentIds("0");
            } else {
                SysDeptDto sysDeptDto = sysDeptService.findById(sysDept.getParentId());
                if (null != sysDeptDto) {
                    if (sysDeptDto.getParentIds().equals("0")) {
                        sysDept.setParentIds("1");
                    } else if (sysDeptDto.getParentIds().equals("1")) {
                        sysDept.setParentIds("2");
                    }
                }
            }
            int cnt = this.sysDeptService.update(sysDept);
            if (cnt > 0) {
                return ResponeModel.ok();
            } else {
                return ResponeModel.error("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponeModel.error("");
        }
    }


    @AccLog
	@PreAuthorize("hasAuthority('sys:dept:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysDeptService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
}
