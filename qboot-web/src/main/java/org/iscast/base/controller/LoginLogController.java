package org.iscast.base.controller;

import java.io.Serializable;

import org.iscast.common.controller.BaseController;
import org.iscast.web.dto.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import org.iscast.base.dto.SysLoginLog;
import org.iscast.base.service.impl.SysLoginLogService;

/**
 * <p>Title: LoginLogController</p>
 * <p>Description: 系统登陆日志</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/loginlog")
public class LoginLogController extends BaseController {

	@Autowired
	private SysLoginLogService sysLoginLogService;

	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysLoginLog sysLoginLog, BindingResult bindingResult) {
		PageInfo<SysLoginLog> page = sysLoginLogService.findByPage(sysLoginLog);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysLoginLog sysLoginLog = sysLoginLogService.findById(id);
		//富文本处理
		return ResponeModel.ok(sysLoginLog);
	}
	
}
