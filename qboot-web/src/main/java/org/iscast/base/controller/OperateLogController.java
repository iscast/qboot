package org.iscast.base.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import org.iscast.base.dto.SysOperateLogDto;
import org.iscast.base.service.impl.SysOperateLogService;
import org.iscast.common.controller.BaseController;
import org.iscast.web.dto.ResponeModel;

/**
 * <p>Title: OperateLogController</p>
 * <p>Description: 日志管理</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/operatelog")
public class OperateLogController extends BaseController {

	@Autowired
	private SysOperateLogService operationLogService;

	@PreAuthorize("hasAuthority('sys:operatelog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysOperateLogDto sysLog, BindingResult bindingResult) {
		PageInfo<SysOperateLogDto> page = operationLogService.findByPage(sysLog);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:operatelog:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysOperateLogDto sysLog = operationLogService.findById(id);
		//富文本处理
		return ResponeModel.ok(sysLog);
	}
}
