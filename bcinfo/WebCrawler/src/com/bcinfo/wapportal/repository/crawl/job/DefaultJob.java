/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-9-29 下午12:43:30
 */
public class DefaultJob implements Job {

	private static Logger log = Logger.getLogger(DefaultJob.class);
	
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	public DefaultJob() {
		//System.out.println(this.getClass().getName());
	}
	
	WebCrawler webCrawler = new WebCrawlerDefaultImpl();
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String folderId = context.getJobDetail().getJobDataMap().getString("folderId");
		String url = context.getJobDetail().getJobDataMap().getString("url");
		try{
			List<FolderBO> folders = webCrawler.crawl(folderId, url);
			DaoService daoService = new DaoServiceDefaultImpl();
			Boolean bln = daoService.saveCrawlResource(folders);
			
			System.out.println(sdf.format(new Date())+"  url: "+url+" 共爬取 "+folders.size()+" 条记录,入库操作："+bln);
			
			System.out.println("  程序运行中的内存情况： "+url+" | "+folderId);
			System.out.println("  Total:"+Runtime.getRuntime().totalMemory()/1024);
			System.out.println("  Free :"+Runtime.getRuntime().freeMemory()/1024);
			System.out.println("  Max  :"+Runtime.getRuntime().maxMemory()/1024);
			System.out.println(" ");
			
		}catch(Exception e){
			System.out.println("爬取 "+url+" 失败");
			if(log.isDebugEnabled()) log.debug(e);
		}
		
	}

}
