package org.iscast.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import org.iscast.base.dto.SysTaskLog;
import org.iscast.base.service.impl.SysTaskLogService;
import org.iscast.common.controller.BaseController;
import org.iscast.web.dto.ResponeModel;

/**
 * 任务日志控制器
 * @author history
 */
@RestController
@RequestMapping("${admin.path}/sys/tasklog")
public class TaskLogController extends BaseController {
	
	@Autowired
	SysTaskLogService sysTaskLogService ;
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysTaskLog sysTaskLog) {
		PageInfo<SysTaskLog> page = sysTaskLogService.findByPage(sysTaskLog);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:task:deleteLog')")
	@PostMapping("/deleteLog")
	public ResponeModel deleteLog(SysTaskLog sysTaskLog) {
		sysTaskLogService.deleteByTaskId(sysTaskLog.getTaskId());
		return ResponeModel.ok();
	}
	
	
}
