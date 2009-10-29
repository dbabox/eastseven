/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 上午09:52:06<br>
 *         负责扫描已入库的资源文件，并将其删除
 */
public class MonitorJob implements Job {

	private static Logger log = Logger.getLogger(MonitorJob.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("扫描开始...");
		try{
			
		}catch(Exception e){
			System.out.println(" 本地扫描失败... ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
