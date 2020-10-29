package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.sys.dto.SysParamType;
import org.qboot.sys.service.impl.SysParamTypeService;
import org.qboot.sys.service.impl.SysUserService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

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
	
	@Autowired
	private SysUserService sysUserService;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamType sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
			return ResponeModel.ok();
		}
		PageInfo<SysParamType> page = sysParamService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryList")
	public ResponeModel qryList(SysParamType sysParam) {
		if(StringUtils.isBlank(sysParam.getParamTypeClass())) {
			return ResponeModel.ok();
		}
		List<SysParamType> list = sysParamService.findList(sysParam);
		return ResponeModel.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysParamType sysParam = sysParamService.findById(id);
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamType sysParam, BindingResult bindingResult, HttpServletRequest request) {
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
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamType sysParam, BindingResult bindingResult, HttpServletRequest request) {
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
		if (StringUtils.isNotBlank(sysParam.getParamTypeName())) {
			sysParam.setParamTypeName(request.getParameter("paramTypeName"));
		}
		if (StringUtils.isNotBlank(sysParam.getI18nField())) {
			String i18nField = sysParam.getI18nField().replace("&quot;","\"");
			sysParam.setI18nField(i18nField);
		}
		int cnt = sysParamService.update(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id, @RequestParam Integer phyFlag) {
		Assert.notNull( id, "sys.response.msg.idIsEmpty");
		SysParamType sysParam = new SysParamType();
		sysParam.setId(String.valueOf(id));
		sysParam.setPhysicsFlag(phyFlag);
		int cnt = sysParamService.changeById(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

	@RequestMapping("/getParamType")
	public ResponeModel getParamType(@RequestParam String paramType) {
		List<SysParamType> types = sysParamService.findParamTypes(paramType);
		return ResponeModel.ok(types);
	}
}
