package org.qboot.common.task.job;

import org.quartz.Job;

/**
 * 允许非阻塞执行
 * @author iscast
 * @date 2020-09-25
 */
public class AllowConcurrentExecutionJob extends JobProxy implements Job {
	
}
