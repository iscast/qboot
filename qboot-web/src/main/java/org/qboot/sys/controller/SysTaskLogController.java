package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.sys.dto.SysTaskLogDto;
import org.qboot.sys.service.SysTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务日志
 * @author history
 */
@RestController
@RequestMapping("${admin.path}/sys/tasklog")
public class SysTaskLogController extends BaseController {
	
	@Autowired
    private SysTaskLogService sysTaskLogService;
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysTaskLogDto sysTaskLog) {
		PageInfo<SysTaskLogDto> page = sysTaskLogService.findByPage(sysTaskLog);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:task:delete')")
	@PostMapping("/deleteLog")
	public ResponeModel deleteLog(SysTaskLogDto sysTaskLog) {
		sysTaskLogService.deleteByTaskId(sysTaskLog.getTaskId());
		return ResponeModel.ok();
	}
	
	
}
