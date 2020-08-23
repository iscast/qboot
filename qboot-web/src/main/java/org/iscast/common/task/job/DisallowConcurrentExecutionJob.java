package org.iscast.common.task.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;

/**
 * 不允许并发执行(阻塞)
 * @author history
 * @date 2018年11月8日 下午8:07:33
 */
@DisallowConcurrentExecution
public class DisallowConcurrentExecutionJob extends JobProxy implements Job {
	
}
