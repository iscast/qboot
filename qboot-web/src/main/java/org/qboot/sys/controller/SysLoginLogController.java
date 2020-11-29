package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.sys.dto.SysLoginLogDto;
import org.qboot.sys.service.impl.SysLoginLogService;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * <p>Title: SysLoginLogController</p>
 * <p>Description: 系统登陆日志</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/loginlog")
public class SysLoginLogController extends BaseController {

	@Autowired
	private SysLoginLogService sysLoginLogService;

	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysLoginLogDto sysLoginLog, BindingResult bindingResult) {
		PageInfo<SysLoginLogDto> page = sysLoginLogService.findByPage(sysLoginLog);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysLoginLogDto sysLoginLog = sysLoginLogService.findById(id);
		//富文本处理
		return ResponeModel.ok(sysLoginLog);
	}

	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@PostMapping("/qryOnlineUser")
	public ResponeModel qryOnlineUser() {
        return ResponeModel.error();
	}
	
}
