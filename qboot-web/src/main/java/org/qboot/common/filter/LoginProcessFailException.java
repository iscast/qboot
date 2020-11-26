package org.qboot.common.filter;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义登陆失败错误异常
 * @Author: iscast
 * @Date: 2020/11/26 19:18
 */
public class LoginProcessFailException extends AuthenticationException {

    public LoginProcessFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginProcessFailException(String msg) {
        super(msg);
    }
}
