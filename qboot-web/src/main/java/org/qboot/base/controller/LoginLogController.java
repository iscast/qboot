package org.qboot.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysLoginLog;
import org.qboot.base.service.impl.SysLoginLogService;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

//		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
//		for (int i = 0,len=allPrincipals.size(); i < len; i++) {
//			logger.info(JSONObject.toJSONString(allPrincipals.get(i)));
//			System.out.println(allPrincipals.get(i));//可以转换成spring的User
//		}

		SysLoginLog log = new SysLoginLog();
		log.setUserId("12");
		log.setLoginName("haha");
		List list = new ArrayList<>();
		list.add(log);
		PageInfo<SysLoginLog> page = new PageInfo(list);;
		return ResponeModel.ok(page);
	}
	
}
