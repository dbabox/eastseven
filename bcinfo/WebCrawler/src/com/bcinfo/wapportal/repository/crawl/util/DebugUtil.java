/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

/**
 * @author dongq
 * 
 * create time : 2009-9-28 ����09:51:04
 */
public final class DebugUtil {

	/*
	public static void printFolderList(List folders){
		//System.out.println("  ");
		try{
			if(folders != null && !folders.isEmpty()){
				for(int index=0;index<folders.size();index++){
					Folder folder = (Folder)folders.get(index);
					//System.out.println(" ----------------------------------------- ");
					//System.out.println(folder.getId()+"|"+folder.getTitle()+"|"+folder.getUrl());
					//System.out.println("�����������ݣ�"+folder.getContent());
					List resources = folder.getResources();
					if(resources!=null && !resources.isEmpty()){
						for(int i=0; i<resources.size(); i++){
							Resource resource = (Resource)resources.get(i);
							//System.out.println("    �ֶ����ݣ�");
							//System.out.println("      ��"+i+"��: "+resource.getResourceContent());
						}
					}
					//System.out.println(" ");
				}
			}else{
				//System.out.println(" ����ļ��϶����ǿյ� ");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("  ");
	}
	*/
	public static void printNodeList(NodeList nodeList){
		//System.out.println("  ");
		try{
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				int index = 1;
				while(iter.hasMoreNodes()){
					Node node = iter.nextNode();
					//System.out.println(" ----------------- "+index+" ------------------------ ");
					//System.out.println(" �ڵ����� " + node.getClass());
					//System.out.println(" �ڵ�ֵ " + node.getText());
					//System.out.println(" ");
					index++;
				}
			}else{
				//System.out.println(" ����ļ��϶����ǿյ� ");
			}
		}catch(Exception e){
			
		}
		//System.out.println("  ");
	}
	
	public static NodeList getNodeList(String link, String tagName){
		NodeList nodeList = new NodeList();
		try{
			Parser parser = new Parser(link);
			nodeList = parser.extractAllNodesThatMatch(new TagNameFilter(tagName));
		}catch(Exception e){
			e.printStackTrace();
		}
		return nodeList;
	}
	
	public static boolean downloadFile(String link){
		boolean bln = false;
		String filePath = null;
		InputStream is = null;
		OutputStream os = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		try{
			String fileSuffix = link.substring(link.lastIndexOf(".")+1);
			URL url = new URL(link);
			URLConnection http = url.openConnection();
			http.addRequestProperty("Referer", link);
			
			is = http.getInputStream();
			if(is!=null){
				filePath = "C:/";
				File file = new File(filePath);
				if(!file.exists()) file.mkdir();
				filePath += "pa_file_"+sdf.format(new Date(System.currentTimeMillis()))+"."+fileSuffix;
				//if(!file.exists()) file.createNewFile();
				file = new File(filePath);
				os = new FileOutputStream(file);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];

				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				System.out.println("�ļ����غ�����"+filePath);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
				if(os!=null) os.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return bln;
	}
}
