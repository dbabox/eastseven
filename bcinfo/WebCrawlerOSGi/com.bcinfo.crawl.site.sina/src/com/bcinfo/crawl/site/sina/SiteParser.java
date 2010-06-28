/**
 * 
 */
package com.bcinfo.crawl.site.sina;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.site.service.AbstractSiteParser;
import com.bcinfo.crawl.utils.RegexUtil;

/**
 * @author dongq
 * 
 * create time : 2010-6-25 ����09:47:41
 */
public class SiteParser extends AbstractSiteParser implements Job {

	private static final Log log = LogFactory.getLog(SiteParser.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		setSite((Site)jobDataMap.get("site"));
		setWebCrawlerDao((WebCrawlerDao)jobDataMap.get("webCrawlerDao"));
		setCrawlerLogService((CrawlerLogService)jobDataMap.get("crawlerLogService"));
		run();
		log.info("["+getSite().getChannelId()+"."+getSite().getChannelName()+"-"+getSite().getName()+"]["+getSite().getUrl()+"]"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getNextFireTime()));
	}

	@Override
	public String hook(String content) {
		String cnt = "";
		try {
			cnt = new String(content);
			cnt = cnt.replaceAll(RegexUtil.STYLE, replacement)
					 .replaceAll(RegexUtil.SCRIPT, replacement)
					 .replaceAll("���ñ��������ݣ���Դ��ע�������Ĵ�", replacement)
					 .replaceAll("&nbsp", " ").replaceAll("<!-- -->", replacement)
			         .replaceAll("��һҳ\\d+��һҳ", replacement)
			         .replaceAll("<img  src=\"http://i2.sinaimg.cn/home/c.gif\" height=5 width=1 style=\"border:none;\">", replacement)
			         .replaceAll("ת��������΢��", replacement)
			         .replaceAll("��ע���»�������̬�������˿Ƽ�΢��", replacement)
			         .replaceAll("��������������Ϊ���ƾ�������Ȩ���˲ƾ�����(�ƾ����Ƽ�Ƶ��)ת�أ���δ��Ȩ�������������ת�أ��������������������ת�ع�����Ȩ�������Ը���", replacement)
			         .replaceAll("����Ϣϵת�������˺���ý�壬���������ش��ĳ��ڴ��ݸ�����Ϣ֮Ŀ�ģ�������ζ����ͬ��۵��֤ʵ���������������ݽ����ο���������Ͷ�ʽ��顣Ͷ���߾ݴ˲����������Ե���", replacement)
			         .replaceAll("�ر�˵�������ڸ���������Ĳ��ϵ�����仯�����������ṩ�����п�����Ϣ�����ο������뿼����Ȩ�����Ź�������ʽ��ϢΪ׼��", replacement)
			         .replaceAll("������Ϣ����ʣ����˸߿�Ƶ�� �߿���̳ �߿�����Ȧ���ĸ߿���Ѷ��ŷ���", replacement)
			         .replaceAll("<br />.*?\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", replacement)
			         .replaceAll(RegexUtil.COMMENT, replacement)
			         ;
			
		} catch (Exception e) {
			return content;
		}
		return cnt;
	}

}
