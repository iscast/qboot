package org.qboot.mon.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.mon.dto.MonLoginLogDto;
import org.qboot.mon.service.MonLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登陆日志
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/mon/loginlog")
public class MonLoginLogController extends BaseController {

	@Autowired
	private MonLoginLogService monLoginLogService;

	@PreAuthorize("hasAuthority('mon:loginlog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(MonLoginLogDto sysLoginLog) {
		PageInfo<MonLoginLogDto> page = monLoginLogService.findByPage(sysLoginLog);
		return ResponeModel.ok(page);
	}
}