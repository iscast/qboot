package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysParamClass;
import org.qboot.base.service.impl.SysParamClassService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
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

import java.io.Serializable;

/**
 * <p>Title: ParamClassController</p>
 * <p>Description: 系统类型</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/paramclass")
public class ParamClassController extends BaseController {

	@Autowired
	private SysParamClassService sysParamService;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysParamClass sysParam) {
		PageInfo<SysParamClass> page = sysParamService.findByPage(sysParam);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysParamClass sysParam = sysParamService.findById(id);
		return ResponeModel.ok(sysParam);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysParamClass sysParam, BindingResult bindingResult) {
		sysParam.setCreateBy(SecurityUtils.getLoginName());
		sysParam.setVisible(1); // 默认可用
		sysParam.setPhysicsFlag(1);
		int cnt = sysParamService.save(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysParamClass sysParam, BindingResult bindingResult) {
		sysParam.setUpdateBy(SecurityUtils.getLoginName());
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
		Assert.notNull( id, "id为空");
		SysParamClass sysParam = new SysParamClass();
		sysParam.setId(String.valueOf(id));
		sysParam.setPhysicsFlag(phyFlag);
		int cnt = sysParamService.changeById(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/visible")
	public ResponeModel visible(@RequestParam Serializable id, @RequestParam Integer visible) {
		Assert.notNull( id, "id为空");
		SysParamClass sysParam = new SysParamClass();
		sysParam.setId(String.valueOf(id));
		sysParam.setVisible(visible);
		int cnt = sysParamService.changeById(sysParam);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}



}
