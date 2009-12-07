/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 ����09:52:06<br>
 *         ����ɨ����������Դ�ļ���������ɾ��
 */
public class MonitorJob implements Job {

	private static Logger log = Logger.getLogger(MonitorJob.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("ɨ�迪ʼ...");
		try{
			DaoService dao = new DaoService();
			//ȡ������ʧ�ܵ��ļ���
			List<String> list = dao.getAllFileLog("0");
			if(list!=null&&!list.isEmpty()){
				String dir = System.getProperty("user.dir")+"/remotedir/";
				boolean delFile = false;
				boolean delFileName = false;
				if(dir!=null&&!"".equals(dir)){
					File remoteDir = new File(dir);
					if(remoteDir.isDirectory()){
						File[] files = remoteDir.listFiles();
						String name;
						for(File file : files){
							name = file.getName();
							if(list.contains(name)){
								log.info(" ɾ���ļ�["+name+"]:"+file.delete());
							}
						}
						delFile = true;
						delFileName = dao.deleteInternalFileLog(list);
					}
				}
				log.info("ɾ����Ч�ļ�[�����ļ�ɾ����"+delFile+"][���ݿⱣ����ļ�����"+delFileName+"]");
			}
		}catch(Exception e){
			System.out.println(" ����ɨ��ʧ��... ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
