package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.base.dto.SysTask;
import org.qboot.base.service.impl.SysTaskService;
import org.qboot.common.controller.BaseController;
import org.qboot.common.exception.ServiceException;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.SecurityUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 任务控制器
 * @author history
 */
@RestController
@RequestMapping("${admin.path}/sys/task")
public class TaskController extends BaseController {
	
	@Autowired
	SysTaskService sysTaskService ;
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@GetMapping("/qryPage")
	public ResponeModel qryPage(SysTask sysTask) {
		PageInfo<SysTask> page = sysTaskService.findByPage(sysTask);
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:task:qry')")
	@RequestMapping("/get")
	public ResponeModel get(SysTask sysTask) {
		SysTask result = sysTaskService.findById(sysTask.getId()) ;
		return ResponeModel.ok(result);
	}
	
	@PreAuthorize("hasAuthority('sys:task:save')")
	@PostMapping("/save")
	public ResponeModel save(SysTask sysTask) {
		sysTask.setUpdateDate(new Date());
		sysTask.setCreateDate(new Date());
		sysTask.setCreateBy(SecurityUtils.getLoginName());
		sysTask.setUpdateBy(SecurityUtils.getLoginName());
		this.checkParams(sysTask);
		sysTaskService.save(sysTask) ;
		return ResponeModel.ok();
	}
	
	@PreAuthorize("hasAuthority('sys:task:edit')")
	@PostMapping("/updateSelect")
	public ResponeModel updateSelect(SysTask sysTask) {
		this.checkParams(sysTask);
		sysTaskService.updateById(sysTask) ;
		return ResponeModel.ok();
	}
	
	@PreAuthorize("hasAuthority('sys:task:edit')")
	@PostMapping("/updateStatus")
	public ResponeModel updateStatus(SysTask sysTask) {
		sysTaskService.updateStatus(sysTask) ;
		return ResponeModel.ok();
	}
	
	@PreAuthorize("hasAuthority('sys:task:execute')")
	@PostMapping("/execute")
	public ResponeModel execute(SysTask sysTask) {
		sysTaskService.runOnce(sysTask.getId());
		return ResponeModel.ok();
	}
	
	@PreAuthorize("hasAuthority('sys:task:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(SysTask sysTask) {
		sysTaskService.deleteTask(sysTask.getId()) ;
		return ResponeModel.ok();
	}
	/**
	 * 检测业务参数的合法性
	 * @return
	 */
	private void checkParams(SysTask sysTask){
		//xss影响正常业务反向处理
		if(StringUtils.isNotEmpty(sysTask.getParams())
				&&StringUtils.contains(sysTask.getParams(), "&quot;")){
			sysTask.setParams(sysTask.getParams().replaceAll("&quot;", "\""));
		}
		//检测任务名是否已存在
		Long count = sysTaskService.countByTaskName(sysTask);
		if(count>=1){
			throw new ServiceException("任务名已存在!!!") ;
		}
        // 字符串是否与正则表达式相匹配
        if(!CronExpression.isValidExpression(sysTask.getCronExp())){
        	throw new ServiceException("时间表达式异常!!!") ;
        }
        
	}
	
}
