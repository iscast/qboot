package org.qboot.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    boolean doBusiness(HttpServletRequest request, HttpServletResponse response) throws LoginProcessFailException;

    /**
     * 处理顺序
     */
    int order();
}
