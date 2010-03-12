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
// *         create time : 2009-10-23 ����12:05:20<br>
// *         ����������һ�μ���ȫ��ץȡ��ַ<br>
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
			message = "ץȡ[v:" + version + "]��ʼ";
			log.info(message);
			// ����ץȡ��ַ
			List<CrawlList> list = new ArrayList<CrawlList>();
			list.add((CrawlList) context.getJobDetail().getJobDataMap().get("crawlList"));
			message = "ץȡ[v:" + version + "]��ַ��" + list.size() + "��";
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

				message = "ץȡ[v:" + version + "]Ƶ��[" + channelId + "]��ַ[" + url + "]��ʼ";
				log.info(message);

//				if (url.contains("lottery.sports.sohu.com")) {
//					// TODO ��Բ�Ʊ���⴦��
//					folders = webCrawlerLottery.crawl(id, channelId.toString(), url);
//				} else if (url.contains("astro.women.sohu.com")) {
//					// TODO �������ռ�����⴦��
//					folders = webCrawlerAstro.crawl(id, channelId.toString(), url);
//				} else if (url.contains("www.moxiu.com") 
//						|| url.contains("www.izhuti.com")
//						|| url.contains("soft.tompda.com")
//						|| url.contains("sj.skycn.")
//						|| url.contains("www.3g37.com")) {
//					// TODO ����ֻ�����������ش���
//					folders = webCrawlerMobileZhuti.crawl(id, channelId.toString(), url);
//				} else {
//					// TODO ͨ��Ƶ������
//					folders = webCrawler.crawl(id, channelId.toString(), url);
//				}
				if (folders != null && !folders.isEmpty()) {
					message = "ץȡ[v:" + version + "]Ƶ��[" + channelId + "]��ַ[" + url + "]��" + folders.size() + "����¼";
					log.info(message);
					new DaoServiceDefaultImpl().saveLog(new AppLog(message, channelId, url, Long.valueOf(folders.size())));
					CacheQueue.getQueue().addAll(folders);
					size = folders.size();
				} else {
					message = "ץȡ[v:" + version + "]Ƶ��[" + channelId + "]��ַ[" + url + "]��" + 0 + "����¼";
					log.info(message);
				}
				
				log.info("CacheQueue:[" + size + "," + CacheQueue.getQueue().size() + "][" + url + "]");

			}

		} catch (Exception e) {
			if (log.isDebugEnabled()) e.printStackTrace();
			message = sdf.format(new Date()) + " : ץȡ[v:" + version + "]ʧ��";
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
