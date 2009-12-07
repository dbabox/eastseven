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
 *         create time : 2009-10-21 上午09:52:06<br>
 *         负责扫描已入库的资源文件，并将其删除
 */
public class MonitorJob implements Job {

	private static Logger log = Logger.getLogger(MonitorJob.class);
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("扫描开始...");
		try{
			DaoService dao = new DaoService();
			//取出处理失败的文件名
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
								log.info(" 删除文件["+name+"]:"+file.delete());
							}
						}
						delFile = true;
						delFileName = dao.deleteInternalFileLog(list);
					}
				}
				log.info("删除无效文件[物理文件删除："+delFile+"][数据库保存的文件名："+delFileName+"]");
			}
		}catch(Exception e){
			System.out.println(" 本地扫描失败... ");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

}
