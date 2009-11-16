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
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerAstroSohuImpl;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerLotteryImpl;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-23 下午12:05:20<br>
 *         单调度任务，一次加载全部抓取地址<br>
 */
public class SingleJob implements Job {

	private static Logger log = Logger.getLogger(SingleJob.class);

	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String message = "";
		Date start = new Date(System.currentTimeMillis());
		String version = new SimpleDateFormat("yyMMddHHmmss").format(start);
		int count = 0;
		try{
			message = sdf.format(start)+" : 抓取[v:"+version+"]开始";
			System.out.println(message);
			log.info(message);
			//加载抓取地址
			DaoService daoService = new DaoServiceDefaultImpl();
			List<CrawlList> list = daoService.getCrawlLists();
			if(list!=null && !list.isEmpty()){
				
				message = sdf.format(new Date())+" : 抓取[v:"+version+"]地址共"+list.size()+"条";
				System.out.println(message);
				log.info(message);
				
				Long channelId = null;
				String url = null;
				WebCrawler webCrawler = new WebCrawlerDefaultImpl();
				WebCrawler webCrawlerLottery = new WebCrawlerLotteryImpl();
				WebCrawler webCrawlerAstro = new WebCrawlerAstroSohuImpl();
				for(CrawlList obj : list){
					List<FolderBO> folders = null;

					channelId = obj.getChannelId();
					url = obj.getCrawlUrl();
					
					message = sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]开始";
					System.out.println(message);
					log.info(message);
					
					if(url.contains("lottery.sports.sohu.com")){
						//TODO 针对彩票特殊处理
						folders = webCrawlerLottery.crawl(channelId.toString(), url);
					}else if(url.contains("astro.women.sohu.com")){
						//TODO 针对星座占卜特殊处理
						folders = webCrawlerAstro.crawl(channelId.toString(), url);
					}else{
						//TODO 通用频道处理
						folders = webCrawler.crawl(channelId.toString(), url);
					}
					
					if(folders!=null && !folders.isEmpty()){
						boolean bln = daoService.saveCrawlResource(folders);
						if(bln) count += folders.size();
						
						message = sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]共"+folders.size()+"条记录，入库操作"+(bln?"成功":"失败");
						System.out.println(message);
						log.info(message);
						
					}else{
						message = sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]资源对象集合为空";
						System.out.println(message);
						log.info(message);
					}
				}
			}else{
				message = sdf.format(new Date())+" : 抓取[v:"+version+"]地址未取到";
				System.out.println(message);
				log.info(message);
			}
			
			Date end = new Date(System.currentTimeMillis());
			double minutes = (double)((end.getTime()-start.getTime())/(60*1000));
			
			message = sdf.format(end)+" : 抓取[v:"+version+"]完成,耗时"+minutes+"分,共入库数据:"+count+"条,下次执行时间:"+sdf.format(context.getNextFireTime());
			System.out.println(message);
			log.info(message);
			
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			message = sdf.format(new Date())+" : 抓取[v:"+version+"]失败";
			System.out.println(message);
			log.error(e);
		}

	}

}
