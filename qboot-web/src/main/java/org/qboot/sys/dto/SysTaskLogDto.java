package org.qboot.sys.dto;

import java.util.Date;

import org.qboot.common.entity.BaseEntity;

/**
 * 任务日志
 * @author history
*/
public class SysTaskLogDto extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	/**任务ID.*/
	private String taskId;
	/**执行开始时间.*/
	private Date beginTime;
	/**执行结束时间.*/
	private Date endTime;
	/**执行耗时.*/
	private Long costTime;
	/**执行节点IP.*/
	private String execIp;
	/**执行结果.1成功;2失败*/
	private Integer execStatus;
	/**结果描述.*/
	private String execResult;

	/**设置任务ID.*/
	public void setTaskId(String taskId){
		this.taskId=taskId;
	}
	/**获取任务ID.*/
	public String getTaskId(){
		return taskId;
	}
	/**设置执行开始时间.*/
	public void setBeginTime(Date beginTime){
		this.beginTime=beginTime;
	}
	/**获取执行开始时间.*/
	public Date getBeginTime(){
		return beginTime;
	}
	/**设置执行结束时间.*/
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	/**获取执行结束时间.*/
	public Date getEndTime(){
		return endTime;
	}
	/**设置执行耗时.*/
	public void setCostTime(Long costTime){
		this.costTime=costTime;
	}
	/**获取执行耗时.*/
	public Long getCostTime(){
		return costTime;
	}
	/**设置执行节点IP.*/
	public void setExecIp(String execIp){
		this.execIp=execIp;
	}
	/**获取执行节点IP.*/
	public String getExecIp(){
		return execIp;
	}
	/**设置执行结果.1成功;2失败*/
	public void setExecStatus(Integer execStatus){
		this.execStatus=execStatus;
	}
	/**获取执行结果.1成功;2失败*/
	public Integer getExecStatus(){
		return execStatus;
	}
	/**设置结果描述.*/
	public void setExecResult(String execResult){
		this.execResult=execResult;
	}
	/**获取结果描述.*/
	public String getExecResult(){
		return execResult;
	}
}

