package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.utils.RedisTools;
import org.qboot.sys.dto.SysLoginLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 系统在线用户
 * @author iscast
 * @date: 2021/6/18 16:43
 */
@RestController
@RequestMapping("${admin.path}/mon/online")
public class OnlineController extends BaseController {

    @Autowired
    private RedisTools redisTools;

	@PreAuthorize("hasAuthority('mon:online:qry')")
	@PostMapping("/qry")
	public ResponeModel qry() {
        Set<String> keys = redisTools.getKeys(CacheConstants.CACHE_PREFIX_LOGIN_USER + "*");
        PageInfo<SysLoginLogDto> page = null;
		return ResponeModel.ok(page);
	}
}