package org.qboot.common.task.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;

/**
 * 不允许并发执行(阻塞)
 * @author iscast
 * @date 2020-09-25
 */
@DisallowConcurrentExecution
public class DisallowConcurrentExecutionJob extends JobProxy implements Job {
	
}
