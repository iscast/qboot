package org.qboot.common.filter;

import org.qboot.sys.dto.SysUserDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义登陆逻辑处理器样例
 * @Author: iscast
 * @Date: 2020/11/27 14:53
 */
//@Component
public class DemoLoginProcessFilter implements ILoginProcessFilter{

    @Override
    public boolean doBusiness(HttpServletRequest request, SysUserDto sysUser) throws LoginProcessFailException {
        String param = obtainParam(request);
        String fldS2 = sysUser.getFldS2();
        if(param.equals(fldS2)) {
            return true;
        }
        throw new LoginProcessFailException("login process fail");
    }

    protected String obtainParam(HttpServletRequest request) {
        return request.getParameter("xxx_Param");
    }
}
