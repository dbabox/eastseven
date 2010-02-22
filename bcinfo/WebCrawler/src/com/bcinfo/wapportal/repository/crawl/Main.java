/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.core.CacheQueue;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;
import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.job.DefaultJob;
import com.bcinfo.wapportal.repository.crawl.job.SingleJob;
import com.bcinfo.wapportal.repository.crawl.job.WeeklyJob;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午09:43:54<br>
 *         
 */
public final class Main {

	private static Logger log = Logger.getLogger(Main.class);
	private static final long repeatInterval = 60 * 60 * 1000L;

	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	/**
	 * 入口
	 * @param args
	 */
	public static void main(String[] args) {
		new Main().start();
	}

	public void start() {
		System.out.println("  程序开始前的内存情况： ");
		System.out.println("  Total:"+Runtime.getRuntime().totalMemory()/1024);
		System.out.println("  Free :"+Runtime.getRuntime().freeMemory()/1024);
		System.out.println("  Used :"+(Runtime.getRuntime().totalMemory()/1024-Runtime.getRuntime().freeMemory()/1024));
		System.out.println("  Max  :"+Runtime.getRuntime().maxMemory()/1024);
		System.out.println("  Queue:"+CacheQueue.getQueue().size());
		System.out.println("  ");
		try{
			//Init log4j
			PropertyConfigurator.configure(ConfigPropertyUtil.getLog4jProperty());
			log.info("加载日志文件成功");
			//Init Quartz Database
			initQuartzDatabase();
			log.info("初始化Quartz数据库成功");
			//Init Quartz Framework
			SchedulerFactory factory = new StdSchedulerFactory(ConfigPropertyUtil.getConfigProperty("quartz.properties"));
			Scheduler scheduler = factory.getScheduler();
			scheduler.start();
			log.info("Quartz启动成功");
			/**/
			DaoService dao = new DaoServiceDefaultImpl();
			//从数据库中读取要爬取的地址列表
			List<CrawlList> list = null;
			list = dao.getCrawlLists("1");
			if(list != null && !list.isEmpty()){
				Trigger trigger = null;
				for(int index=0;index<list.size();index++){
					//TODO 一个URL一个Job&Trigger
					CrawlList info = (CrawlList)list.get(index);
					String folderId = info.getChannelId().toString();
					
					String jobName = info.getCrawlId().toString();
					JobDetail job = new JobDetail(jobName, folderId, SingleJob.class);
					job.setDescription(info.getCrawlUrl());
					job.getJobDataMap().put("crawlList", info);
					
					Date start = new Date(System.currentTimeMillis() + ((index + 1) * 1000L));
					String triName = jobName;
					trigger = new SimpleTrigger(triName, folderId, start, null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
					//trigger = new CronTrigger(triName, folderId, "59 1/59 * * * ?");
					trigger.setDescription(info.getCrawlUrl());
					
					scheduler.scheduleJob(job, trigger);
				}
				
				System.out.println( "  Quartz调度运行  ");
			}
			
			//守护线程
			JobDetail monitorJob = new JobDetail("monitor_job", "monitor_group", DefaultJob.class);
			Trigger trigger = new CronTrigger("monitor_trigger", "monitor_group", "0/59 * * * * ?");
			scheduler.scheduleJob(monitorJob, trigger);
			
			//数据清理
			monitorJob = new JobDetail("monitor_job_del", "monitor_group", WeeklyJob.class);
			trigger = new CronTrigger("monitor_trigger_del", "monitor_group", "59 0 1 * * ?");
			scheduler.scheduleJob(monitorJob, trigger);
			
		}catch(Exception e){
			log.error("启动失败："+e);
		}finally{
			log.info("处理完毕");
		}
	}
	
	private void initQuartzDatabase() {
		try {
			//TODO 在应用程序中调用ANT任务
			/*
			String buildFile = System.getProperties().getProperty("user.dir")+"/config/sql.xml";
			log.info("Ant脚本文件位于："+buildFile);
			Project project = new Project();
			project.setBasedir(System.getProperties().getProperty("user.dir"));
			File build = new File(buildFile);
			project.init();
			ProjectHelper helper = ProjectHelperImpl.getProjectHelper();
			helper.parse(project, build);
			project.executeTarget(project.getDefaultTarget());
			*/
			String sqlFile = System.getProperties().getProperty("user.dir")+"/config/tables_oracle.sql";
			boolean bln = new DaoServiceDefaultImpl().initQuartzDatabase(sqlFile);
			log.info("初始化Quartz数据库创建"+(bln?"成功":"失败"));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("初始化Quartz数据库失败："+e);
		}
	}

}
