/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.job.MonitorJob;
import com.bcinfo.wapportal.repository.crawl.job.ParseJob;
import com.bcinfo.wapportal.repository.crawl.util.ConfigPropertyUtil;

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
		try{
			PropertyConfigurator.configure(new ConfigPropertyUtil().getConfigProperty("log4j.properties"));
			log.info("日志文件加载成功");
		}catch(Exception e){
			log.error(e);
			log.error("日志文件加载失败");
		}
		
		try{
			//初始化内存数据库
			DaoService dao = new DaoService();
			boolean bln = dao.isTableExist("internal_file_log");
			if(!bln){
				log.info("初始化内存数据库");
				bln = dao.createInternalFileLogTable();
				log.info("内存数据库创建："+(bln?"成功":"失败"));
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("初始化内存数据库出错");
		}
		
		try{
			System.out.println(sdf.format(new Date())+" 本地服务启动... ");
			log.info(" 本地服务启动... ");
			//创建并启动调度线程
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//解析Job,每1分钟解析一次
			JobDetail parseJob = new JobDetail("parseJob", Scheduler.DEFAULT_GROUP, ParseJob.class);
			Trigger trigger_1 = new SimpleTrigger("trigger_1", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 1 * 60 * 1000L);
			scheduler.scheduleJob(parseJob, trigger_1);
			//监控Job,每60分钟扫描一次,目前未使用
			JobDetail monitorJob = new JobDetail("monitorJob", Scheduler.DEFAULT_GROUP, MonitorJob.class);
			Trigger trigger_60 = null;
			trigger_60 = new SimpleTrigger("trigger_60", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 60 * 60 * 1000L);
			//每天凌晨3点执行
			//trigger_60 = new CronTrigger("monitorTrigger", Scheduler.DEFAULT_GROUP, "0 0 3 ? * *");
			scheduler.scheduleJob(monitorJob, trigger_60);
			
			scheduler.start();
			System.out.println(sdf.format(new Date())+" 本地服务启动完毕... ");
			log.info(" 本地服务启动完毕... ");
		}catch(Exception e){
			System.out.println(sdf.format(new Date())+" 本地服务启动失败 ");
			log.info(" 本地服务启动失败 ");
			log.error(e);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
