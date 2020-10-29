package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysTaskLog;
import org.qboot.base.service.impl.SysTaskLogService;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
