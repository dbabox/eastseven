/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bcinfo.wapportal.repository.crawl.domain.Folder;

/**
 * @author dongq
 * 
 * create time : 2009-11-30 上午11:18:24
 */
public class ParseXML {

	private static final Logger log = Logger.getLogger(ParseXML.class);
	
	public static void main(String[] args) {
		File file = new File("C:/Download/091201123940_022038_988390996874898054.xml");
		Folder folder = new ParseXML().parse(file);
		System.out.println(folder);
		try {
			new HandleContent().handleFolder(folder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Folder parse(File file) {
		Folder folder = null;
		
		try{
			
			SAXBuilder sb = new SAXBuilder(); 
		    Document doc = sb.build(file); //构造文档对象
		    Element root = doc.getRootElement(); //获取根元素
		    
		    List children = root.getChildren();
		    folder = new Folder();
			folder.setResFileName(file.getName());
		    for(int index=0;index<children.size();index++){
		    	Element e = (Element)children.get(index);
		    	String name = e.getName();
		    	if("localChannelId".equals(name)){
		    		folder.setId(e.getText());
		    	}else if("title".equals(name)){
		    		folder.setTitle(e.getText());
		    	}else if("link".equals(name)){
		    		folder.setUrl(e.getText());
		    	}else if("createTime".equals(name)){
		    		//TODO 暂不处理，备用
		    	}else if("content".equals(name)){
		    		String cnt = CrawlerUtil.formatSpecialWords(e.getText());
		    		folder.setContent(cnt);
		    	}else if("imgPath".equals(name)){
		    		//TODO 暂不处理，备用
		    	}else if("filePath".equals(name)){
		    		folder.setDownloadFile(e.getText().replaceAll("=", ""));
		    		log.debug("要下载的文件路径："+folder.getDownloadFile());
		    	}else if("operation".equals(name)){
		    		folder.setOperation(e.getText());
		    	}else if("sendType".equals(name)){
		    		folder.setSendType(e.getText());
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}
		
		return folder;
	}
}
