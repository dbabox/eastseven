///**
// * 
// */
package com.bcinfo.wapportal.repository.crawl.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.core.CacheQueue;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerAstroSohuImpl;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerLotteryImpl;
import com.bcinfo.wapportal.repository.crawl.core.impl.WebCrawlerMobileZhutiImpl;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;
//
///**
// * @author dongq
// * 
// *         create time : 2009-10-23 下午12:05:20<br>
// *         单调度任务，一次加载全部抓取地址<br>
// */
public class SingleJob implements Job {

	private static Logger log = Logger.getLogger(SingleJob.class);

	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		String message = "";
		Date start = new Date(System.currentTimeMillis());
		String version = new SimpleDateFormat("yyMMddHHmmss").format(start);
		try {
			message = "抓取[v:" + version + "]开始";
			log.info(message);
			// 加载抓取地址
			List<CrawlList> list = new ArrayList<CrawlList>();
			list.add((CrawlList) context.getJobDetail().getJobDataMap().get("crawlList"));
			message = "抓取[v:" + version + "]地址共" + list.size() + "条";
			log.info(message);

			Long channelId = null;
			String url = null;
			WebCrawler webCrawler = new WebCrawlerDefaultImpl();
			WebCrawler webCrawlerLottery = new WebCrawlerLotteryImpl();
			WebCrawler webCrawlerAstro = new WebCrawlerAstroSohuImpl();
			WebCrawler webCrawlerMobileZhuti = new WebCrawlerMobileZhutiImpl();
			int size = 0;
			for (CrawlList obj : list) {
				List<FolderBO> folders = new ArrayList<FolderBO>();

				Long id = obj.getCrawlId();
				channelId = obj.getChannelId();
				url = obj.getCrawlUrl();

				message = "抓取[v:" + version + "]频道[" + channelId + "]地址[" + url + "]开始";
				log.info(message);

//				if (url.contains("lottery.sports.sohu.com")) {
//					// TODO 针对彩票特殊处理
//					folders = webCrawlerLottery.crawl(id, channelId.toString(), url);
//				} else if (url.contains("astro.women.sohu.com")) {
//					// TODO 针对星座占卜特殊处理
//					folders = webCrawlerAstro.crawl(id, channelId.toString(), url);
//				} else if (url.contains("www.moxiu.com") 
//						|| url.contains("www.izhuti.com")
//						|| url.contains("soft.tompda.com")
//						|| url.contains("sj.skycn.")
//						|| url.contains("www.3g37.com")) {
//					// TODO 针对手机主题软件下载处理
//					folders = webCrawlerMobileZhuti.crawl(id, channelId.toString(), url);
//				} else {
//					// TODO 通用频道处理
//					folders = webCrawler.crawl(id, channelId.toString(), url);
//				}
				if (folders != null && !folders.isEmpty()) {
					message = "抓取[v:" + version + "]频道[" + channelId + "]地址[" + url + "]共" + folders.size() + "条记录";
					log.info(message);
					new DaoServiceDefaultImpl().saveLog(new AppLog(message, channelId, url, Long.valueOf(folders.size())));
					CacheQueue.getQueue().addAll(folders);
					size = folders.size();
				} else {
					message = "抓取[v:" + version + "]频道[" + channelId + "]地址[" + url + "]共" + 0 + "条记录";
					log.info(message);
				}
				
				log.info("CacheQueue:[" + size + "," + CacheQueue.getQueue().size() + "][" + url + "]");

			}

		} catch (Exception e) {
			if (log.isDebugEnabled()) e.printStackTrace();
			message = sdf.format(new Date()) + " : 抓取[v:" + version + "]失败";
			log.error(e);
			if (e instanceof NullPointerException) {
				e.printStackTrace();
			}
		}

	}

	@Deprecated
	boolean filterHandle(List<FolderBO> folders){
		boolean bln = false;
		
		try {
			
		} catch (Exception e) {
			
		}
		
		return bln;
	}
}
