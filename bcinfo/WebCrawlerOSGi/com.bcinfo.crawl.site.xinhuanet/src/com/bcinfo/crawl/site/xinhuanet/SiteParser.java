/**
 * 
 */
package com.bcinfo.crawl.site.xinhuanet;

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
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
 *         create time : 2010-6-22 上午10:31:23
 */
public class SiteParser implements Runnable, Job {

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
	
	@SuppressWarnings("unchecked")
	public synchronized void crawlSiteStart() {
		log.info("抓取["+site.getChannelName()+"]["+site.getUrl()+"]"+"开始");
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
					
					if(site.isDebug()) log.info("[筛选]["+title+"]["+link+"]");
					
					if(StringUtils.isEmpty(link) || StringUtils.isEmpty(title)) continue;
					if(!StringUtils.isEmpty(site.getPageSuffix()))
						if(!Pattern.compile(site.getPageSuffix()).matcher(link).find()) continue;
					
					if(site.isRealTime()) {
						if(!link.contains(currentDate)) continue;
					}
					
					CrawlerLog crawlerLog = new CrawlerLog(site.getChannelId(), link);
					if(crawlerLogs.contains(crawlerLog)) {
						if(site.isDebug())  log.info("[有了]["+title+"]["+link+"]");
						continue;
					}
					Resource resource = new Resource();
					resource.setSite(site);
					resource.setLink(link);
					resource.setTitle(title);
					resource.setChannel(new Channel(site.getChannelId(), site.getChannelName()));
					crawlPageContent(resource);
					if(!resource.isEmpty()) {
						if(site.isDebug()) {
							System.out.println("");
							System.out.println(title+"["+resource.getImages().size()+"]");
							System.out.println(link);
							System.out.println(resource.getContent());
						}
						list.add(resource);
						index++;
					}
					
				}
				
				//剔除重复对象
				if(!list.isEmpty()) {
					List<Resource> _list = new ArrayList<Resource>();
					for(Iterator<Resource> iter = new UniqueFilterIterator(list.iterator()); iter.hasNext(); ) {
						_list.add(iter.next());
					}
					list.clear();
					list.addAll(_list);
				}
				
				
				if(!site.isDebug()&&!list.isEmpty()) {
					boolean bln = webCrawlerDao.saveBatch(list.toArray(), null);
					log.info("抓取["+site.getChannelName()+"]["+site.getUrl()+"]保存入库["+list.size()+"]["+bln+"]");
				}
			}
			
		} catch (Exception e) {
			log.warn("抓取["+site.getChannelName()+"]["+site.getUrl()+"]"+"失败");
			e.printStackTrace();
		}
		log.info("抓取["+site.getChannelName()+"]["+site.getUrl()+"]"+"结束");
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void crawlPageContent(Resource resource) {
		//log.info("抓取资源["+resource.getTitle()+"]开始");
		String content = "";
		String head = "";
		try {
			
			head = resource.getLink();
			head = head.substring(0, head.lastIndexOf("/")+1);
			
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
						System.out.println(node.toHtml());
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
			if(site.getName().equals(Site.XIN_HUA_NET)) content = xinhuanet(content, head, resource);
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
	
	public String xinhuanet(String content, String head, Resource resource) {
		String cnt = "";
		try {
			cnt = content;
			if(cnt.contains("<img")) {
				//新华网图片地址处理
				List<String[]> images = new ArrayList<String[]>();
				parser.reset();
				parser.setInputHTML(cnt);
				parser.setEncoding(site.getCharset());
				NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
				if(nodeList != null && nodeList.size() > 0) {
					int index = 1;
					for(NodeIterator iter = nodeList.elements(); iter.hasMoreNodes();index++) {
						ImageTag imageTag = (ImageTag)iter.nextNode();
						String imageSrc = imageTag.getImageURL();
						imageSrc = head + imageSrc;
						String imageHTML_Old = imageTag.toHtml();
						String imageHTML_New = "<img src=\""+imageSrc+"\" alt=\""+imageTag.getImageURL()+"\"/>";
						images.add(new String[]{imageHTML_Old, imageHTML_New});
						resource.getImages().add(imageSrc);
					}
				}
				if(!images.isEmpty()) {
					for(String[] image : images) {
						cnt = cnt.replace(image[0], image[1]);
					}
				}
			}
			
			cnt = cnt.replaceAll("发表您的观点。请您文明上网、理性发言并遵守相关规定，在注册后发表评论。 ", replacement)
				     .replaceAll("留言须知", replacement)
				     .replaceAll("\\[进入新华网.*?论坛\\]", replacement)
				     .replaceAll("\\[\\d+\\]", replacement)
				     .replaceAll("={2,}", replacement)
				     .replaceAll("\\.{2,}", replacement);
//				     .replaceAll("<font color=\"blue\">.*?</font>", replacement);
			
			//格式化内容标签,去相关链接
			/*
			Scanner scanner = new Scanner(content);
			scanner.useDelimiter("<");
			cnt = "";
			while(scanner.hasNext()) {
				String line = "<" + scanner.next().trim() + "\n";
				if(line.startsWith("<font color=\"blue\">")) continue;
				if(line.startsWith("<font color=\"maroon\">")) continue;
				cnt += line;
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			return content;
		}
		return cnt;
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
		                 .replaceAll(RegexUtil.IFRAME, replacement).replaceAll(RegexUtil.B, replacement)
		                 .replaceAll(RegexUtil.U, replacement)
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
		content = content.replaceAll("\\?{4}", "　　");
		return content;
	}
	
	@Override
	public void run() {
		crawlSiteStart();
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		setSite((Site)jobDataMap.get("site"));
		setWebCrawlerDao((WebCrawlerDao)jobDataMap.get("webCrawlerDao"));
		setCrawlerLogService((CrawlerLogService)jobDataMap.get("crawlerLogService"));
		run();
		log.info("["+site.getChannelId()+"."+site.getChannelName()+"-"+site.getName()+"]["+site.getUrl()+"]"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getNextFireTime()));
	}
}
