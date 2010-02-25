/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.job.SingleJob;
import com.bcinfo.wapportal.repository.crawl.job.WeeklyJob;

/**
 * @author dongq
 * 
 *         create time : 2009-10-23 下午12:09:10<br>
 *         只启动一个Job调度，减少数据库并发操作
 */
@Deprecated
public class MainSingle {

	private static Logger log = Logger.getLogger(MainSingle.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	public static void main(String[] args) {
		System.out.println("  程序开始前的内存情况： ");
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
			log.error("加载log4j.properties文件失败");
			log.error(e.getMessage());
		}
		
		try{
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//业务Job
			JobDetail job = new JobDetail("singleJob", Scheduler.DEFAULT_GROUP, SingleJob.class);
			long repeatInterval = 60 * 60 * 1000L;//TODO 60分钟一次
			Trigger trigger = new SimpleTrigger("singleTrigger", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
			scheduler.scheduleJob(job, trigger);
			
			//数据处理Job,每周一凌晨0:00:00执行
			job = new JobDetail("weeklyJob",Scheduler.DEFAULT_GROUP,WeeklyJob.class);
			trigger = new CronTrigger("cronTrigger", Scheduler.DEFAULT_GROUP, "0 0 0 ? * MON");
			scheduler.scheduleJob(job, trigger);
			
			scheduler.start();
			log.info(sdf.format(new Date())+" : 程序启动 ");
			 
			//scheduler.shutdown();
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			log.info(sdf.format(new Date())+" : 程序启动失败 ");
		}
	}

}
