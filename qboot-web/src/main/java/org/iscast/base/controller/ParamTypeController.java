package org.iscast.base.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.iscast.common.controller.BaseController;
import org.iscast.web.dto.ResponeModel;
import org.iscast.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.iscast.base.dto.SysParamType;
import org.iscast.base.service.impl.SysParamTypeService;
import org.iscast.base.service.impl.SysUserService;

import com.github.pagehelper.PageInfo;

/**
 * <p>Title: ParamTypeController</p>
 * <p>Description: 系统类型</p>
 * 
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
	
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamType sysParam, BindingResult bindingResult, HttpServletRequest request) {
		sysParam.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
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
	
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamType sysParam, BindingResult bindingResult, HttpServletRequest request) {
		sysParam.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
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
		List<SysParamType> types = new ArrayList<>();
		if("SYS_USER_TYPE".equalsIgnoreCase(paramType)) {
			SysParamType sysParamType = new SysParamType();
			sysParamType.setParamTypeId(Integer.valueOf(sysUserService.findById(SecurityUtils.getUserId()).getUserType()));
			sysParamType.setParamTypeClass(paramType);
			types = sysParamService.findList(sysParamType);
		}else {
			types = sysParamService.findParamTypes(paramType);
		}
		return ResponeModel.ok(types);
	}
}
