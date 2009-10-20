/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午12:43:16<br>
 *         通过第三方RSS组件获取RSS内容<br>
 */
public final class RomeRSS {

	@SuppressWarnings("unchecked")
	public List<FolderBO> getAllLinks(String rssUrl){
		List<FolderBO> links = null;
		try{
			URL url = new URL(rssUrl);
			XmlReader reader = new XmlReader(url);
			SyndFeedInput input = new SyndFeedInput();
			// 得到SyndFeed对象，即得到Rss源里的所有信息
			SyndFeed feed = input.build(reader);
			// 得到Rss新闻中子项列表
			List entries = feed.getEntries();
			links = new ArrayList(entries.size());
			// 循环得到每个子项信息
			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = (SyndEntry) entries.get(i);
				String link = entry.getLink().trim();
				//去除目前不能爬取的地址
				if(!CrawlerUtil.canCrwal(link)) continue;
				//TODO 针对新浪RSS
				//http://go.rss.sina.com.cn/redirect.php?url=http://sports.sina.com.cn/g/2009-09-16/07074590053.shtml
				if(link.indexOf("url=http") != -1){
					String[] _link = link.split("url=");
					for(int index = 0;index <_link.length; index++){
						if(!_link[index].endsWith(".php")) link = _link[index];
					}
				}	                          
				String title = entry.getTitle().trim();
				FolderBO folder = new FolderBO();
				folder.setLink(link);
				folder.setTitle(title);
				links.add(folder);
			}
		}catch(Exception e){
			System.out.println("通过RSS取得页面["+rssUrl+"]内的所有超链接地址失败");
		}
		return links;
	}
}
