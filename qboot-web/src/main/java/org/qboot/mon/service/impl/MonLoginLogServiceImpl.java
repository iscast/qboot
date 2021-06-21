package org.qboot.mon.service.impl;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.IdGen;
import org.qboot.common.utils.IpUtils;
import org.qboot.mon.dao.MonLoginLogDao;
import org.qboot.mon.dto.MonLoginLogDto;
import org.qboot.mon.service.MonLoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 登录日志
 * @author history
 */
@Service
public class MonLoginLogServiceImpl extends CrudService<MonLoginLogDao, MonLoginLogDto> implements MonLoginLogService {
	protected Logger logger = LoggerFactory.getLogger(MonLoginLogServiceImpl.class);

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
                MonLoginLogDto loginLog = new MonLoginLogDto();
                loginLog.setLoginName(loginName);
                loginLog.setId(IdGen.uuid());
                loginLog.setStatus(status);
                loginLog.setBrowserName(browserStr);
                loginLog.setDeviceName(deviceName);
                loginLog.setIp(ip);
                loginLog.setUserAgent(userAgentStr);
                loginLog.setLoginTime(new Date());
                save(loginLog);
            } catch (Exception e) {
                logger.error("save login log err : {}", ExceptionUtils.getStackTrace(e));
            }

        }
    }
}
