/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.CatchConfigInfo;

/**
 * @author dongq
 * 
 * create time : 2009-9-25 下午05:06:03
 */
public class CatchHtmlAdapter {

	public static List newApp(List list){
		List newList = null;
		try{
			if(list != null && !list.isEmpty()){
				newList = new ArrayList();
				for(int index=0;index<list.size();index++){
					CatchConfigInfo info = (CatchConfigInfo)list.get(index);
					if(info.isNew()) newList.add(info);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return newList;
	}
	
	public static String[][] oldApp(List list){
		String rssAddr[][] = null;
		try{
			if(list != null && !list.isEmpty()){
				int size = 0;
				List oldList = new ArrayList();
				//去除新的
				for(int index=0;index<list.size();index++){
					CatchConfigInfo info = (CatchConfigInfo)list.get(index);
					if(!info.isNew()){
						oldList.add(info);
						size++;
					}
				}
				//将旧的转换为二维数组
				rssAddr = new String[size][3];
				for(int index=0;index<size;index++){
					CatchConfigInfo info = (CatchConfigInfo)oldList.get(index);
					rssAddr[index][0] = info.getUrl();
					rssAddr[index][1] = info.getFolderId();
					rssAddr[index][2] = info.getLogFilePath();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rssAddr;
	}
	
	public static void main(String[] args) {}
}
