package org.qboot.sys.bo;

import org.qboot.common.utils.DateUtils;

import java.util.Date;

/**
 * 日志记录bo
 * @Author: iscast
 * @Date: 2020/12/24 16:27
 */
public class AccLogBO {

    private String uri;

    private String requestIP;

    private Long userId;

    private Date requestTime;

    private Long costTime;

    private String requestParams;

    private String responseParams;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    @Override
    public String toString() {
        return "userId=[" + userId + "],requestIP=[" + requestIP + "],uri=[" + uri +
                "],requestTime=[" + DateUtils.formatDateTimeMilli(requestTime) + "],costTime=[" + costTime + "]" + '\n' +
                "requestParams=" + requestParams + '\n' + "responseParams=" + responseParams ;
    }
}
