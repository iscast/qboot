package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysLoginLog;
import org.qboot.base.service.impl.SysLoginLogService;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * <p>Title: LoginLogController</p>
 * <p>Description: 系统登陆日志</p>
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

	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@PostMapping("/qryOnlineUser")
	public ResponeModel qryOnlineUser() {
        return ResponeModel.error();
	}
	
}
