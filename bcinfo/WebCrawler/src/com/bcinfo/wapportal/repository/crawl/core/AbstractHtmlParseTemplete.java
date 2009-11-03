/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����12:09:22<br>
 *         ��ҳ������ģ���࣬���н���ʵ���඼Ӧ�ü̳д���<br>
 */
public abstract class AbstractHtmlParseTemplete {

	private static Logger log = Logger.getLogger(AbstractHtmlParseTemplete.class);
	
	public final String replacement = "";
	
	/**
	 * �ṩһ��ָ���ԵĽ������,����������������ҳ��,��ͨ��ʵ��Parse�ӿ��еķ�����ʵ��
	 * @param link
	 * @return
	 */
	public String simpleParse(String link){
		String content = null;
		
		try{
			//ȡĿ��ҳ���ڵķ�ҳ����û�з�ҳ����Ӧ�÷��ذ����Լ�link��List����
			List<String> links = checkPageOfLinks(link);
			content = "";
			
			content = mergeTargetContent(link, links);
		}catch(Exception e){
			System.out.println("ҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return content;
	}
	
	/**
	 * ͳһ��ʽ���ı�����
	 * @param targetCnt
	 * @return
	 */
	public String commonParseContent(String targetCnt) {
		String content = targetCnt;
		
		try{
			content = content.replaceAll(RegexUtil.REGEX_COMMENT, replacement);
			//SCRIPT
			content = removeTag(content);
			//STRONG
			content = content.replaceAll(RegexUtil.REGEX_STRONG, replacement);
			//CENTER
			content = content.replaceAll(RegexUtil.REGEX_CENTER, replacement);
			//A
			content = content.replaceAll(RegexUtil.REGEX_A, replacement);
			//P end
			content = content.replaceAll(RegexUtil.REGEX_P_END, replacement);
			//FONT
			content = content.replaceAll(RegexUtil.REGEX_FONT, replacement);
			//B
			content = content.replaceAll("<B>|<b>|</B>|</b>", replacement);
			//P to BR
			content = content.replaceAll(RegexUtil.REGEX_P_START, RegexUtil.REGEX_BR);
			content = content.replaceAll(RegexUtil.REGEX_P_START_NO_ATTR, RegexUtil.REGEX_BR);
			content = content.replaceAll("<br>", RegexUtil.REGEX_BR);
			content = content.replaceAll("<br />", RegexUtil.REGEX_BR);
			content = content.replaceAll("<[bB][rR]>", RegexUtil.REGEX_BR);
			//��������
			content = content.replaceAll("�����ǰ��ԡ���ӰѶ���ӽ�Ŀ�����ֻ�����������Ƶ�� ent.sina.cn", replacement);
			content = content.replaceAll("(���Сͼ����ͼ)", replacement);
			content = content.replaceAll("����˴��鿴����ͼƬ", replacement);
			//�Ѻ�����
			content = content.replaceAll("<SOHUADCODE></SOHUADCODE>", replacement);
			//TABLE
			content = content.replaceAll(RegexUtil.REGEX_TABLE_ALL, replacement);
			//DIV
			content = content.replaceAll(RegexUtil.REGEX_DIV, replacement);
			//IFRAME
			content = content.replaceAll(RegexUtil.REGEX_IFRAME, replacement);
			//LI
			content = content.replaceAll("<[lL][iI]\\s+[^>]+>|<[lL][iI]>|</[lL][iI]>", replacement);
			//UL
			content = content.replaceAll("<[uU][lL]\\s+[^>]+>|<[uU][lL]>|</[uU][lL]>", replacement);
			//��ʽ��
			content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ESC_SPACE, replacement);
			content = content.replaceAll(RegexUtil.REGEX_FORMAT_SPACE, "><");
			content = CrawlerUtil.formatSpecialWords(content);
			
		}catch(Exception e){
			System.out.println("����ʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return content;
	}
	
	/**
	 * ɾ��ָ����ǩ����
	 * @param targetCnt
	 * @return
	 */
	@Deprecated
	public synchronized String removeTag(String targetCnt){
		try{
			String content = "";
			Parser parser = new Parser();
			parser.setInputHTML(targetCnt);
			NodeList nodeList = parser.parse(null);
			
			NodeIterator iter = nodeList.elements();
			while(iter.hasMoreNodes()){
				Node node = iter.nextNode();
				
				if(!(node instanceof ScriptTag) && !(node instanceof StyleTag) && !(node instanceof RemarkNode)){

					if(node instanceof Span){
						Span span = (Span)node;
						String value = span.getAttribute("class");
						if("contentPlayer".equals(value)) continue;
					}else if(node instanceof Div){
						Div div = (Div)node;
						String value = div.getAttribute("class");
						if("otherContent_01".equals(value)) continue;
						else if("blk-video".equals(value)) continue;
						else content += div.toHtml().trim();
					}else if(node instanceof ImageTag){
						ImageTag img = (ImageTag)node;
						if(img.getImageURL().contains("c.gif")) continue;
						else content += img.toHtml().trim();
					}else if(node instanceof LinkTag){
						LinkTag linkTag = (LinkTag)node;
						String value = linkTag.extractLink();
						if(value.contains("nba_in_wap.html")) continue;
						else content += linkTag.toHtml().trim();
					}else{
						String text = node.toHtml().trim();
						content += text;
					}
				}
				
			}
			
			return content;
		}catch(Exception e){
			System.out.println("ȥ��ָ����ǩʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
			return targetCnt;
		}
	}
	
	/**
	 * �ϲ�һ��ҳ�����з�ҳ������
	 * @param link
	 * @param links
	 * @return
	 */
	public String mergeTargetContent(String link,List<String> links){
		String content = null;
		
		try{
			content = "";
			for(String pageLink : links){
				content += getTargetContent(pageLink);
			}
		}catch(Exception e){
			System.out.println("�ϲ�ҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return content;
	}
	
	/**
	 * ȡ��ָ����ǩ�µ�����<br>
	 * Ŀǰ����ȡ�õı�ǩ�У�<br>
	 * Div<br>
	 * TableColumn<br>
	 * ���µ����󣬿�������ӱ�ǩ<br>
	 * @param link
	 * @param tagName ��ǩ��
	 * @param attrName ��ǩ����
	 * @param attrValue ��ǩ����ֵ
	 * @return 
	 * @throws Exception ""
	 */
	public String getPageContent(String link, String tagName, String attrName, String attrValue) throws Exception {
		String content = "";
		Parser parser = new Parser(link);
		NodeList nodeList = new NodeList();
		try{
			nodeList = parser.extractAllNodesThatMatch(new TagNameFilter(tagName));
		}catch(EncodingChangeException e){
			parser.reset();
			parser.setEncoding("GBK");
			nodeList = parser.extractAllNodesThatMatch(new TagNameFilter(tagName));
		}
		try{
			if(nodeList!=null&&nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Node node = iter.nextNode();
					if(node instanceof Div){
						Div div = (Div)node;
						if(attrValue.equals(div.getAttribute(attrName))){
							if(div.getChildCount()>0)
								content += div.getChildrenHTML();
							else
								content += div.toHtml();
						}
					}else if(node instanceof TableColumn){
						TableColumn td = (TableColumn)node;
						if(attrValue.equals(td.getAttribute(attrName))){
							if(td.getChildCount()>0)
								content += td.getChildrenHTML();
							else
								content += td.toHtml();
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println("ȡ��ָ����ǩ�µ�����["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return content;
	}
	
	/**
	 * ���ݸ�����link����ȡָ��DIV�µ�����<br>
	 * �磺�Ѻ�����ҳ�������Ǵ����idΪcontentText��DIV�µģ�<br>
	 * 	       ��Ѷ��ҳ�������Ǵ����idΪArticleCnt��DIV�µ�<br>
	 *     
	 * @param link
	 * @param divAttrName
	 * @param divAttrValue
	 * @return û�еĻ�����""
	 * @throws Exception ""
	 */
	public String getPageContent(String link, String divAttrName, String divAttrValue) throws Exception {
		String content = "";
		Parser parser = new Parser(link);
		NodeList nodeList = new NodeList();
		try{
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
		}catch(EncodingChangeException e){
			//System.out.println(link+" ���뱻����ΪGBK");
			parser.reset();
			parser.setEncoding("GBK");
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
		}
		SimpleNodeIterator iter = nodeList.elements();
		while(iter.hasMoreNodes()){
			Div div = (Div)iter.nextNode();
			if (divAttrValue.equalsIgnoreCase(div.getAttribute(divAttrName))) {
				content = div.getChildren().toHtml().trim();
				break;
			}
		}
		return content;
	}
	
	/**
	 * ȡ��Ŀ��ҳ�������
	 * @param link
	 * @return
	 */
	public abstract String getTargetContent(String link);
	
	/**
	 * ȡĿ��ҳ���ڵķ�ҳ����û�з�ҳ����Ӧ�÷��ذ����Լ�link��List����
	 * @param link
	 * @return
	 */
	public abstract List<String> checkPageOfLinks(String link);
	
}
