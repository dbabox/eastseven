/**
 * 
 */
package com.bcinfo.crawl.channel.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Channel;
import com.bcinfo.crawl.domain.model.CrawlerLog;
import com.bcinfo.crawl.domain.model.Resource;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.utils.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2010-6-21 上午09:56:58
 */
public class SiteParser implements Runnable {
	private static final Log log = LogFactory.getLog(SiteParser.class);

	private final String replacement = "";
	private String currentDate = "";
	private Parser parser;
	private Site site;
	private CrawlerLogService crawlerLogService = null;
	private WebCrawlerDao webCrawlerDao = null;

	public void setCrawlerLogService(CrawlerLogService crawlerLogService) {
		this.crawlerLogService = crawlerLogService;
	}

	public void setWebCrawlerDao(WebCrawlerDao webCrawlerDao) {
		this.webCrawlerDao = webCrawlerDao;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
	public synchronized void crawlSiteStart() {
		log.info("抓取"+site.getChannelName()+"["+site.getUrl()+"]"+"开始");
		try {
			currentDate = new SimpleDateFormat(site.getDatePattern()).format(new Date());
			parser = new Parser();
			parser.setURL(site.getUrl());
			parser.setEncoding(site.getCharset());
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList != null && nodeList.size() > 0) {
				List<CrawlerLog> crawlerLogs = this.crawlerLogService.get(site.getChannelId());
				List<Resource> list = new ArrayList<Resource>();
				int index = 0;
				for(NodeIterator iter = nodeList.elements(); iter.hasMoreNodes(); ) {
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String link = linkTag.extractLink().trim();
					String title = linkTag.getLinkText().trim();
					
					if(StringUtils.isEmpty(link) || StringUtils.isEmpty(title)) continue;
					if(!StringUtils.isEmpty(site.getPageSuffix()))
						if(!Pattern.compile(site.getPageSuffix()).matcher(link).find()) continue;
					
					if(site.isRealTime()) {
						if(!link.contains(currentDate)) continue;
					}
					
					CrawlerLog crawlerLog = new CrawlerLog(site.getChannelId(), link);
					if(crawlerLogs.contains(crawlerLog)) {
						log.info("[有了]["+title+"]["+link+"]");
						continue;
					}
					log.info("["+site.getChannelName()+"-处理中..."+index+"]["+title+"]["+link+"]");
					Resource resource = new Resource();
					resource.setSite(site);
					resource.setLink(link);
					resource.setTitle(title);
					resource.setChannel(new Channel(site.getChannelId(), site.getChannelName()));
					crawlPageContent(resource);
					if(!resource.isEmpty()) {
						if(site.isDebug()) {
							System.out.println("");
							System.out.println(title);
							System.out.println(link);
							System.out.println(resource.getContent());
						}
						list.add(resource);
						index++;
					}
					
				}
				if(!site.isDebug()&&!list.isEmpty()) {
					boolean bln = webCrawlerDao.saveBatch(list.toArray(), null);
					log.info("抓取"+site.getChannelName()+"["+site.getUrl()+"]保存入库["+list.size()+"]["+bln+"]");
				}
			}
			
		} catch (Exception e) {
			log.warn("抓取"+site.getChannelName()+"["+site.getUrl()+"]"+"失败");
			e.printStackTrace();
		}
		log.info("抓取"+site.getChannelName()+"["+site.getUrl()+"]"+"结束");
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void crawlPageContent(Resource resource) {
		//log.info("抓取资源["+resource.getTitle()+"]开始");
		String content = "";
		try {
			parser.reset();
			parser.setURL(resource.getLink());
			parser.setEncoding(site.getCharset());
			
			NodeList nodeList = null;
			if(StringUtils.isNotEmpty(site.getPageSelector()))
				nodeList = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter(site.getPageSelector()));
			
			//取分页地址
			List<String> pageLinks = new ArrayList<String>();
			pageLinks.add(resource.getLink());
			if(nodeList != null && nodeList.size() > 0) {
				for(NodeIterator iter = nodeList.elements(); iter.hasMoreNodes(); ) {
					Node node = iter.nextNode();
					if(node instanceof LinkTag) {
						LinkTag linkTag = (LinkTag)node;
						String link = linkTag.extractLink();
						if(link.contains("http://")) pageLinks.add(link);
					} else if(node instanceof OptionTag) {
						OptionTag optionTag = (OptionTag)node;
						String link = optionTag.getValue();
						if(link.contains("http://")) pageLinks.add(link);
					} else {
						System.out.println(node.getClass());
					}
				}
			}
			
			//去重复的分页地址
			List<String> list = new ArrayList<String>();
			for(Iterator<String> iter = new UniqueFilterIterator(pageLinks.iterator());iter.hasNext();) {
				list.add(iter.next());
			}
			pageLinks.clear();
			pageLinks.addAll(list);
			
			//循环分页内容
			for(String link : pageLinks) {
				parser.reset();
				parser.setURL(link);
				parser.setEncoding(site.getCharset());
				nodeList = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter(site.getContentSelector()));
				if(nodeList != null && nodeList.size() > 0) content += nodeList.toHtml().trim();
			}
			
			content = content.toLowerCase();
			
			//去外链
			content = content.replaceAll(RegexUtil.A, replacement);
			//格式化空格
			content = content.replaceAll("\\s+", " ");
			//将TD转换为BR
			content = content.replaceAll(RegexUtil.TD, RegexUtil.BR);
			
			//格式化内容标签
			Scanner scanner = new Scanner(content);
			scanner.useDelimiter("<");
			content = "";
			while(scanner.hasNext()) {
				content += "<" + scanner.next().trim() + "\n";
			}
			
			content = common(content);
			
			resource.setContent(content);
			
		} catch (Exception e) {
			log.warn("抓取资源["+resource.getTitle()+"]["+resource.getLink()+"]失败");
			e.printStackTrace();
		} finally {
			if(!site.isDebug()) this.crawlerLogService.save(new CrawlerLog(site.getChannelId(), resource.getLink()));
		}
		//log.info("抓取资源["+resource.getTitle()+"]结束");
	}
	
	public String common(String content) {
		Scanner scanner = new Scanner(content);
		//去通用标签
		content = content.replaceAll(RegexUtil.FONT, replacement)
		                 .replaceAll(RegexUtil.STRONG, replacement)
		                 .replaceAll(RegexUtil.SPAN, replacement)
		                 .replaceAll(RegexUtil.TABLE, replacement)
		                 .replaceAll(RegexUtil.COMMENT, replacement)
		                 .replaceAll(RegexUtil.CENTER, replacement)
		                 .replaceAll(RegexUtil.P_END, replacement)
		                 .replaceAll(RegexUtil.DIV, replacement)
		                 .replaceAll("\\(责任编辑.*?\\)", replacement)
		                 .replaceAll("<select\\s+[^>]+>.*?</select>", replacement)
		                 .replaceAll("&nbsp;", " ")
		                 ;
		
		//格式化BR标签及P标签,IMG标签加BR
		content = content.replaceAll("<[bB][rR]>", RegexUtil.BR)
		                 .replaceAll(RegexUtil.P, RegexUtil.BR)
		                 .replaceAll("<[iI][mM][gG]", RegexUtil.BR+"<img ")
		                 .replaceAll("\\r", replacement)
			             .replaceAll("\\r\\n", replacement)
			             .replaceAll("\\n", replacement);
		
		scanner = new Scanner(content);
		scanner.useDelimiter(RegexUtil.BR);
		content = "";
		while(scanner.hasNext()) {
			String line = scanner.next().trim();
			if(line.startsWith("<select")) continue;
			if(StringUtils.isNotEmpty(line)) content += RegexUtil.BR + line + "\n";
		}
		content = content.replaceFirst(RegexUtil.BR, replacement);
		return content;
	}
	
	@Override
	public void run() {
		crawlSiteStart();
	}

}
