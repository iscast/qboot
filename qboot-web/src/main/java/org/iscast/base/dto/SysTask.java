package org.iscast.base.dto;

import java.util.Date;

import org.iscast.common.entity.BaseEntity;
import org.iscast.common.constant.CacheConstants;

/**[sys_task]数据库实体模型
*@author history
*/
public class SysTask extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;
	/**任务名称.*/
	private String taskName;
	/**任务分组.*/
	private String groupName;
	/**时间表达式.*/
	private String cronExp;
	/**执行目标.*/
	private String taskTarget;
	/**任务状态.0未启用;1启用*/
	private Integer status;
	/**任务状态.1启用*/
	public static final int TASK_ENABLED = 1 ;
	/**任务状态.0未启用;*/
	public static final int TASK_DISABLED = 0 ;
	
	/**
	 * 内存运行状态 0 初始 1执行中
	 */
	private int runStatus ;
	/**
	 * 内存运行状态 0 初始
	 */
	public static final int RUN_STATUS_INI = 0 ;
	/**
	 * 内存运行状态 1执行中
	 */
	public static final int RUN_STATUS_RUNNING = 1 ;
	/**
	 * 执行方式:0阻塞 1非阻塞.
	 */
	private Integer execType ;
	/**最后执行时间.*/
	private Date lastRunTime;
	/**最后执行结果.*/
	private String lastResult;
	/**任务备注.*/
	private String remarks;
	/**附加参数.JSON格式*/
	private String params;
	
	public SysTask() {
		
	}
	public SysTask(long id) {
		this.setId(id);
	}
	/**设置任务名称.*/
	public void setTaskName(String taskName){
		this.taskName=taskName;
	}
	/**获取任务名称.*/
	public String getTaskName(){
		return taskName;
	}
	/**设置任务分组.*/
	public void setGroupName(String groupName){
		this.groupName=groupName;
	}
	/**获取任务分组.*/
	public String getGroupName(){
		return groupName;
	}
	/**设置时间表达式.*/
	public void setCronExp(String cronExp){
		this.cronExp=cronExp;
	}
	/**获取时间表达式.*/
	public String getCronExp(){
		return cronExp;
	}
	/**设置执行目标.*/
	public void setTaskTarget(String taskTarget){
		this.taskTarget=taskTarget;
	}
	/**获取执行目标.*/
	public String getTaskTarget(){
		return taskTarget;
	}
	/**设置任务状态.0未启用;1启用*/
	public void setStatus(Integer status){
		this.status=status;
	}
	/**获取任务状态.0未启用;1启用*/
	public Integer getStatus(){
		return status;
	}
	/**设置最后执行时间.*/
	public void setLastRunTime(Date lastRunTime){
		this.lastRunTime=lastRunTime;
	}
	/**获取最后执行时间.*/
	public Date getLastRunTime(){
		return lastRunTime;
	}
	/**设置最后执行结果.*/
	public void setLastResult(String lastResult){
		this.lastResult=lastResult;
	}
	/**获取最后执行结果.*/
	public String getLastResult(){
		return lastResult;
	}
	/**设置任务备注.*/
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	/**获取任务备注.*/
	public String getRemarks(){
		return remarks;
	}
	/**设置附加参数.JSON格式*/
	public void setParams(String params){
		this.params=params;
	}
	/**获取附加参数.JSON格式*/
	public String getParams(){
		return params;
	}
	public String getStatusName(){
		if(null==status){
			return "未知" ;
		}else if(TASK_ENABLED==status){
			return "启用";
		}else if(TASK_DISABLED==status){
			return "禁用";
		}
		return "未知" ;
	}
	public int getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(int runStatus) {
		this.runStatus = runStatus;
	}
	public String getRunStatusName(){
		if(RUN_STATUS_INI==runStatus){
			return "初始";
		}else if(RUN_STATUS_RUNNING==runStatus){
			return "执行中";
		}
		return "未知" ;
	}
	/**
	 * 执行方式:0阻塞 1非阻塞.
	 * @return
	 */
	public Integer getExecType() {
		return execType;
	}
	/**
	 * 执行方式:0阻塞 1非阻塞.
	 * @param execType
	 */
	public void setExecType(Integer execType) {
		this.execType = execType;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(this.getId()).append(",")
		.append("taskName=").append(this.getTaskName()).append(",")
		.append("groupName=").append(this.getGroupName()).append(",")
		.append("taskTarget=").append(this.getTaskTarget()).append(",")
		.append("status=").append(this.getStatus()).append(",")
		.append("cronExp=").append(this.getCronExp()).append(",")
		.append("params=").append(this.getParams()).append(",")
		.append("remarks=").append(this.getRemarks());
		return sb.toString();
	}
	/**
	 * 任务是否未启用
	 * @return
	 */
	public boolean isDisabled() {
		if(null==status){
			//未知的状态,默认认为关闭
			return true ;
		}else if(status==TASK_DISABLED){
			return true ;
		}
		return false;
	}
	/**
	 * 任务是否开启
	 * @return
	 */
	public boolean isEnabled() {
		if(null!=status&&status==TASK_ENABLED){
			return true ;
		}
		return false;
	}
	/**
	 * 任务对象缓存key
	 * @return
	 */
	public String toCacheKeyString() {
		StringBuilder taskInfoCacheKey = new StringBuilder(CacheConstants.TASK_INFO);
		taskInfoCacheKey.append(this.getId()) ;
		return taskInfoCacheKey.toString();
	}
	@Override
	public int hashCode() {
		return getId().intValue();
	}
	@Override
	public boolean equals(Object obj) {
		if(null==obj||this.getId()==null){
			return false ;
		}
		SysTask other = (SysTask)obj ;
		if(null==other.getId()){
			return false ;
		}
		return this.getId().longValue()==other.getId().longValue();
	}
	/**
	 * 立即执行缓存通知key
	 * @return
	 */
	public String toRunNowCacheNoticeString() {
		StringBuilder taskInfoCacheKey = new StringBuilder(CacheConstants.TASK_INFO);
		taskInfoCacheKey.append("run_now_notify_").append(this.getId()) ;
		return taskInfoCacheKey.toString();
	}
	/**
	 * 是否允许非阻塞执行
	 * @return
	 */
	public boolean isConcurrentExecutionAllow() {
		if(null==execType){
			return false ;
		}
		if(execType==1){
			return true ;
		}
		return false;
	}
	
}

