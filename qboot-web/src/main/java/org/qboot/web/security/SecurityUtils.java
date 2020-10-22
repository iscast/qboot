package org.qboot.web.security;

import org.qboot.common.constant.SysConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>Title: LoginUser</p>
 * <p>Description: current login user tool</p>
 * @author history
 * @date 2018-09-08
 */
public class SecurityUtils {
	
	public static Long getUserId() {
		QUser user = getUser();
		if(null!=user){
			return user.getUserId();
		}
		return null;
	}

	public static String getLoginName() {
        QUser user = getUser();
        if(null!=user){
            return user.getLoginName();
        }
        return null;
	}
	
	public static QUser getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			Object principal = authentication.getPrincipal();
			if(principal instanceof QUserDetails) {
				QUserDetails userDetails =  (QUserDetails) principal;
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
