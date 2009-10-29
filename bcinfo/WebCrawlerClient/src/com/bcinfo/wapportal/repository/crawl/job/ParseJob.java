/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.job;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.wapportal.repository.crawl.domain.Folder;
import com.bcinfo.wapportal.repository.crawl.domain.Resource;
import com.bcinfo.wapportal.repository.crawl.util.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.util.HandleContent;
import com.bcinfo.wapportal.repository.crawl.util.OperationDB;
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
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("开始解析文件");
		try{
			
			Properties property = new ConfigPropertyUtil().getConfigProperty();
			String dir = null;
			if(property!=null){
				String os = System.getenv("OS");
				System.out.println("OS:"+os);
				if(os!=null&&!"".equals(os)&&!"null".equals(os)){
					dir = property.getProperty("resource.dir.windows");
					System.out.println("WINNT:"+dir);
				}else{
					dir = property.getProperty("resource.dir.linux");
					System.out.println("LINUX:"+dir);
				}
			}else{
				System.out.println(" 没有取到配置文件的值 ");
			}
			if(dir!=null && !"".equals(dir)){
				System.out.println(" 开始解析目录["+dir+"]下的文件");
				List<Folder> list = new ArrayList<Folder>();
				File directory = new File(dir);
				if(directory.isDirectory()){
					File[] resFiles = directory.listFiles(/*new ResourceFileFilter()*/);
					if(resFiles!=null){
						System.out.println(dir+"目录下有："+resFiles.length+" 个文件");
					}else{
						System.out.println(dir+"目录下没有文件");
					}
					Folder folder = null;
					for(File resFile : resFiles){
						System.out.println("解析资源文件："+resFile.getName());
						folder = new Folder();
						folder.setResFileName(resFile.getName());
						SAXBuilder sb = new SAXBuilder(); 
					    Document doc = sb.build(resFile); //构造文档对象
					    Element root = doc.getRootElement(); //获取根元素
					    
					    List children = root.getChildren();
					    for(int index=0;index<children.size();index++){
					    	Element e = (Element)children.get(index);
					    	String name = e.getName();
					    	if("localChannelId".equals(name)){
					    		folder.setId(e.getText());
					    	}else if("title".equals(name)){
					    		folder.setTitle(e.getText());
					    	}else if("createTime".equals(name)){
					    		//TODO 暂不处理，备用
					    	}else if("content".equals(name)){
					    		folder.setContent(e.getText());
					    	}else if("imgPath".equals(name)){
					    		//TODO 暂不处理，备用
					    	}
//					    	System.out.println(" ");
//					    	System.out.println("XML节点名称"+e.getName());
//					    	System.out.println("XML节点名称"+e.getText());
					    }
					    list.add(folder);
					}
				}else{
					System.out.println("不是存放资源文件的目录...");
				}
				
				//分段处理
				if(!list.isEmpty()){
					HandleContent handle = new HandleContent();
					list = handle.handleFolders(list);
					if(list != null && !list.isEmpty()){
						System.out.println("成功处理记录："+list.size());
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
						System.out.println("入库操作开始");
						//入库操作
						boolean bln = new OperationDB().patchSave(list);
						System.out.println(" 记录： "+list.size()+" 入库操作： "+bln);
					}else{
						System.out.println("记录为空，不能入库");
					}
				}
				
			}else{
				System.out.println(" 本地解析, 未找到config.properties文件中的资源目录");
			}
			
		}catch(Exception e){
			System.out.println(" 本地解析入库失败 ");
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
