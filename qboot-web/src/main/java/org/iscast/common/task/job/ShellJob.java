package org.iscast.common.task.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shell脚本型任务
 * 	提供执行shell脚本的方法callScript
 * @author history
 * @date 2018年11月12日 上午10:40:48
 */
public abstract class ShellJob implements BaseJob{
	
	protected Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	/**
	 * 执行系统命令
	 * @param command 待执行的命令
	 * @return 返回结果
	 */
	protected String callScript(String command){
		return this.callScript(command, null) ;
	}
	/**
	 * 执行系统命令
	 * @param command 待执行的命令
	 * @param commandType 0:shell 1:cmd 传入空值时,执行原始command
	 * @return 返回结果
	 */
	protected String callScript(String command,Integer commandType){
		String result = "" ;
		BufferedReader input = null;
		InputStreamReader isr = null ;
		try {
			if(null!=commandType&&0==commandType){
				//shell命令
				command = "sh "+command ;
			}else if(null!=commandType&&1==commandType){
				//cmd命令
				command = "cmd /c "+command ;
			}
			Process process = Runtime.getRuntime().exec(command) ;
			isr = new InputStreamReader(process.getInputStream()) ;
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				result += line ;
			}
		}catch (Exception e){
			logger.error("脚本执行异常",e);
		}finally{
			try {
				isr.close();
			} catch (IOException e1) {
				logger.error("输入流关闭异常",e1);
			}
			try {
				input.close();
			} catch (IOException e) {
				logger.error("缓冲流关闭异常",e);
			}
		}
		return result ;
	}
}
