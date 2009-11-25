/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;
import com.bcinfo.wapportal.repository.crawl.service.impl.CrawlResourceServiceDefaultImpl;

/**
 * @author dongq
 * 
 *         create time : 2009-11-24 下午05:02:05
 */
public class AutoSendJob implements Job {

	private static final Logger log = Logger.getLogger(AutoSendJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try{
			CrawlResourceService service = new CrawlResourceServiceDefaultImpl();
			boolean bln = service.sendResourceAuto();
			if(bln){
				log.info("自动发送成功");
			}else{
				log.info("自动发送失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.info("自动发送报错");
		}
	}

}
