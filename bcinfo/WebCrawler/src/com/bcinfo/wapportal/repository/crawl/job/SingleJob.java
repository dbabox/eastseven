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
 *         create time : 2009-10-23 ����12:05:20<br>
 *         ����������һ�μ���ȫ��ץȡ��ַ<br>
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
			System.out.println(sdf.format(start)+" : ץȡ[v:"+version+"]��ʼ");
			
			//����ץȡ��ַ
			DaoService daoService = new DaoServiceDefaultImpl();
			List<CrawlList> list = daoService.getCrawlLists();
			if(list!=null && !list.isEmpty()){
				System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]��ַ��"+list.size()+"��");
				Long channelId = null;
				String url = null;
				WebCrawler webCrawler = new WebCrawlerDefaultImpl();
				for(CrawlList obj : list){
					channelId = obj.getChannelId();
					url = obj.getCrawlUrl();
					System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��ʼ");
					List<FolderBO> folders = webCrawler.crawl(channelId.toString(), url);
					if(folders!=null && !folders.isEmpty()){
						boolean bln = daoService.saveCrawlResource(folders);
						if(bln) count += folders.size();
						System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��"+folders.size()+"����¼��������"+(bln?"�ɹ�":"ʧ��"));
					}else{
						System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]Ƶ��["+channelId+"]��ַ["+url+"]��Դ���󼯺�Ϊ��");
					}
				}
			}else{
				System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]��ַδȡ��");
			}
			
			Date end = new Date(System.currentTimeMillis());
			double minutes = (double)((end.getTime()-start.getTime())/(60*1000));
			System.out.println(sdf.format(end)+" : ץȡ[v:"+version+"]���,��ʱ"+minutes+"��,���������:"+count+"��,�´�ִ��ʱ��:"+sdf.format(context.getNextFireTime()));
			
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			System.out.println(sdf.format(new Date())+" : ץȡ[v:"+version+"]ʧ��");
		}

	}

}
