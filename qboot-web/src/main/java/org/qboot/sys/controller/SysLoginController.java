package org.qboot.sys.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.RSAsecurity;
import org.qboot.common.utils.TreeHelper;
import org.qboot.sys.dto.SysMenuDto;
import org.qboot.sys.dto.SysUserDto;
import org.qboot.sys.service.SysMenuService;
import org.qboot.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@PostMapping("/getUserInfo")
	public ResponeModel getUserInfo() {
		CustomUser sysUser = SecurityUtils.getUser();
		if(null == sysUser) {
            return ResponeModel.error(SYS_USER_NOTEXISTS);
		}

        return ResponeModel.ok(sysUser);
	}

    @GetMapping("/checkFirstLogin")
    public ResponeModel checkFirstLogin() {
    	if (!SecurityUtils.isSuperAdmin(SecurityUtils.getLoginName())
                && sysUserService.selectFirstLoginUser(SecurityUtils.getUserId())) {
            return ResponeModel.error(SYS_USER_LOGIN_FIRST_TIME_HINT);
    	}
		return ResponeModel.ok();
	}

    @AccLog
	@GetMapping("/switchLanguage")
	public ResponeModel switchLanguage(@RequestParam String lang){
		if (StringUtils.isBlank(lang)) {
			return ResponeModel.error(SYS_USER_LANG_INCORRECT);
		}
        SysUserDto sysUser = sysUserService.findById(SecurityUtils.getUserId());
		sysUser.setLang(lang);
		return ResponeModel.ok(sysUserService.updateInfo(sysUser));
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
		return ResponeModel.ok(SysConstants.SUCCESS, keyPair.getT1());
	}

    private String loginPage() {
        // 判断是否自定义了登陆页面
        String loginPage = "/login_pc.html";
        if(StringUtils.isNotBlank(adminLoginUrl) && adminLoginUrl.contains("html")) {
            int i = adminLoginUrl.lastIndexOf("/");
            loginPage = adminLoginUrl.substring(i);
        }
        return loginPage;
    }
}
