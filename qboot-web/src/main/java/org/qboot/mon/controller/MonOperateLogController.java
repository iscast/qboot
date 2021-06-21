package org.qboot.mon.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.sys.exception.errorcode.SysModuleErrTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 访问日志
 * @author: iscast
 * @date: 2020/12/24 18:11
 */
@Controller
@RequestMapping("${admin.path}/mon/operatelog")
public class MonOperateLogController extends BaseController {

    @Value("${server.logPath:}")
    private String logPath = "logs";
    private final static String access = "access";

    @AccLog
	@PreAuthorize("hasAuthority('mon:operatelog:qry')")
	@GetMapping("/download")
	public ResponeModel download(String date, String level, HttpServletResponse response) throws Exception {
        if(StringUtils.isBlank(level)) {
            level = access;
        }

        String targetName = "log_" + level;
        String fileName = logPath + File.separator + targetName + ".log";
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

        File file = new File(fileName);
        if (!file.exists())
            return ResponeModel.error(SysModuleErrTable.SYS_LOG_NO_EXIST);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        zos.putNextEntry(new ZipEntry("logs" + File.separator + file.getName()));
        byte[] bytes = FileUtils.readFileToByteArray(file);
        IOUtils.write(bytes, zos);
        zos.closeEntry();
        zos.close();
        byte[] byteArray = bos.toByteArray();

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename="+ targetName + ".zip");
        response.setContentLength(byteArray.length);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.write(byteArray, output);
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(bos);
		return ResponeModel.ok();
	}

}
