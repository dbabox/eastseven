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
 *         create time : 2009-10-14 ����12:43:16<br>
 *         ͨ��������RSS�����ȡRSS����<br>
 */
public final class RomeRSS {

	@SuppressWarnings("unchecked")
	public List<FolderBO> getAllLinks(String rssUrl){
		List<FolderBO> links = null;
		try{
			URL url = new URL(rssUrl);
			XmlReader reader = new XmlReader(url);
			SyndFeedInput input = new SyndFeedInput();
			// �õ�SyndFeed���󣬼��õ�RssԴ���������Ϣ
			SyndFeed feed = input.build(reader);
			// �õ�Rss�����������б�
			List entries = feed.getEntries();
			links = new ArrayList(entries.size());
			// ѭ���õ�ÿ��������Ϣ
			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = (SyndEntry) entries.get(i);
				String link = entry.getLink().trim();
				//ȥ��Ŀǰ������ȡ�ĵ�ַ
				if(!CrawlerUtil.canCrwal(link)) continue;
				//TODO �������RSS
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
			System.out.println("ͨ��RSSȡ��ҳ��["+rssUrl+"]�ڵ����г����ӵ�ַʧ��");
		}
		return links;
	}
}
