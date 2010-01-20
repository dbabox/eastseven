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
 *         create time : 2010-1-20 ����08:59:22
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
		log.info("������־�ļ�");
		Properties property = new Properties();
		property.load(new FileInputStream(CONFIG_PATH + "config.properties"));
		File root = new File(property.getProperty("excel.file.path"));
		if(root.isDirectory()){
			String name = root.getAbsolutePath()+"/success";
			File dir = new File(name);
			if(!dir.exists()){
				log.info("����success�ļ���"+(dir.mkdirs()?"�ɹ�":"ʧ��"));
			}
			name = property.getProperty("excel.file.path")+"/fail";
			dir = new File(name);
			if(!dir.exists()){
				log.info("����fail�ļ���"+(dir.mkdirs()?"�ɹ�":"ʧ��"));
			}
		}
	}
	
	static void start() throws Exception {
		//CronExp
		Properties property = new Properties();
		property.load(new FileInputStream(CONFIG_PATH + "config.properties"));
		String cronExpression = property.getProperty("parse.time.cron.expression");
		//����Job
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = new JobDetail("ParseExcelJob", Scheduler.DEFAULT_GROUP, ParseService.class);
		Trigger trigger = new CronTrigger("ParseExcelTrigger", Scheduler.DEFAULT_GROUP, cronExpression);
		Date date = scheduler.scheduleJob(job, trigger);
		log.info("������ʼʱ�䣺"+date);
		scheduler.start();
		log.info("����...");
	}
}
