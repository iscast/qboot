package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.sys.service.impl.SysParamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * <p>Title: ParamTypeController</p>
 * <p>Description: 系统类型</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/paramtype")
public class ParamTypeController extends BaseController {

	@Autowired
	private SysParamTypeService sysParamService;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamTypeDto sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
            return ResponeModel.error(SYS_PARAM_TYPE_QUERY_FAIL);
		}
		PageInfo<SysParamTypeDto> page = sysParamService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryList")
	public ResponeModel qryList(SysParamTypeDto sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
			return ResponeModel.ok(SYS_PARAM_TYPE_QUERY_FAIL);
		}
		List<SysParamTypeDto> list = sysParamService.findList(sysParam);
		return ResponeModel.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysParamTypeDto sysParam = sysParamService.findById(id);
		if(null == sysParam) {
            return ResponeModel.ok(SYS_PARAM_TYPE_QUERY_FAIL);
        }
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamTypeDto sysParam, BindingResult bindingResult, HttpServletRequest request) {
		sysParam.setCreateBy(SecurityUtils.getLoginName());
		sysParam.setPhysicsFlag(1);
		if (StringUtils.isNotBlank(sysParam.getParamTypeName())) {
			sysParam.setParamTypeName(request.getParameter("paramTypeName"));
		}
		if (StringUtils.isNotBlank(sysParam.getI18nField())) {
			String i18nField = sysParam.getI18nField().replace("&quot;","\"");
			sysParam.setI18nField(i18nField);
		}
		int cnt = sysParamService.save(sysParam);
		if(cnt > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamTypeDto sysParam, BindingResult bindingResult, HttpServletRequest request) {
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
		if (StringUtils.isNotBlank(sysParam.getParamTypeName())) {
			sysParam.setParamTypeName(request.getParameter("paramTypeName"));
		}
		if (StringUtils.isNotBlank(sysParam.getI18nField())) {
			String i18nField = sysParam.getI18nField().replace("&quot;","\"");
			sysParam.setI18nField(i18nField);
		}
		if(sysParamService.update(sysParam) > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id, @RequestParam Integer phyFlag) {
        MyAssertTools.notNull(id, SYS_PARAM_TYPE_ID_NULL);
		SysParamTypeDto sysParam = new SysParamTypeDto();
		sysParam.setId(String.valueOf(id));
		sysParam.setPhysicsFlag(phyFlag);
		if(sysParamService.changeById(sysParam) > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error(SYS_PARAM_TYPE_DELETE_FAIL);
	}

	@RequestMapping("/getParamType")
	public ResponeModel getParamType(@RequestParam String paramType) {
		List<SysParamTypeDto> types = sysParamService.findParamTypes(paramType);
		if(CollectionUtils.isEmpty(types)) {
            return ResponeModel.error(SYS_PARAM_TYPE_QUERY_FAIL);
        }
		return ResponeModel.ok(types);
	}
}
