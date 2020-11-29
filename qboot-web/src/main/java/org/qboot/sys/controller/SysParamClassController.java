package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dto.SysParamClassDto;
import org.qboot.sys.service.impl.SysParamClassService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * <p>Title: SysParamClassController</p>
 * <p>Description: 系统类型</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/paramclass")
public class SysParamClassController extends BaseController {

	@Autowired
	private SysParamClassService sysParamClassService;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamClassDto sysParam) {
		PageInfo<SysParamClassDto> page = sysParamClassService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysParamClassDto sysParam = sysParamClassService.findById(id);
		if(null == sysParam) {
            return ResponeModel.error(SYS_PARAM_CLASS_QUERY_FAIL);
        }
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamClassDto sysParam, BindingResult bindingResult) {
		sysParam.setCreateBy(SecurityUtils.getLoginName());
		sysParam.setVisible(1); // 默认可用
		sysParam.setPhysicsFlag(1);
		int cnt = sysParamClassService.save(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamClassDto sysParam, BindingResult bindingResult) {
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
		int cnt = sysParamClassService.update(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id, @RequestParam Integer phyFlag) {
        MyAssertTools.notNull(id, SYS_PARAM_CLASS_ID_NULL);
		SysParamClassDto sysParam = new SysParamClassDto();
		sysParam.setId(String.valueOf(id));
		sysParam.setPhysicsFlag(phyFlag);
		int cnt = sysParamClassService.changeById(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_DELETE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/visible")
	public ResponeModel visible(@RequestParam Serializable id, @RequestParam Integer visible) {
        MyAssertTools.notNull(id, SYS_PARAM_CLASS_ID_NULL);
		SysParamClassDto sysParam = new SysParamClassDto();
		sysParam.setId(String.valueOf(id));
		sysParam.setVisible(visible);
		int cnt = sysParamClassService.changeById(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_CLASS_UPDATE_FAIL);
	}

}
