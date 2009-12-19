/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

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
}
