/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;
import com.bcinfo.wapportal.repository.crawl.job.DefaultJob;

/**
 * @author dongq
 * 
 *         create time : 2009-10-15 ����03:13:02
 */
public final class Main {

	private static Logger log = Logger.getLogger(Main.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	/**
	 * ���
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("  ����ʼǰ���ڴ������ ");
		System.out.println("  Total:"+Runtime.getRuntime().totalMemory()/1024);
		System.out.println("  Free :"+Runtime.getRuntime().freeMemory()/1024);
		System.out.println("  Max  :"+Runtime.getRuntime().maxMemory()/1024);
		System.out.println(" ");
		try{
			DaoService dao = new DaoServiceDefaultImpl();
			
			//�����ݿ��ж�ȡҪ��ȡ�ĵ�ַ�б�
			List<CrawlList> list = dao.getCrawlLists();
			if(list != null && !list.isEmpty()){
				SchedulerFactory factory = new StdSchedulerFactory();
				Scheduler scheduler = factory.getScheduler();
				//long currentTime = System.currentTimeMillis();
				
				scheduler.start();
				System.out.println( "  Quartz���ȿ�ʼ����  " + list.size());
				
				for(int index=0;index<list.size();index++){
					//5*1000L;
					long repeatInterval = 600 * 1000L;//TODO 10����һ��
					//Date startTime = new Date(currentTime + repeatInterval*index);
					
					CrawlList info = (CrawlList)list.get(index);
					String folderId = info.getChannelId().toString();
					String url = info.getCrawlUrl();
					//String logFilePath = FileOperation.log_dir + info.getFolderId();
					
					String jobName = "j_" + folderId + "_" + url;
					JobDetail job = new JobDetail(jobName, folderId, DefaultJob.class);
					job.getJobDataMap().put("folderId", folderId);
					job.getJobDataMap().put("url", url);
					
					String triName = "t_" + folderId + "_" + index;
					Trigger trigger = new SimpleTrigger(triName, Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
					
					scheduler.scheduleJob(job, trigger);
					System.out.println(sdf.format(new Date()) + " : " + "һ��job����" + sdf.format(new Date()) + "����������job������Ϊ��" + jobName);
					System.out.println(" ");
				}
				System.out.println( "  Quartz��������  ");
			}else{
				System.out.println("�����ݿ��ж�ȡҪ��ȡ�ĵ�ַ�б�Ϊ��");
			}
		}catch(Exception e){
			log.error(e);
		}
		
	}

}
