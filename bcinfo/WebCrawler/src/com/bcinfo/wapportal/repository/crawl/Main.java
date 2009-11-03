/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl;

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

import com.bcinfo.wapportal.repository.crawl.domain.CatchConfigInfo;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;
import com.bcinfo.wapportal.repository.crawl.job.DefaultJob;
import com.bcinfo.wapportal.repository.crawl.util.CatchHtmlAdapter;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����09:43:54<br>
 *         �����ķ���
 */
@Deprecated
public final class Main {

	private static Logger log = Logger.getLogger(Main.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	//������������
	public static void main(String[] args) {
		System.out.println(sdf.format(new Date()) + " : CatchHtml Ӧ�ÿ�ʼ��ʼ��...");
		//1.load catch list
		System.out.println(sdf.format(new Date()) + " : ����" + FileOperation.catch_list + "�ļ�...");
		
		List<CatchConfigInfo> list = FileOperation.getUsableCatchConfigInfoList();
		
		if(list != null && !list.isEmpty()){
			System.out.println(sdf.format(new Date()) + " : ��" + FileOperation.catch_list + "�ļ��ж�ȡ"+list.size()+"����ȡ����...");
			//2.run quartz job
			boolean bln = run(list);
			if(bln){
				System.out.println(sdf.format(new Date()) + " : CatchHtml Ӧ�������ɹ�...");
			}else{
				System.out.println(sdf.format(new Date()) + " : CatchHtml Ӧ������ʧ��...");
			}
		}else{
			System.out.println(sdf.format(new Date()) + " : ����" + FileOperation.catch_list + "�ļ�Ϊ�գ�CatchHtmlδ������");
		}
		
	}

	public static boolean run(List list){
		boolean bln = false;
		if(list == null) return false;
		try{
			//TODO ��ʱ���¾ɳ���ֿ�ִ�У�����Ӱ��ɵ���ȡ���򣬲��𲽽��ɵĳ���Ǩ�Ƶ��µ�����
			//new
			List newList = CatchHtmlAdapter.newApp(list);
			if(newList != null && !newList.isEmpty()){
				SchedulerFactory factory = new StdSchedulerFactory();
				Scheduler scheduler = factory.getScheduler();
				long currentTime = System.currentTimeMillis();
				
				scheduler.start();
				System.out.println(sdf.format(new Date()) + " : Quartz���ȿ�ʼ����  " + newList.size());
				
				for(int index=0;index<newList.size();index++){
					
					long repeatInterval = 1000L;//600 * 1000L;
					Date startTime = new Date(currentTime + repeatInterval*index);
					
					CatchConfigInfo info = (CatchConfigInfo)newList.get(index);
					String folderId = info.getFolderId();
					String[] urls = info.getUrl().split("\\|");
					//String logFilePath = FileOperation.log_dir + info.getFolderId();
					
					for(int i=0;i<urls.length;i++){
						String url = urls[i];
						
						String jobName = "j_" + folderId + "_" + url;
						JobDetail job = new JobDetail(jobName, folderId, DefaultJob.class);
						job.getJobDataMap().put("folderId", folderId);
						job.getJobDataMap().put("url", url);
						
						String triName = "t_" + folderId + "_" + i;
						Trigger trigger = new SimpleTrigger(triName, Scheduler.DEFAULT_GROUP, startTime, null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
						
						scheduler.scheduleJob(job, trigger);
						System.out.println(sdf.format(new Date()) + " : " + "һ��job����" + sdf.format(startTime) + "����������job������Ϊ��" + jobName);
						System.out.println(" ");
					}
				}
			}
			//old
			String[][] rssAddr = CatchHtmlAdapter.oldApp(list);
			if(rssAddr != null){
				//for(int i=0;i<rssAddr.length;i++){
					//System.out.println(i+"|url:"+rssAddr[i][0]+"|fid:"+rssAddr[i][1]+"|log:"+rssAddr[i][2]);
				//}
				//RrsDownload.Rss_rx(rssAddr);
			}
			bln = true;
		}catch(Exception e){
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return bln;
	}

}
