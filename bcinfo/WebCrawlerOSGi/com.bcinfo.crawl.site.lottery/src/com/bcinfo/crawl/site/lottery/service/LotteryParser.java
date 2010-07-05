/**
 * 
 */
package com.bcinfo.crawl.site.lottery.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.util.NodeList;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Channel;
import com.bcinfo.crawl.domain.model.LotteryResource;
import com.bcinfo.crawl.domain.model.Resource;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.site.service.ParsePageService;

/**
 * @author dongq
 * 
 *         create time : 2010-5-24 上午09:16:24
 */
public final class LotteryParser implements ParsePageService, Runnable {

	private static final Log log = LogFactory.getLog(LotteryParser.class);
	
	private Site site;
	private WebCrawlerDao dao;
	
	public LotteryParser() {
		
	}
	
	public LotteryParser(Site site, WebCrawlerDao dao) {
		this.site = site;
		this.dao = new WebCrawlerDaoImpl();
	}
	
	public void run() {
		synchronized (site) {
			String link = site.getUrl();
			if(link != null) {
				Resource resource = getPageContent(link);
				if(resource != null && !resource.isEmpty()) {
					Channel channel = new Channel();
					channel.setId(site.getChannelId());
					channel.setName(site.getChannelName());
					resource.setChannel(channel);
					resource.setTitle(site.getChannelName() + resource.getTitle() + "开奖公告");
					resource.setStatus(Resource.AGREE);
					boolean bln = dao.save(resource, null);
					if(bln) System.out.println(resource.getTitle() + " 保存：" + (bln ? "成功" : "失败"));
				}
			}
		}
	}
	
	public Resource getPageContent(String link) {
		if(link == null && "".equals(link)) return null;
		Resource resource = null;
		String content = "";
		String title = "";
		try {
			resource = new LotteryResource();
			resource.setLink(link);
			
			Parser parser = new Parser(link);
			parser.setEncoding(resource.getCharset());
			NodeList nodeList = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class=ball_box01]>ul>li"));
			
			if(nodeList != null && nodeList.size() > 0) {
				Node[] nodes = nodeList.toNodeArray();
				content += "开奖日期："+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"\n<br />";
				content += "开奖号码：";
				for(Node node : nodes) {
					Bullet li = (Bullet)node;
					content += li.getStringText() + " ";
				}
			} else {
				log.warn("彩票抓取无内容");
			}
			
			if(content.length() == 0) return null;
			parser.reset();
			nodeList = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("td[class=td_title01]>span[class=span_left]"));
			if(nodeList != null && nodeList.size() > 0) {
				title = nodeList.toHtml();
				title = title.replaceAll("<[sS][pP][aA][nN]\\s+[^>]+>|<[sS][pP][aA][nN]>|</[sS][pP][aA][nN]>", "");
				title = title.replaceAll("<[fF][oO][nN][tT]\\s+[^>]+>|</[fF][oO][nN][tT]>", "");
				title = title.replaceAll("<[sS][tT][rR][oO][nN][gG]>|</[sS][tT][rR][oO][nN][gG]>", "");
				title = title.replaceAll(" ", "");
			} else {
				log.warn("彩票抓取无标题");
			}
			
			resource.setContent(content);
			resource.setTitle(title);
			
		} catch (Exception e) {
			log.warn("彩票抓取[" + link + "]");
			e.printStackTrace();
		}
		
		return resource;
	}
	
}
