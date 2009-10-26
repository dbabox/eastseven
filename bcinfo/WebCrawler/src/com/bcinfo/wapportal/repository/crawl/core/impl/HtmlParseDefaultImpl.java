/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.core.HtmlParse;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;
import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
import com.bcinfo.wapportal.repository.crawl.util.RomeRSS;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午10:27:05
 */
public class HtmlParseDefaultImpl implements HtmlParse {

	//private Parser parser;
	
	private FileOperation fileOperation;
	
	public HtmlParseDefaultImpl() {
		this.fileOperation = new FileOperation();
	}
	
	@Override
	public List<FolderBO> getUsableCrawlLink(String folderId, String url) {
		List<FolderBO> usableLinks = null;
		List<FolderBO> folders = null;
		try{
			
			//取得待爬取的link集合，分类处理url：URL、RSS
			if(url.startsWith("http://rss.")){
				folders = new RomeRSS().getAllLinks(url);
			}else{
				//TODO 貌似HTMLParser又可以将JS数据解析为静态页面了
				//综合处理页面链接抓取，考虑链接数据存放在js脚本中的情况
				folders = CrawlerUtil.getAllLinks(url);
			}
			
			usableLinks = new ArrayList<FolderBO>();
			//TODO 与log文件中的记录比较，并筛选出可爬取的集合返回
			List<String> logLinks = fileOperation.readLog(folderId);
			for(FolderBO folder : folders){
				String link = folder.getLink();
				
				if(folder.getTitle() == null) continue;
				else if("".equals(folder.getTitle()) || folder.getTitle().length() == 0) continue;
				
				if(!logLinks.contains(link)) usableLinks.add(folder);
				//TODO 采用数据比对
				//if(!fileOperation.contains(link)) usableLinks.add(folder);
			}
			
		}catch(Exception e){
			System.out.println("筛选["+url+"]可用爬取地址失败");
		}
		return usableLinks;
	}

}
