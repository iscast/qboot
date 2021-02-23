package org.qboot.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.IdGen;
import org.qboot.common.utils.IpUtils;
import org.qboot.sys.dao.SysLoginLogDao;
import org.qboot.sys.dto.SysLoginLogDto;
import org.qboot.sys.service.SysLoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

/**
 * 登录日志
 * @author history
 */
@Service
public class SysLoginLogServiceImpl extends CrudService<SysLoginLogDao, SysLoginLogDto> implements SysLoginLogService {

	protected Logger logger = LoggerFactory.getLogger(SysLoginLogServiceImpl.class);

    /**
     * 登录日志
     * @param status
     * @param loginName
     * @param request
     */
    @Override
    public void saveLog(String status, String loginName, HttpServletRequest request) {
        new LogThread(IpUtils.getIpAddr(request), request.getHeader("User-Agent"), status, loginName).run();
    }


    private class LogThread extends Thread {
        private String ip;
        private String userAgentStr;
        private String status;
        private String loginName;


        public LogThread(String ip, String userAgentStr, String status, String loginName) {
            this.ip = ip;
            this.userAgentStr = userAgentStr;
            this.status = status;
            this.loginName = loginName;
        }

        @Override
        public void run() {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            Browser browser = userAgent.getBrowser();
            OperatingSystem os = userAgent.getOperatingSystem();
            String deviceName = os.getName() + " "+ os.getDeviceType();
            String browserStr = browser.getName() +" "+ browser.getVersion(userAgentStr);
            try {
                String currentLoginName = SecurityUtils.getLoginName();
                if(StringUtils.isBlank(currentLoginName)) {
                    currentLoginName = loginName;
                }
                SysLoginLogDto loginLog = new SysLoginLogDto();
                loginLog.setLoginName(loginName);
                loginLog.setId(IdGen.uuid());
                loginLog.setStatus(status);
                loginLog.setBrowserName(browserStr);
                loginLog.setDeviceName(deviceName);
                loginLog.setIp(ip);
                loginLog.setUserAgent(userAgentStr);
                loginLog.setLoginTime(new Date());
                loginLog.setCreateBy(currentLoginName);
                loginLog.setUpdateBy(currentLoginName);
                save(loginLog);
            } catch (Exception e) {
                logger.error("保存登录日记记录异常，{}....", ExceptionUtils.getStackTrace(e));
            }

        }
    }
}
