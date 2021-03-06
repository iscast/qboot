package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.IdGen;
import org.qboot.sys.dto.SysTaskDto;
import org.qboot.sys.exception.SysTaskException;
import org.qboot.sys.exception.errorcode.SysModuleErrTable;
import org.qboot.sys.service.SysTaskService;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * 任务控制器
 * @author history
 */
@RestController
@RequestMapping("${admin.path}/sys/task")
public class SysTaskController extends BaseController {
	
	@Autowired
    private SysTaskService sysTaskService;
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysTaskDto sysTask) {
		PageInfo<SysTaskDto> page = sysTaskService.findByPage(sysTask);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@RequestMapping("/get")
	public ResponeModel get(SysTaskDto sysTask) {
		SysTaskDto result = sysTaskService.findById(sysTask.getId());
        if(null == result) {
            return ResponeModel.error(SYS_TASK_QUERY_FAIL);
        }
		return ResponeModel.ok(result);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:task:save')")
	@PostMapping("/save")
	public ResponeModel save(SysTaskDto sysTask) {
        sysTask.setId(IdGen.uuid());
		sysTask.setUpdateDate(new Date());
		sysTask.setCreateDate(new Date());
		sysTask.setCreateBy(SecurityUtils.getLoginName());
		sysTask.setUpdateBy(SecurityUtils.getLoginName());
		this.checkParams(sysTask);
        int cnt = sysTaskService.save(sysTask);
        if(cnt > 0) {
            return ok();
        }
        return ResponeModel.error(SYS_TASK_SAVE_FAIL);
    }

    @AccLog
	@PreAuthorize("hasAuthority('sys:task:update')")
	@PostMapping("/updateSelect")
	public ResponeModel updateSelect(SysTaskDto sysTask) {
		this.checkParams(sysTask);
        int cnt = sysTaskService.update(sysTask);
        if(cnt > 0) {
            return ok();
        }
        return ResponeModel.error(SYS_TASK_UPDATE_FAIL);
    }

    @AccLog
	@PreAuthorize("hasAuthority('sys:task:update')")
	@PostMapping("/updateStatus")
	public ResponeModel updateStatus(SysTaskDto sysTask) {
        int cnt = sysTaskService.updateStatus(sysTask);
        if(cnt > 0) {
            return ok();
        }
        return ResponeModel.error(SYS_TASK_UPDATE_FAIL);
	}
	
	@PreAuthorize("hasAuthority('sys:task:execute')")
	@PostMapping("/execute")
	public ResponeModel execute(SysTaskDto sysTask) {
		sysTaskService.runOnce(sysTask.getId());
		return ok();
	}
	
	@PreAuthorize("hasAuthority('sys:task:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(SysTaskDto sysTask) {
        int cnt = sysTaskService.deleteById(sysTask.getId());
        if(cnt > 0) {
            return ok();
        }
        return ResponeModel.error(SYS_TASK_DELETE_FAIL);
    }

	/**
	 * 检测业务参数的合法性
	 * @return
	 */
	private void checkParams(SysTaskDto sysTask){
		//xss影响正常业务反向处理
		if(StringUtils.isNotEmpty(sysTask.getParams())
				&&StringUtils.contains(sysTask.getParams(), "&quot;")){
			sysTask.setParams(sysTask.getParams().replaceAll("&quot;", "\""));
		}
		//检测任务名是否已存在
		Long count = sysTaskService.countByTaskName(sysTask);
		if(count>=1){
            throw new SysTaskException(SysModuleErrTable.SYS_TASK_DUPLICATE);
		}
        // 字符串是否与正则表达式相匹配
        if(!CronExpression.isValidExpression(sysTask.getCronExp())){
            throw new SysTaskException(SysModuleErrTable.SYS_TASK_EXPRESSION_ERROR);
        }
	}
}