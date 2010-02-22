/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-11 ����11:30:18<br>
 *         SOHU��Ʊץȡ��ʵ��<br>
 */
public class WebCrawlerLotteryImpl extends AbstractHtmlParseTemplete implements WebCrawler {

	private static Logger log = Logger.getLogger(WebCrawlerLotteryImpl.class);

	/*
	 * http://lottery.sports.sohu.com/open/sceexw.shtml
	 * http://lottery.sports.sohu.com/open/static/shtml/ygcttl/default.shtml
	 * http://lottery.sports.sohu.com/open/index_df.shtml
	 * http://lottery.sports.sohu.com/open/index_gp.shtml
	 * */
	@Override
	public List<FolderBO> crawl(Long crawlId, String folderId, String url) {
		List<FolderBO> folders = null;
		
		try{
			String link = null;
			String httpHeader = CrawlerUtil.extractLinkHeader(url);
			//SOHU��Ʊ��ַΪ�̶����ӣ����Բ��ý����Ӽ�¼����־��
			//�轫��ͬ����ÿ�ڽ��ű��棬�������ݿ��¼�ȶԣ���ֹ�ظ���ȡͬ�ڽ���
			Parser parser = new Parser(url);
			NodeList nodeList = parser.extractAllNodesThatMatch(new TagNameFilter("iframe"));
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Node node = iter.nextNode();
					if(node instanceof TagNode){
						TagNode tagNode = (TagNode)node;
						if("IframeHistoryAward".equals(tagNode.getAttribute("id"))){
							link = CrawlerUtil.addLinkHeader(tagNode.getAttribute("src"), httpHeader);
							
						}
					}
				}
				
				
			}else{
				log.info("û���ҵ�["+url+"]Ŀ��");
			}
			
			if(link != null){
				DaoService dao = new DaoServiceDefaultImpl();
				//span class span_left
				String title = null;
				title = this.getPageContent(link, "span", "class", "span_left");
				title = title.replaceAll(RegexUtil.REGEX_FONT, replacement);
				title = title.replaceAll(RegexUtil.REGEX_STRONG, replacement);
				title = title.replaceAll("��|��", replacement);
				title = dao.getChannelName(Long.parseLong(folderId))+"��"+title.trim()+"�ڿ�������";
				
				
				//div class ball_box01
				//span class cfont5
				String content = null;
				content = this.getPageContent(link, "div", "class", "ball_box01");
				content += this.getPageContent(link, "span", "class", "cfont5 ");
				content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
				content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
				content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
				content = content.replaceAll("<ul>|</ul>", replacement);
				content = content.replaceAll("</li>", replacement);
				content = content.replaceAll("<li\\s+[^>]+>|<li>", " ");
				content = " �������� : "+content.trim();
				
				if(log.isDebugEnabled()){
					log.info(link);
					log.info("�ں�["+title+"]����["+content+"]");
				}
				
				
				if(dao.isExistCrawlResource(Long.parseLong(folderId), title)){
					log.info("Ƶ��["+folderId+"]��Ʊ�ں�["+title+"]�Ѵ���");
					return null;
				}
				
				if(content != null && !"".equals(content)){
					folders = new ArrayList<FolderBO>();
					folders.add(new FolderBO(folderId, title, link, content));
				}
			}else{
				log.info("û���ҵ�["+url+"]Ŀ��");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.info(" ��Ʊץȡʧ��["+url+"]");
			//log.error("");
		}
		
		return folders;
	}

	@Override
	public List<String> checkPageOfLinks(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetContent(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception{
		//http://lottery.sports.sohu.com/open/sd.shtml
		//http://lottery.sports.sohu.com/open/sfc.shtml
		String url = "http://lottery.sports.sohu.com/open/sd.shtml";
		new WebCrawlerLotteryImpl().crawl(1L, "001", url);
	}
}
