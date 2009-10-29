/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.job.MonitorJob;
import com.bcinfo.wapportal.repository.crawl.job.ParseJob;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 ����09:29:48<br>
 *         ���ط���<br>
 *         ���ڽ������ķ��͹�������Դ�ļ������������
 */
public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	public static void main(String[] args) {
		System.out.println("******************************");
		Map<String, String> sys = System.getenv();
		Set<String> key = sys.keySet();
		for(String k : key){
			System.out.println(k+" : "+sys.get(k));
		}
		System.out.println("******************************");
		try{
			System.out.println(sdf.format(new Date())+" ���ط�������... ");
			//���������������߳�
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//����Job,ÿ5���ӽ���һ��
			JobDetail parseJob = new JobDetail("parseJob", Scheduler.DEFAULT_GROUP, ParseJob.class);
			Trigger trigger_5 = new SimpleTrigger("trigger_5", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 5 * 60 * 1000L);
			scheduler.scheduleJob(parseJob, trigger_5);
			//���Job,ÿ60����ɨ��һ��
			JobDetail monitorJob = new JobDetail("monitorJob", Scheduler.DEFAULT_GROUP, MonitorJob.class);
			Trigger trigger_60 = new SimpleTrigger("trigger_60", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 60 * 60 * 1000L);
			scheduler.scheduleJob(monitorJob, trigger_60);
			
			scheduler.start();
			System.out.println(sdf.format(new Date())+" ���ط����������... ");
		}catch(Exception e){
			System.out.println(sdf.format(new Date())+" ���ط�������ʧ�� ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
