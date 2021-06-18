package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.utils.RedisTools;
import org.qboot.sys.dto.SysLoginLogDto;
import org.qboot.sys.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 系统登陆日志
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/mon/loginlog")
public class SysLoginLogController extends BaseController {

	@Autowired
	private SysLoginLogService sysLoginLogService;
    @Autowired
    private RedisTools redisTools;

	@PreAuthorize("hasAuthority('mon:loginlog:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysLoginLogDto sysLoginLog) {
		PageInfo<SysLoginLogDto> page = sysLoginLogService.findByPage(sysLoginLog);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('mon:loginlog:qry')")
	@PostMapping("/qryOnlineUser")
	public ResponeModel qryOnlineUser() {
        Set<String> keys = redisTools.getKeys(CacheConstants.CACHE_PREFIX_LOGIN_USER + "*");
        PageInfo<SysLoginLogDto> page = null;
		return ResponeModel.ok(page);
	}
}