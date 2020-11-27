package org.qboot.common.filter;

import org.qboot.sys.dto.SysUserDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义登陆流程处理
 * @Author: iscast
 * @Date: 2020/11/26 19:09
 */
public interface ILoginProcessFilter {

    /**
     * 处理登陆过程
     * AuthenticationException
     */
    boolean doBusiness(HttpServletRequest request, SysUserDto sysUser) throws LoginProcessFailException;

}
