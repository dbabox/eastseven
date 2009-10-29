/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 ����09:52:06<br>
 *         ����ɨ����������Դ�ļ���������ɾ��
 */
public class MonitorJob implements Job {

	private static Logger log = Logger.getLogger(MonitorJob.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("ɨ�迪ʼ...");
		try{
			
		}catch(Exception e){
			System.out.println(" ����ɨ��ʧ��... ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
