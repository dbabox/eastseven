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
 *         create time : 2009-11-9 ����01:26:50<br>
 *         ÿ��ִ��һ�Σ�ɾ������֮ǰ��δ�������
 */
public class WeeklyJob implements Job {
	
	private static Logger log = Logger.getLogger(WeeklyJob.class);
	
	private DaoService daoService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String message = "";
		
		try{
			message = "��������ʼ";
			log.info(message);
			
			daoService = new DaoServiceDefaultImpl();
			boolean bln = daoService.clearCrawlResource();
			message = "��������"+(bln?"�ɹ�":"ʧ��");
			log.info(message);
			
			message = "�����������";
			log.info(message);
		}catch(Exception e){
			message = "��������ʧ��";
			log.info(message);
			log.error(message);
			log.error(e);
			if(log.isDebugEnabled())
				e.printStackTrace();
		}
	}

}
