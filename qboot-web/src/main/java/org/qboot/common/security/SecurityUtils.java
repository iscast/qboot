package org.qboot.common.security;

import org.qboot.common.constants.SysConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * login user tools
 * @author iscast
 * @date 2020-09-25
 */
public class SecurityUtils {
	
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
		return SysConstants.SUPER_ADMIN_NAME.equals(userName);
	}
	
	public static boolean isSuperAdmin() {
		return isSuperAdmin(getLoginName());
	}
}
