/**
 * 
 */
package com.bcinfo.job;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.job.service.ParseService;


/**
 * @author dongq
 * 
 *         create time : 2010-1-20 上午08:59:22
 */
public final class Main {

	private static final Logger log = Logger.getLogger(Main.class);

	public static final String CONFIG_PATH = System.getProperties().getProperty("user.dir") + "/conf/";
	
	public static void main(String[] args) {
		try {
			init();
			start();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	static void init() throws Exception {
		String configFilename = CONFIG_PATH + "log4j.properties";
		PropertyConfigurator.configure(configFilename);
		log.info("加载日志文件");
		Properties property = new Properties();
		property.load(new FileInputStream(CONFIG_PATH + "config.properties"));
		File root = new File(property.getProperty("excel.file.path"));
		if(root.isDirectory()){
			String name = root.getAbsolutePath()+"/success";
			File dir = new File(name);
			if(!dir.exists()){
				log.info("创建success文件夹"+(dir.mkdirs()?"成功":"失败"));
			}
			name = property.getProperty("excel.file.path")+"/fail";
			dir = new File(name);
			if(!dir.exists()){
				log.info("创建fail文件夹"+(dir.mkdirs()?"成功":"失败"));
			}
		}
	}
	
	static void start() throws Exception {
		//CronExp
		Properties property = new Properties();
		property.load(new FileInputStream(CONFIG_PATH + "config.properties"));
		String cronExpression = property.getProperty("parse.time.cron.expression");
		//启动Job
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = new JobDetail("ParseExcelJob", Scheduler.DEFAULT_GROUP, ParseService.class);
		Trigger trigger = new CronTrigger("ParseExcelTrigger", Scheduler.DEFAULT_GROUP, cronExpression);
		Date date = scheduler.scheduleJob(job, trigger);
		log.info("解析开始时间："+date);
		scheduler.start();
		log.info("启动...");
	}
}
