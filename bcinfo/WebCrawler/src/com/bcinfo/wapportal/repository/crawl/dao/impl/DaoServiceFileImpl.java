/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceBO;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-16 上午11:14:16
 */
public class DaoServiceFileImpl implements DaoService {

	private static Logger log = Logger.getLogger(DaoServiceFileImpl.class);
	
	@Override
	public List<CrawlList> getCrawlLists() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CrawlList> getCrawlLists(Long folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean saveCrawlResource(List<FolderBO> folders) {
		Boolean bln =false;
		try{
			long id = 0;
			for(FolderBO folder : folders){
				Boolean isSave = writeFile(folder.getTitle(), folder.getLink(), folder.getContent()); 
					//saveToXMLFile(folder, id);
				if(log.isDebugEnabled()){
					if(isSave){
						log.info(folder.getLink()+" | "+folder.getTitle()+" | 保存成功");
					}else{
						log.info(folder.getLink()+" | "+folder.getTitle()+" | 保存失败");
					}
				}
				id++;
			}
		}catch(Exception e){
			log.error(e);
		}
		return bln;
	}

	Boolean saveToXMLFile(FolderBO folder, long id) {
		Boolean bln = false;
		try{
			if(log.isDebugEnabled()){
				log.debug(folder);
			}
			// 创建根节点 list;
	        Element root = new Element("folder");
	       
	        Element folderId = new Element("folderId");
	        folderId.setText(folder.getFolderId());
	        root.addContent(folderId);
	        
	        Element link = new Element("link");
	        link.setText(folder.getLink());
	        root.addContent(link);
	        
	        Element title = new Element("title");
	        title.setText(folder.getTitle());
	        root.addContent(title);
	        
	        Element originalCnt = new Element("originalCnt");
	        originalCnt.setText(folder.getContent());
	        root.addContent(originalCnt);
	        
	        List<ResourceBO> list = folder.getResources();
	        if(log.isDebugEnabled()){
	        	if(list != null){
	        		log.debug("Resource List is not null");
	        	}else{
	        		log.debug("Resource List is null");
	        	}
	        }
	        if(list != null && !list.isEmpty()){
	        	Element resources = new Element("resources");
		        Integer resId = 0;
		        for(ResourceBO res : list){
		        	Element resource = new Element("resource");
		        	
		        	Element resourceId = new Element("resourceId");
		        	resourceId.setText(res.toString());
		        	resource.addContent(resourceId);
		        	
		        	Element resourceCnt = new Element("resourceCnt");
		        	resourceCnt.setText(res.getContent());
		        	resource.addContent(resourceCnt);
		        	
		        	Element resourcePath = new Element("resourcePath");
		        	resourcePath.setText(res.getPath());
		        	resource.addContent(resourcePath);
		        	
		        	Element resourceType = new Element("resourceType");
		        	resourceType.setText(String.valueOf(res.getType()));
		        	resource.addContent(resourceType);
		        	
		        	resources.addContent(resource);
		        	resId++;
		        }
		        
		        root.addContent(resources);
	        }
	        
	        XMLOutputter xmlOut = new XMLOutputter();
	        // 根节点添加到文档中；
	        Document doc = new Document(root);
	        
	        String dir = "E:/bcinfo/内容抓取/contents/"+folder.getFolderId()+"/";
	        File file = new File(dir);
	        if(!file.exists()){
	        	boolean mk = file.mkdir();
	        	if(mk) log.debug(dir+"创建成功");
	        	else log.debug(dir+"创建失败");
	        }
	        
	        xmlOut.output(doc, new FileOutputStream(dir+id+".xml"));
	        bln = true;
		}catch(Exception e){
			log.error(e);
		}
		return bln;
	}
	
	/**
	 * 将爬取的资源保存到文件中
	 * @param fileName
	 * @param content
	 * @return
	 */
	boolean writeFile(String fileName, String link, String content){
		boolean bln = false;
		FileOutputStream out = null;
		try{
			File file = new File("C:/Download/"+fileName+".txt");
			//if(file.exists()) file.delete();
			out = new FileOutputStream(file,true);
			link += "\n";
			fileName += "\n";
			content += "\n";
			out.write(link.getBytes());
			out.write(fileName.getBytes());
			out.write(content.getBytes());
			out.close();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return bln;
	}
}
