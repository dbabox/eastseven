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
 *         create time : 2009-10-23 ����12:05:20<br>
 *         ����������һ�μ���ȫ��ץȡ��ַ<br>
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
			message = sdf.format(start)+" : ץȡ[v:"+version+"]��ʼ";
			System.out.println(message);
			log.info(message);
			//����ץȡ��ַ
			DaoService daoService = new DaoServiceDefaultImpl();
			List<CrawlList> list = daoService.getCrawlLists();
			if(list!=null && !list.isEmpty()){
				
				message = sdf.format(new Date())+" : ץȡ[v:"+version+"]��ַ��"+list.size()+"��";
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
					
					message = sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��ʼ";
					System.out.println(message);
					log.info(message);
					
					if(url.contains("lottery.sports.sohu.com")){
						//TODO ��Բ�Ʊ���⴦��
						folders = webCrawlerLottery.crawl(channelId.toString(), url);
					}else if(url.contains("astro.women.sohu.com")){
						//TODO �������ռ�����⴦��
						folders = webCrawlerAstro.crawl(channelId.toString(), url);
					}else{
						//TODO ͨ��Ƶ������
						folders = webCrawler.crawl(channelId.toString(), url);
					}
					
					if(folders!=null && !folders.isEmpty()){
						boolean bln = daoService.saveCrawlResource(folders);
						if(bln) count += folders.size();
						
						message = sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��"+folders.size()+"����¼��������"+(bln?"�ɹ�":"ʧ��");
						System.out.println(message);
						log.info(message);
						
					}else{
						message = sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��Դ���󼯺�Ϊ��";
						System.out.println(message);
						log.info(message);
					}
				}
			}else{
				message = sdf.format(new Date())+" : ץȡ[v:"+version+"]��ַδȡ��";
				System.out.println(message);
				log.info(message);
			}
			
			Date end = new Date(System.currentTimeMillis());
			double minutes = (double)((end.getTime()-start.getTime())/(60*1000));
			
			message = sdf.format(end)+" : ץȡ[v:"+version+"]���,��ʱ"+minutes+"��,���������:"+count+"��,�´�ִ��ʱ��:"+sdf.format(context.getNextFireTime());
			System.out.println(message);
			log.info(message);
			
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			message = sdf.format(new Date())+" : ץȡ[v:"+version+"]ʧ��";
			System.out.println(message);
			log.error(e);
		}

	}

}
