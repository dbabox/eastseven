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
 *         create time : 2009-10-21 ����09:29:48<br>
 *         ���ط���<br>
 *         ���ڽ������ķ��͹�������Դ�ļ������������
 */
public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	public static void main(String[] args) {
		try{
			PropertyConfigurator.configure(new ConfigPropertyUtil().getConfigProperty("log4j.properties"));
			log.info("��־�ļ����سɹ�");
		}catch(Exception e){
			log.error(e);
			log.error("��־�ļ�����ʧ��");
		}
		
		try{
			//��ʼ���ڴ����ݿ�
			DaoService dao = new DaoService();
			boolean bln = dao.isTableExist("internal_file_log");
			if(!bln){
				log.info("��ʼ���ڴ����ݿ�");
				bln = dao.createInternalFileLogTable();
				log.info("�ڴ����ݿⴴ����"+(bln?"�ɹ�":"ʧ��"));
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("��ʼ���ڴ����ݿ����");
		}
		
		try{
			System.out.println(sdf.format(new Date())+" ���ط�������... ");
			log.info(" ���ط�������... ");
			//���������������߳�
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//����Job,ÿ1���ӽ���һ��
			JobDetail parseJob = new JobDetail("parseJob", Scheduler.DEFAULT_GROUP, ParseJob.class);
			Trigger trigger_1 = new SimpleTrigger("trigger_1", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 1 * 60 * 1000L);
			scheduler.scheduleJob(parseJob, trigger_1);
			//���Job,ÿ60����ɨ��һ��,Ŀǰδʹ��
			JobDetail monitorJob = new JobDetail("monitorJob", Scheduler.DEFAULT_GROUP, MonitorJob.class);
			Trigger trigger_60 = null;
			trigger_60 = new SimpleTrigger("trigger_60", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 60 * 60 * 1000L);
			//ÿ���賿3��ִ��
			//trigger_60 = new CronTrigger("monitorTrigger", Scheduler.DEFAULT_GROUP, "0 0 3 ? * *");
			scheduler.scheduleJob(monitorJob, trigger_60);
			
			scheduler.start();
			System.out.println(sdf.format(new Date())+" ���ط����������... ");
			log.info(" ���ط����������... ");
		}catch(Exception e){
			System.out.println(sdf.format(new Date())+" ���ط�������ʧ�� ");
			log.info(" ���ط�������ʧ�� ");
			log.error(e);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
