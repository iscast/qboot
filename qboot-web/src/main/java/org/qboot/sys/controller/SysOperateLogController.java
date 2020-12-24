package org.qboot.sys.controller;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.sys.exception.errorcode.SysModuleErrTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 访问日志
 * @author: iscast
 * @date: 2020/12/24 18:11
 */
@Controller
@RequestMapping("${admin.path}/sys/operatelog")
public class SysOperateLogController extends BaseController {

    @Value("${server.logPath:}")
    private String logPath = "logs";

//    private final static String info = "info";
//    private final static String warn = "warn";
//    private final static String error = "error";
    private final static String access = "access";

	@PreAuthorize("hasAuthority('sys:operatelog:qry')")
	@GetMapping("/download")
	public ResponeModel download(String date, String level, HttpServletResponse response) throws Exception {
        logger.info("user:{} download system date:{} level:{} log {}", SecurityUtils.getLoginName(), date, level, logPath);
        response.setContentType("multipart/form-data");
        if(StringUtils.isBlank(level)) {
            level = access;
        }

        String fileName = logPath + File.separator + "log_" + level + ".log";
        if(StringUtils.isNotBlank(date)) {
            String dirStr = logPath + File.separator + level;
            File dir = new File(dirStr);
            if(dir.exists()) {
                String[] list = dir.list();
                for(String name : list) {
                    if(name.contains(date)) {
                        fileName = logPath + File.separator + level + File.separator + name;
                    }
                }
            }
        }

        if (StringUtils.isBlank(fileName)) {
            return ResponeModel.error(SysModuleErrTable.SYS_LOG_NO_EXIST);
        }

        ServletOutputStream out = null;
        InputStream inputStream = null;
        try{
            File file = new File(fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            inputStream = new FileInputStream(file);
            out = response.getOutputStream();

            int b = 0;
            byte[] buffer = new byte[512];
            while (b != -1){
                b = inputStream.read(buffer);
                //4.写到输出流(out)中
                out.write(buffer,0,b);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            logger.error("down log file fail", e);
        }finally {
            if(null != out)
                out.close();

            if(null != inputStream)
                inputStream.close();
        }

		return ResponeModel.ok();
	}

}
