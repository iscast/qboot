package org.qboot.mon.bo;

import java.io.Serializable;

/**
 * 缓存用户信息
 * @Author: iscast
 * @Date: 2021/6/21 10:53
 */
public class CacheUserBO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String loginName;
    private String sessionId;
    private String loginTime;
    private String ip;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
