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
		Date start = new Date(System.currentTimeMillis());
		String version = new SimpleDateFormat("yyMMddHHmmss").format(start);
		int count = 0;
		try{
			System.out.println(sdf.format(start)+" : 抓取[v:"+version+"]开始");
			
			//加载抓取地址
			DaoService daoService = new DaoServiceDefaultImpl();
			List<CrawlList> list = daoService.getCrawlLists();
			if(list!=null && !list.isEmpty()){
				System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]地址共"+list.size()+"条");
				Long channelId = null;
				String url = null;
				WebCrawler webCrawler = new WebCrawlerDefaultImpl();
				for(CrawlList obj : list){
					channelId = obj.getChannelId();
					url = obj.getCrawlUrl();
					System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]开始");
					List<FolderBO> folders = webCrawler.crawl(channelId.toString(), url);
					if(folders!=null && !folders.isEmpty()){
						boolean bln = daoService.saveCrawlResource(folders);
						if(bln) count += folders.size();
						System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]共"+folders.size()+"条记录，入库操作"+(bln?"成功":"失败"));
					}else{
						System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]频道["+channelId+"]地址["+url+"]资源对象集合为空");
					}
				}
			}else{
				System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]地址未取到");
			}
			
			Date end = new Date(System.currentTimeMillis());
			double minutes = (double)((end.getTime()-start.getTime())/(60*1000));
			System.out.println(sdf.format(end)+" : 抓取[v:"+version+"]完成,耗时"+minutes+"分,共入库数据:"+count+"条,下次执行时间:"+sdf.format(context.getNextFireTime()));
			
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			System.out.println(sdf.format(new Date())+" : 抓取[v:"+version+"]失败");
		}

	}

}
