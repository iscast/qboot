package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysOperateLogInfoDto;
import org.qboot.base.service.impl.SysOperateLogInfoService;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * <p>Title: OperateLogInfoController</p>
 * <p>Description: 系统操作日志</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/operateinfo")
public class OperateLogInfoController extends BaseController {

	@Autowired
	private SysOperateLogInfoService operationLogInfoService;

	@PreAuthorize("hasAuthority('sys:operateinfo:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysOperateLogInfoDto logInfoDto, BindingResult bindingResult) {
		PageInfo<SysOperateLogInfoDto> page = operationLogInfoService.findByPage(logInfoDto);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:operateinfo:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysOperateLogInfoDto logInfoDto = operationLogInfoService.findById(id);
		return ResponeModel.ok(logInfoDto);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:operateinfo:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysOperateLogInfoDto logInfoDto, BindingResult bindingResult) {
		boolean uriIsUsed = operationLogInfoService.uriIsUsed(null, logInfoDto.getReqUri());
		if(!uriIsUsed) {
			return ResponeModel.error("URI地址配置已经存在，请检查！");
		}
		logInfoDto.setCreateBy(SecurityUtils.getLoginName());
		int cnt = operationLogInfoService.save(logInfoDto);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:operateinfo:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysOperateLogInfoDto logInfoDto,BindingResult bindingResult) {
		boolean uriIsUsed = operationLogInfoService.uriIsUsed(logInfoDto.getLogId(), logInfoDto.getReqUri());
		if(!uriIsUsed) {
			return ResponeModel.error("URI地址配置已经存在，请检查！");
		}
		logInfoDto.setUpdateBy(SecurityUtils.getLoginName());
		int cnt = operationLogInfoService.update(logInfoDto);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:operateinfo:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = operationLogInfoService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
}
