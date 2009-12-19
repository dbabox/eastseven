/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.bcinfo.wapportal.repository.crawl.core.CacheQueue;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;
import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-9-29 下午12:43:30<br>
 *         监控Job
 */
public class DefaultJob implements Job {

	private static Logger log = Logger.getLogger(DefaultJob.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	private DaoService daoService;
	
	public DefaultJob() {
		daoService = new DaoServiceDefaultImpl();
	}
	
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try{
			Scheduler scheduler = context.getScheduler();
			
			System.out.println(" ***** 监控["+sdf.format(new Date(System.currentTimeMillis()))+"] ***** ");
			System.out.println("  程序运行中的内存情况： ");
			System.out.println("  Total:"+Runtime.getRuntime().totalMemory()/1024);
			System.out.println("  Free :"+Runtime.getRuntime().freeMemory()/1024);
			System.out.println("  Max  :"+Runtime.getRuntime().maxMemory()/1024);
			System.out.println("  Queue:"+CacheQueue.getQueue().size());
			List executingJobs = scheduler.getCurrentlyExecutingJobs();
			int job_size = executingJobs!=null&&!executingJobs.isEmpty()?executingJobs.size():0;
			
			System.out.println("  Executing Jobs:"+job_size);
			if(job_size<=5&&job_size>0){
				for(int index=0;index<job_size;index++){
					JobExecutionContext exeContext = (JobExecutionContext)executingJobs.get(index);
					System.out.println("    Job Name:"+exeContext.getJobDetail().getName());
				}
			}
			int size = Integer.parseInt(ConfigPropertyUtil.getConfigProperty("config.properties").get("queue.size").toString());
			
			boolean bln = false;
			if(CacheQueue.getQueue().size()>=size){
				bln = save(size);
				log.info("保存"+size+"条记录到数据库，当前队列中还有"+CacheQueue.getQueue().size()+"条记录["+bln+"]");
			}
			/**/
			if(CacheQueue.getQueue().size()>0&&CacheQueue.getQueue().size()<size&&job_size<=1){
				size = CacheQueue.getQueue().size();
				bln = save(size);
				log.info("清空队列,保存"+size+"条记录到数据库,当前队列中还有"+CacheQueue.getQueue().size()+"条记录["+bln+"]");
			}
			
			List<String> list = new ArrayList<String>();
			//加入新的Job和Trigger
			List<CrawlList> startList = daoService.getCrawlLists("1");
			if(startList!=null&&!startList.isEmpty()){
				String name = null;
				String group = null;
				JobDetail job = null;
				Trigger trigger = null;
				for(CrawlList crawl : startList){
					name = crawl.getCrawlId().toString();
					list.add(name);
					group = crawl.getChannelId().toString();
					job = scheduler.getJobDetail(name, group);
					if(job==null){
						job = new JobDetail(name, group, SingleJob.class);
						job.getJobDataMap().put("crawlList", crawl);
						trigger = new SimpleTrigger(name, group, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, 60*60*1000L);
						trigger.setDescription(crawl.getCrawlUrl());
						Date start = scheduler.scheduleJob(job, trigger);
						log.info("抓取地址["+crawl.getCrawlUrl()+"]将在"+start+"启动");
					}
				}
			}
			//停用的
			List<CrawlList> stopList = daoService.getCrawlLists("0");
			if(stopList!=null&&!stopList.isEmpty()){
				String name = null;
				String group = null;
				JobDetail job = null;
				for(CrawlList crawl : stopList){
					name = crawl.getCrawlId().toString();
					list.add(name);
					group = crawl.getChannelId().toString();
					job = scheduler.getJobDetail(name, group);
					if(job!=null){
						scheduler.pauseJob(name, group);
						scheduler.pauseTrigger(name, group);
						log.info("停用抓取地址["+crawl.getCrawlUrl()+"]");
					}
				}
			}
			
			if(list!=null&&!list.isEmpty()){
				String[] jobGroups = scheduler.getJobGroupNames();
				for(String jobGroup : jobGroups){
					String[] jobNames = scheduler.getJobNames(jobGroup);
					for(String jobName : jobNames){
						if(!list.contains(jobName)&&!jobName.contains("monitor")){
							scheduler.deleteJob(jobName, jobGroup);
							log.info("删除废弃的抓取地址[id:"+jobName+"][channel:"+jobGroup+"]");
						}
					}
				}
			}
			System.out.println(" ");
			
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
	}
	
	//保存到数据库
	boolean save(int size) throws Exception {
		boolean bln = false;
		List<FolderBO> list = new ArrayList<FolderBO>(size);
		for(int i=0;i<size;i++){
			list.add(CacheQueue.getQueue().poll());
		}
		if(!list.isEmpty()){
			bln = daoService.saveCrawlResource(list);
		}
		return bln;
	}
}
