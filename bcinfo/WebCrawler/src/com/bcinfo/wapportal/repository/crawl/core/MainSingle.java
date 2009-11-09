/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.job.SingleJob;

/**
 * @author dongq
 * 
 *         create time : 2009-10-23 ����12:09:10<br>
 *         ֻ����һ��Job���ȣ��������ݿⲢ������
 */
public class MainSingle {

	private static Logger log = Logger.getLogger(MainSingle.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	public static void main(String[] args) {
		System.out.println("  ����ʼǰ���ڴ������ ");
		System.out.println("  Total:"+Runtime.getRuntime().totalMemory()/1024);
		System.out.println("  Free :"+Runtime.getRuntime().freeMemory()/1024);
		System.out.println("  Max  :"+Runtime.getRuntime().maxMemory()/1024);
		System.out.println(" ");
		
		try{
			Properties properties = null;
			properties = ConfigPropertyUtil.getLog4jProperty();
			if(properties != null)
				PropertyConfigurator.configure(properties);
			
		}catch(Exception e){
			System.out.println("����log4j.properties�ļ�ʧ��");
		}
		
		try{
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			JobDetail job = new JobDetail("singleJob", Scheduler.DEFAULT_GROUP, SingleJob.class);
			long repeatInterval = 60 * 60 * 1000L;//TODO 60����һ��
			Trigger trigger = new SimpleTrigger("singleTrigger", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
			scheduler.scheduleJob(job, trigger);
			
			scheduler.start();
			System.out.println(sdf.format(new Date())+" : �������� ");
			
			//scheduler.shutdown();
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			System.out.println(sdf.format(new Date())+" : ��������ʧ�� ");
		}
	}

}
