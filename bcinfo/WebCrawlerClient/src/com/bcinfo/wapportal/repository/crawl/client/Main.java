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
 *         create time : 2009-10-21 上午09:29:48<br>
 *         本地服务<br>
 *         用于接收中心发送过来的资源文件，并解析入库
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
			System.out.println(sdf.format(new Date())+" 本地服务启动... ");
			//创建并启动调度线程
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//解析Job,每5分钟解析一次
			JobDetail parseJob = new JobDetail("parseJob", Scheduler.DEFAULT_GROUP, ParseJob.class);
			Trigger trigger_5 = new SimpleTrigger("trigger_5", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 5 * 60 * 1000L);
			scheduler.scheduleJob(parseJob, trigger_5);
			//监控Job,每60分钟扫描一次
			JobDetail monitorJob = new JobDetail("monitorJob", Scheduler.DEFAULT_GROUP, MonitorJob.class);
			Trigger trigger_60 = new SimpleTrigger("trigger_60", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 60 * 60 * 1000L);
			scheduler.scheduleJob(monitorJob, trigger_60);
			
			scheduler.start();
			System.out.println(sdf.format(new Date())+" 本地服务启动完毕... ");
		}catch(Exception e){
			System.out.println(sdf.format(new Date())+" 本地服务启动失败 ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
