package org.qboot.sys.controller;

import com.alibaba.fastjson.JSONObject;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.utils.TreeHelper;
import org.qboot.sys.dto.SysMenuDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.impl.SysMenuService;
import org.qboot.sys.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysUserErrTable.*;

/**
 * 登录控制器
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/user")
public class SysLoginController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysMenuService sysMenuService;

    @Value("${admin.loginUrl:}")
    private String adminLoginUrl;

	private TreeHelper<SysMenuDto> treeHelper = new TreeHelper<SysMenuDto>();
	
	@PostMapping("/getUserMenus")
	public ResponeModel getUserMenus() {
		CustomUser user = SecurityUtils.getUser();
		List<SysMenuDto> menus = null;
		// 系统管理员，拥有最高权限
		if (SecurityUtils.isSuperAdmin()) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(user.getUserId());
		}
		List<SysMenuDto> treeMenus = treeHelper.treeGridList(menus);
		return ResponeModel.ok(treeMenus);
	}

	@GetMapping("/getUserInfo")
	public ResponeModel getUserInfo() {
		CustomUser sysUser = SecurityUtils.getUser();
		if(null == sysUser) {
            return ResponeModel.error(SYS_USER_NOTEXISTS);
		}

        return ResponeModel.ok(sysUser);
	}

	@PostMapping("/updateInfo")
	public ResponeModel updateInfo(SysUserDto user) {
        MyAssertTools.hasLength(user.getName(), SYS_USER_NAME_EMPTY);
		SysUserDto sysUser = new SysUserDto();
		sysUser.setName(user.getName());
		sysUser.setEmail(user.getEmail());
		sysUser.setMobile(user.getMobile());
		sysUser.setId(SecurityUtils.getUserId());
		int cnt = sysUserService.update(sysUser);
        if(cnt > 0) {
            return ok();
        }
        return ResponeModel.error();

	}

    @GetMapping("/checkFirstLogin")
    public ResponeModel checkFirstLogin() {
    	if (!SecurityUtils.isSuperAdmin(SecurityUtils.getLoginName())) {
    		boolean firstLogin = sysUserService.selectFirstLoginUser(SecurityUtils.getUserId());
        	if(firstLogin) {
        		return ResponeModel.error(SYS_USER_LOGIN_FIRST_TIME_HINT);
        	}
		}
		return ResponeModel.ok();
	}

    @AccLog
	@PostMapping("/updatePwd")
	public ResponeModel updatePwd(@RequestParam String password, @RequestParam String oldPassword) {
		Long userId = SecurityUtils.getUserId();
		if (!sysUserService.validatePwd(oldPassword, userId)) {
			return ResponeModel.error(SYS_USER_ORIGINAL_PWD_INCORRECT);
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(userId);
		sysUser.setPassword(password);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

	@GetMapping("/switchLanguage")
	public ResponeModel switchLanguage(@RequestParam String lang){
		Long userId = SecurityUtils.getUserId();
		if (StringUtils.isEmpty(lang)) {
			return ResponeModel.error(SYS_USER_LANG_INCORRECT);
		}
		SysUserDto sysUser = new SysUserDto();
		sysUser.setId(userId);
		sysUser.setLang(lang);
		int cnt = sysUserService.update(sysUser);
		return ResponeModel.ok(cnt);
	}

    @GetMapping("/getLoginPage")
    public ResponeModel getLoginPage(){
        JSONObject data = new JSONObject();
        data.put("loginPage", loginPage());
        return ResponeModel.ok(data);
    }

	@GetMapping("/getPublicKey")
	public ResponeModel getPublicKey(HttpServletRequest request) {
		Tuple2<String, String> keyPair = RSAsecurity.getInstance().generateKeyPair();
		request.getSession().setAttribute("privateKey", keyPair.getT2());
		return ResponeModel.ok("GetPublicKeySuccess",keyPair.getT1());
	}

    private String loginPage() {
        // 判断是否自定义了登陆页面
        String loginPage = "/login_pc.html";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(adminLoginUrl) && adminLoginUrl.contains("html")) {
            int i = adminLoginUrl.lastIndexOf("/");
            loginPage = adminLoginUrl.substring(i);
        }
        return loginPage;
    }
}
