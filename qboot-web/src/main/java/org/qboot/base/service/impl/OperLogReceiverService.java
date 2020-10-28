package org.qboot.base.service.impl;

import org.qboot.base.dao.SysOperateLogDao;
import org.qboot.base.dto.SysOperateLogDto;
import org.qboot.common.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>Title: OperLogReceiverService</p>
 * <p>Description: 操作日志接收服务</p>
 * @author history
 * @date 2018-08-08
 */
@Service("operLogReceiverService")
public class OperLogReceiverService extends CrudService<SysOperateLogDao, SysOperateLogDto> {
	
    private static Logger logger = LoggerFactory.getLogger(OperLogReceiverService.class);
    
    @PostConstruct 
    public void init(){
    	ExecutorService threadPool = Executors.newCachedThreadPool();
    	threadPool.execute(new LogConsumer());
    }
    
    @Autowired
	private SysOperateLogService operationLogService;
	
	/**
	 * 接收队最大长度阀值
	 */
    private static int MAX_QUEUE_SIZE = 10000;
    /**
     * 日志临时队列
     */
 	private static BlockingQueue<SysOperateLogDto> logQueue = new LinkedBlockingQueue<SysOperateLogDto>(MAX_QUEUE_SIZE);
 	/**
 	 * 日志批量入库条数
 	 */
 	private static int batchInsertSize = 100;
    /**
     * 日志入库线程池
     */
 	private static ThreadPoolExecutor fixedPool = null;
	 /**
	  * 批量插入队列容量
	  */
 	private static int queueCapacity = 500; 
 	/**
 	 *  并发线程
 	 */
 	private static int concurrentNum = 5;  
 	
 	static {
 		try {
			fixedPool =  new ThreadPoolExecutor(concurrentNum, concurrentNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity) , new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					logger.error("Exceeded the queue of batch insert capacity...");
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}  
 	}
	
	public void receive(SysOperateLogDto operationLog){
		// 过载预警
		if(logQueue.remainingCapacity() <= MAX_QUEUE_SIZE * 0.3){
			logger.warn("The number of operation logs exceeds the security value!");
		}
		
		if( !logQueue.offer(operationLog)) {  
			// 是否放入成功
			logger.error("Log data is out of capacity!");
		}
	}
	
	/**
	 * 内部类：操作日志消费
	 * @author history
	 * @date 2017-12-08
	 */
	class LogConsumer implements Runnable{
		@Override
		public void run() {
			 // 是否批量入库
			boolean isExec = false;
			// 批量插入List
			List<SysOperateLogDto> operLogList = new ArrayList<SysOperateLogDto>(); 
			SysOperateLogDto operationLogDto = null;
			long sleepSeconds = 0L;
			while(true){
				operationLogDto = logQueue.poll();
				if (operationLogDto != null) {
					operLogList.add(operationLogDto);
				}

				// 如果队列中没有日志，休眠五秒
				if (operationLogDto == null) { 
					try {
						TimeUnit.SECONDS.sleep(5);
						sleepSeconds +=5;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				
				if(sleepSeconds >= 60){
					sleepSeconds = 0L;
					isExec = true;
				}
				//日志满了阀值或者等待时间1分钟入库一次
				if(isExec || operLogList.size() >= batchInsertSize) {
					fixedPool.execute(new LogBatchInsertTask(operLogList));
					operLogList = new ArrayList<>();
					isExec = false;
				}
			}
		}
	}
	
	
	/**
	 * 内部类：操作日志入库任务
	 * @author history
	 * @date 2017-12-05
	 */
	class LogBatchInsertTask implements Runnable{
		/**
		 * 操作日志
		 */
	    private List<SysOperateLogDto> operLogList; 
	     
	    public LogBatchInsertTask(List<SysOperateLogDto> operLogList){
	        this.operLogList = operLogList;
	    }
	    
	    @Override
	    public void run() {
	    	operationLogService.batchInsertOperationLog(operLogList);
	    }
	}
	
}



