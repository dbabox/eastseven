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
 *         create time : 2009-10-21 ����09:36:44<br>
 *         ����FTP��������Դ�ļ��������������ݿ�
 */
public class ParseJob implements Job {

	private static Logger log = Logger.getLogger(ParseJob.class);
	
	public ParseJob() {
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("��ʼ�����ļ�");
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
				System.out.println(" û��ȡ�������ļ���ֵ ");
			}
			if(dir!=null && !"".equals(dir)){
				System.out.println(" ��ʼ����Ŀ¼["+dir+"]�µ��ļ�");
				List<Folder> list = new ArrayList<Folder>();
				File directory = new File(dir);
				if(directory.isDirectory()){
					File[] resFiles = directory.listFiles(/*new ResourceFileFilter()*/);
					if(resFiles!=null){
						System.out.println(dir+"Ŀ¼���У�"+resFiles.length+" ���ļ�");
					}else{
						System.out.println(dir+"Ŀ¼��û���ļ�");
					}
					Folder folder = null;
					for(File resFile : resFiles){
						System.out.println("������Դ�ļ���"+resFile.getName());
						folder = new Folder();
						folder.setResFileName(resFile.getName());
						SAXBuilder sb = new SAXBuilder(); 
					    Document doc = sb.build(resFile); //�����ĵ�����
					    Element root = doc.getRootElement(); //��ȡ��Ԫ��
					    
					    List children = root.getChildren();
					    for(int index=0;index<children.size();index++){
					    	Element e = (Element)children.get(index);
					    	String name = e.getName();
					    	if("localChannelId".equals(name)){
					    		folder.setId(e.getText());
					    	}else if("title".equals(name)){
					    		folder.setTitle(e.getText());
					    	}else if("createTime".equals(name)){
					    		//TODO �ݲ���������
					    	}else if("content".equals(name)){
					    		folder.setContent(e.getText());
					    	}else if("imgPath".equals(name)){
					    		//TODO �ݲ���������
					    	}
//					    	System.out.println(" ");
//					    	System.out.println("XML�ڵ�����"+e.getName());
//					    	System.out.println("XML�ڵ�����"+e.getText());
					    }
					    list.add(folder);
					}
				}else{
					System.out.println("���Ǵ����Դ�ļ���Ŀ¼...");
				}
				
				//�ֶδ���
				if(!list.isEmpty()){
					HandleContent handle = new HandleContent();
					list = handle.handleFolders(list);
					if(list != null && !list.isEmpty()){
						System.out.println("�ɹ������¼��"+list.size());
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
						System.out.println("��������ʼ");
						//������
						boolean bln = new OperationDB().patchSave(list);
						System.out.println(" ��¼�� "+list.size()+" �������� "+bln);
					}else{
						System.out.println("��¼Ϊ�գ��������");
					}
				}
				
			}else{
				System.out.println(" ���ؽ���, δ�ҵ�config.properties�ļ��е���ԴĿ¼");
			}
			
		}catch(Exception e){
			System.out.println(" ���ؽ������ʧ�� ");
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
