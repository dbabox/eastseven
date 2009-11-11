/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.job.WeeklyJob;

/**
 * @author dongq
 * 
 * create time : 2009-11-9 ÏÂÎç01:33:29
 */
public class CronTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();
		JobDetail job = new JobDetail("test", Scheduler.DEFAULT_GROUP, WeeklyJob.class);
		Trigger trigger = new CronTrigger("trigger", Scheduler.DEFAULT_GROUP, "0 0 0 ? * MON");
		Date date = scheduler.scheduleJob(job, trigger);
		System.out.println(date);
		scheduler.start();
	}

}
