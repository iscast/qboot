package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.sys.dto.SysOperateLogDto;
import org.qboot.sys.service.impl.SysOperateLogService;
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
 * <p>Title: SysOperateLogController</p>
 * <p>Description: 日志管理</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/operatelog")
public class SysOperateLogController extends BaseController {

	@Autowired
	private SysOperateLogService sysOperateLogService;

	@PreAuthorize("hasAuthority('sys:operatelog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysOperateLogDto sysLog, BindingResult bindingResult) {
		PageInfo<SysOperateLogDto> page = sysOperateLogService.findByPage(sysLog);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:operatelog:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysOperateLogDto sysLog = sysOperateLogService.findById(id);
		//富文本处理
		return ResponeModel.ok(sysLog);
	}
}