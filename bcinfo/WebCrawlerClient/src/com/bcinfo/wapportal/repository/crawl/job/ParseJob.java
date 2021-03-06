/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.domain.Folder;
import com.bcinfo.wapportal.repository.crawl.domain.Resource;
import com.bcinfo.wapportal.repository.crawl.util.HandleContent;
import com.bcinfo.wapportal.repository.crawl.util.OperationDB;
import com.bcinfo.wapportal.repository.crawl.util.ParseXML;
import com.bcinfo.wapportal.repository.crawl.util.ResourceType;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 上午09:36:44<br>
 *         解析FTP过来的资源文件，并保存至数据库
 */
public class ParseJob implements Job {

	private static Logger log = Logger.getLogger(ParseJob.class);
	
	public ParseJob() {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("开始解析文件");
		try{
			String dir = System.getProperty("user.dir")+"/remotedir/";
			if(dir!=null && !"".equals(dir)){
				log.info(" 开始解析目录["+dir+"]下的文件");
				List<Folder> list = new ArrayList<Folder>();
				File directory = new File(dir);
				if(directory.isDirectory()){
					File[] resFiles = directory.listFiles();
					if(resFiles!=null){
						log.info(dir+"目录下有："+resFiles.length+" 个文件");
					}else{
						log.info(dir+"目录下没有文件");
					}
					Folder folder = null;
					String fileName = "";
					DaoService dao = new DaoService();
					List<String> fileLogs = dao.getAllFileLog();
					ParseXML parseXML = new ParseXML();
					for(File resFile : resFiles){
						
						fileName = resFile.getName();
						if(fileName.lastIndexOf(".xml") == -1){
							log.info("非资源文件["+fileName+"]不予解析入库");
							continue;
						}
						
						if(!fileLogs.contains(fileName)){
							//记录
							boolean bln = dao.saveInternalFileLog(fileName, "1");
							log.info("记录解析资源文件："+fileName+" : "+bln);
						}else{
							continue;
						}
						log.info("解析资源文件："+fileName);
						folder = parseXML.parse(resFile);
						if(folder!=null) list.add(folder);
					}
				}else{
					log.info("不是存放资源文件的目录...");
				}
				
				//分段处理
				if(!list.isEmpty()){
					HandleContent handle = new HandleContent();
					list = handle.handleFolders(list);
					if(list != null && !list.isEmpty()){
						log.info("成功处理记录："+list.size());
						for(int index=0;index<list.size();index++){
							Folder f = (Folder)list.get(index);
							System.out.println(index+" : "+f.getId()+"|"+f.getTitle());
							List res = f.getResources();
							if(res!=null && !res.isEmpty()){
								for(int i=0;i<res.size();i++){
									Resource r = (Resource)res.get(i);
									if(r.getResourceType()==ResourceType.PIC){
										System.out.println(index+" : "+i+"|"+r.getResourcePath());
									}else{
										System.out.println(index+" : "+i+"|"+r.getResourceContent());
									}
										
								}
							}
						}
					}
					
					if(list!=null&&!list.isEmpty()){
						log.info("入库操作开始");
						//入库操作
						boolean bln = new OperationDB().patchSave(list);
						
						log.info(" 记录： "+list.size()+" 入库操作： "+bln);
					}else{
						log.info("记录为空，不能入库");
					}
				}
				
			}else{
				log.info(" 本地解析, 未找到config.properties文件中的资源目录");
			}
			
		}catch(Exception e){
			log.info(" 本地解析入库失败 ");
			log.error(e);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
	}

	class ResourceFileFilter implements FileFilter{

		@Override
		public boolean accept(File f) {
			return (f.getName().lastIndexOf(".xml")!=-1);
		}

	}
}
