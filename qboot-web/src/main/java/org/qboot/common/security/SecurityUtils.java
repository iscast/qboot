package org.qboot.common.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.utils.CodecUtils;
import org.qboot.sys.dto.SysUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * login user tools
 * @author iscast
 * @date 2020-09-25
 */
@Component
@ConfigurationProperties(prefix="qboot")
public class SecurityUtils {
    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    private static HashMap<String, SysUserDto> adminMap = new HashMap<>();

    private List<SysUserDto> admins = new ArrayList<>();
    public void setAdmins(List<SysUserDto> admins) {
        this.admins = admins;
    }


    static SysUserDto getAdminUser(String loginName) {
        return adminMap.get(loginName);
    }

    @PostConstruct
    public void init() {
        if(CollectionUtils.isEmpty(admins)) {
            logger.info("system don't set admin or config is incomplete");
            return;
        }

        for(SysUserDto adm : admins) {
            String password = adm.getPassword();
            String name = adm.getName();
            String salt = RandomStringUtils.randomAlphanumeric(20);
            adm.setLoginName(name);
            adm.setSalt(salt);
            adm.setStatus(SysConstants.SYS_USER_STATUS_NORMAL);
            adm.setPassword(CodecUtils.sha256(password + salt));
            adminMap.put(name, adm);
        }

        admins = null;
    }

	public static String getUserId() {
		CustomUser user = getUser();
		if(null!=user){
			return user.getUserId();
		}
		return null;
	}

	public static String getLoginName() {
        CustomUser user = getUser();
        if(null!=user){
            return user.getLoginName();
        }
        return null;
	}
	
	public static CustomUser getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			Object principal = authentication.getPrincipal();
			if(principal instanceof CustomUserDetails) {
				CustomUserDetails userDetails =  (CustomUserDetails) principal;
				return userDetails.getUser();
			}
		}
		return null;
	}
	
	public static boolean isSuperAdmin(String userName) {
        return adminMap.containsKey(userName);
	}
	
	public static boolean isSuperAdmin() {
		return isSuperAdmin(getLoginName());
	}
}
