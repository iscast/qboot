package org.qboot.common.task.job;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.qboot.base.service.impl.SysTaskService;


/**
 * 从数据库加载任务Job
 * 为保证所有节点都能及时感知到任务信息更新,我们每隔1秒从数据库重新初始化任务
 * @author history
 * @date 2018年11月8日 下午8:12:11
 */
@Component
@ConfigurationProperties(prefix="rdp.common.task")
public class LoadTaskJob {
	Logger logger = LoggerFactory.getLogger(getClass()) ;
	@Autowired
	private SysTaskService sysTaskService;
	/**
	 * 启动加载任务
	 * 0:不加载
	 * 1:加载
	 * 此配置项配置为1的节点才能参与任务竞争,配置为0,任务处理器不会初始化任务.
	 */
	private String loadTaskOnStartup = "false" ;
	/**
	 * 检测任务变更间隔时间,单位s
	 */
	private String checkUpdateInterval = "10" ;
	
	@PostConstruct
	public void execute(){
		if(this.isTaskInitializable()){
			//初始化任务调度器
			sysTaskService.initTask();
			//检测任务信息变更
			this.checkTaskUpdate();
		}else{
			logger.info("当前节点不参与任务竞争,不进行任务初始化,不执行任务.");
		}
	}
	/**
	 * 检测任务信息变更
	 */
	private void checkTaskUpdate() {
		//检测是否有任务存在变更,一旦变更及时更新quartz任务调度器
		long checkInterval = Long.parseLong(checkUpdateInterval) ;
		Thread daemon = new Thread(() -> {
			while(true){
				try {
					TimeUnit.SECONDS.sleep(checkInterval);
				} catch (InterruptedException e) {
					logger.error("休眠异常",e);
				}
				sysTaskService.reloadTask();
			}
		}) ;
		daemon.setDaemon(true);
		daemon.start();
	}
	/**
	 * 是否初始化任务
	 * @return
	 */
	private boolean isTaskInitializable() {
		return "true".equals(loadTaskOnStartup);
	}

	public String getLoadTaskOnStartup() {
		return loadTaskOnStartup;
	}

	public void setLoadTaskOnStartup(String loadTaskOnStartup) {
		this.loadTaskOnStartup = loadTaskOnStartup;
	}
	public String getCheckUpdateInterval() {
		return checkUpdateInterval;
	}
	public void setCheckUpdateInterval(String checkUpdateInterval) {
		this.checkUpdateInterval = checkUpdateInterval;
	}
}
