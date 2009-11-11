/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;

/**
 * @author dongq
 * 
 *         create time : 2009-11-9 下午01:26:50<br>
 *         每周执行一次，删除上周之前的未审核数据
 */
public class WeeklyJob implements Job {
	
	private static Logger log = Logger.getLogger(WeeklyJob.class);
	
	private DaoService daoService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String message = "";
		
		try{
			message = "数据清理开始";
			log.info(message);
			
			daoService = new DaoServiceDefaultImpl();
			boolean bln = daoService.clearCrawlResource();
			message = "数据清理"+(bln?"成功":"失败");
			log.info(message);
			
			message = "数据清理结束";
			log.info(message);
		}catch(Exception e){
			message = "数据清理失败";
			log.info(message);
			log.error(message);
			log.error(e);
			if(log.isDebugEnabled())
				e.printStackTrace();
		}
	}

}
