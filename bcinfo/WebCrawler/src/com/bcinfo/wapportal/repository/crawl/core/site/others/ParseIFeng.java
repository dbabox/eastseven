/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-12-1 ÉÏÎç10:21:00<br>
 *         ·ï»ËÍø<br>
 */
public class ParseIFeng extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(ParseIFeng.class);
	
	private Parser parser;
	
	public ParseIFeng(String url) {
		try {
			this.parser = new Parser(url);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
		}
	}
	
	public ParseIFeng() {
	}
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = new ArrayList<String>();
		String pageNumStr = "1";
		try{
			links.add(link);
			if(this.parser==null) this.parser = new Parser(link);
			NodeFilter filter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "slide_org"));
			NodeList nodeList = this.parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null && nodeList.size()>0){
				Node node = nodeList.elements().nextNode();
				Tag tag = (Tag)node;
				log.info("·ÖÒ³£º"+tag.getFirstChild().getText());
				pageNumStr = tag.getFirstChild().getText();
				int pageNum = Integer.parseInt(pageNumStr);
				String next = "";
				for(int index=1;index<pageNum;index++){
					if(index==1){
						next = getNextLink(link);
					}else{
						next = getNextLink(next);
					}
					if(!"".equals(next)){
						links.add(next);
					}
				}
			}
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("È¡·ï»ËÍø["+link+"]·ÖÒ³Ê§°Ü");
		}
		return links;
	}

	private String getNextLink(String link) {
		String next = "";
		try {
			this.parser = new Parser(link);
			NodeFilter filter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "nextPage"));
			NodeList nodeList = this.parser.parse(filter);
			if(nodeList!=null && nodeList.size()>0){
				Node node = nodeList.elements().nextNode();
				//System.out.println(node);
				NodeList list = node.getChildren().extractAllNodesThatMatch(new TagNameFilter("script"));
				for(NodeIterator iter = list.elements();iter.hasMoreNodes();){
					String html = iter.nextNode().toHtml();
					if(html.contains("nextgroup")){
						next = html.substring(html.indexOf("href=\""), html.indexOf("' + nextgroup + '\">")).replace("href=\"", this.replacement);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
		}
		return next;
	}
	
	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "artical_real");
			if(content==null||"".equals(content)){
				if(this.parser==null) this.parser = new Parser(link);
				this.parser.reset();
				NodeFilter filter = new AndFilter(new TagNameFilter("dl"), new HasAttributeFilter("class", "slideBigShow"));
				NodeList nodeList = this.parser.parse(filter);
				if(nodeList!=null && nodeList.size()>0){
					content = nodeList.toHtml();
					content = content.replaceAll("<[dD][lL]\\s+[^>]+>|</[dD][lL]>|<dt>|</dt>|<dd>|</dd>", replacement);
				}
			}
			content = this.commonParseContent(content);
			
		}catch(Exception e){
			log.info("½âÎö·ï»ËÍøÒ³Ãæ["+link+"]ÄÚÈÝÊ§°Ü");
		}
		
		return content;
	}

	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO TEST
	public static void main(String[] args) {
		String link = "http://ent.ifeng.com/photo/star/detail_2010_02/01/332678_0.shtml";
		ParseIFeng p = new ParseIFeng(link);
		System.out.println(p.parse(link));
	}
}
