/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.ArrayList;
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
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午12:09:22<br>
 *         网页解析的模板类，所有解析实现类都应该继承此类<br>
 */
public abstract class AbstractHtmlParseTemplete {

	private static Logger log = Logger.getLogger(AbstractHtmlParseTemplete.class);
	
	public final String replacement = "";
	
	/**
	 * 提供一个指导性的解析入口,若有特殊解析规则的页面,可通过实现Parse接口中的方法来实现
	 * @param link
	 * @return
	 */
	public String simpleParse(String link){
		String content = null;
		
		try{
			//取目标页面内的分页，若没有分页，则应该返回包含自己link的List集合
			List<String> links = checkPageOfLinks(link);
			content = "";
			
			content = mergeTargetContent(link, links);
		}catch(Exception e){
			////System.out.println("页面["+link+"]解析失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}
	
	/**
	 * 统一格式化文本内容
	 * @param targetCnt
	 * @return
	 */
	public String commonParseContent(String targetCnt) {
		String content = targetCnt;
		
		try{
			content = content.replaceAll(RegexUtil.REGEX_COMMENT, replacement);
			//SCRIPT
			//String regex = "(<!-{2,}.*?-{2,}>)|(<[sS][tT][yY][lL][eE]>.*?</[sS][tT][yY][lL][eE]>)|((<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>|<[sS][cC][rR][iI][pP][tT]>).*?</[sS][cC][rR][iI][pP][tT]>)";
			//content = content.replaceAll(regex, replacement);
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
			content = content.replaceAll("<[bB]>|<[bB]\\s+[^>]+>|</[bB]>", replacement);
			//TODO 新浪内容
			content = content.replaceAll("看明星八卦、查影讯电视节目，上手机新浪网娱乐频道 ent.sina.cn", replacement);
			content = content.replaceAll("(点击小图看大图)", replacement);
			content = content.replaceAll("点击此处查看其它图片", replacement);
			content = content.replaceAll("我要评论", replacement);
			content = content.replaceAll("新浪声明：此消息系转载自新浪合作媒体，新浪网登载此文出于传递更多信息之目的，并不意味着赞同其观点或证实其描述。文章内容仅供参考，不构成投资建议。投资者据此操作，风险自担。", replacement);
			//TODO 搜狐内容
			content = content.replaceAll("搜狐体育讯", replacement);
			content = content.replaceAll("<SOHUADCODE></SOHUADCODE>", replacement);
			content = content.replaceAll("点击图片进入下一页", replacement);
			content = content.replaceAll("我来说两句： 查看评论", replacement);
			content = content.replaceAll("<SOHU_AD_LAST>", replacement);
			content = content.replaceAll("在这里发表评论！", replacement);
			//TODO 文字内容
			content = content.replaceAll("与.*?关系最密切的\\d+个人\\(图解\\)", replacement);
			content = content.replaceAll("NBA频道", replacement);
			content = content.replaceAll("NBA数据库", replacement);
			content = content.replaceAll("NBA视频直播", replacement);
			content = content.replaceAll("NBA视频点播", replacement);
			content = content.replaceAll("NBA互动直播", replacement);
			//FORM
			content = content.replaceAll(RegexUtil.REGEX_FORM, replacement);
			//INPUT
			content = content.replaceAll(RegexUtil.REGEX_INPUT, replacement);
			//TEXTAREA
			content = content.replaceAll(RegexUtil.REGEX_TEXTAREA, replacement);
			//TODO TOM内容
			content = content.replaceAll("声明：泛华娱乐提供专稿，未经tom授权，严禁转载，违者必究！", replacement);
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
			//U
			content = content.replaceAll("<[uU]\\s+[^>]+>|<[uU]>|</[uU]>", replacement);
			//H
			content = content.replaceAll("<[hH]\\d+>|</[hH]\\d+>", replacement);
			//HR
			content = content.replaceAll("<[hH][rR]>|</[hH][rR]>", replacement);
			//P to BR
			content = content.replaceAll(RegexUtil.REGEX_P_START, RegexUtil.REGEX_BR);
			content = content.replaceAll(RegexUtil.REGEX_P_START_NO_ATTR, RegexUtil.REGEX_BR);
			content = content.replaceAll("<br>", RegexUtil.REGEX_BR);
			content = content.replaceAll("<br />", RegexUtil.REGEX_BR);
			content = content.replaceAll("<[bB][rR]>", RegexUtil.REGEX_BR);
			//&#8212;
			content = content.replaceAll("&#8212;", "-");
			//&hellip;
			content = content.replaceAll("&hellip;", "...");
			//&rdquo;
			content = content.replaceAll("&rdquo;", "\"");
			//&ldquo;
			content = content.replaceAll("&ldquo;", "\"");
			
			//SPAN
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			
			//格式化
			content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ESC_SPACE, replacement);
			content = content.replaceAll(RegexUtil.REGEX_FORMAT_SPACE, "><");
			
			//再次剔除SCRIPT
			//content = content.replaceAll(regex, replacement);
			
			//特殊字符
			content = CrawlerUtil.formatSpecialWords(content);
			
		}catch(Exception e){
			//System.out.println("处理失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}
	
	public synchronized String removeScritpTag(String link){
		String content = "";
		try{
			Parser parser = new Parser(link);
			parser.setEncoding("GBK");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class));
			List<ScriptTag> list = new ArrayList<ScriptTag>();
			if(nodeList!=null && nodeList.size()>0){
				for(int index=0;index<nodeList.size();index++){
					list.add((ScriptTag)nodeList.elementAt(index));
				}
			}
			if(list!=null && !list.isEmpty()){
				parser = new Parser(link);
				parser.setEncoding("GBK");
				nodeList = parser.extractAllNodesThatMatch(null);
				for(ScriptTag node : list){
					nodeList.remove(node);
				}
				content = nodeList.toHtml();
			}
			return content;
		}catch(Exception e){
			if(log.isDebugEnabled()) e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 删除指定标签对象
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
			//System.out.println("去除指定标签失败");
			if(log.isDebugEnabled()) e.printStackTrace();
			return targetCnt;
		}
	}
	
	/**
	 * 合并一个页面内有分页的链接
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
			//System.out.println("合并页面["+link+"]解析失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}
	
	/**
	 * 取得指定标签下的内容<br>
	 * 目前可以取得的标签有：<br>
	 * Div<br>
	 * TableColumn<br>
	 * Span<br>
	 * 有新的需求，可自行添加标签<br>
	 * @param link
	 * @param tagName 标签名
	 * @param attrName 标签属性
	 * @param attrValue 标签属性值
	 * @return ""
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
					}else if(node instanceof Span){
						Span span = (Span)node;
						if(attrValue.equals(span.getAttribute(attrName))){
							if(span.getChildCount()>0){
								content += span.getChildrenHTML();
							}else{
								content += span.toHtml();
							}
						}
					}else if(node instanceof TableTag){
						TableTag table = (TableTag)node;
						if(attrValue.equals(table.getAttribute(attrName))){
							if(table.getChildCount()>0){
								content += table.getChildrenHTML();
							}else{
								content += table.toHtml();
							}
						}
					}
				}
			}
		}catch(Exception e){
			//System.out.println("取得指定标签下的内容["+link+"]解析失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		
		return content;
	}
	
	/**
	 * 根据给定的link，爬取指定DIV下的内容<br>
	 * 如：搜狐网的页面正文是存放在id为contentText的DIV下的；<br>
	 * 	       腾讯的页面正文是存放在id为ArticleCnt的DIV下的<br>
	 *     
	 * @param link
	 * @param divAttrName
	 * @param divAttrValue
	 * @return 没有的话返回""
	 * @throws Exception ""
	 */
	public String getPageContent(String link, String divAttrName, String divAttrValue) throws Exception {
		String content = "";
		Parser parser = new Parser(link);
		NodeList nodeList = new NodeList();
		try{
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
		}catch(EncodingChangeException e){
			////System.out.println(link+" 编码被设置为GBK");
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
	 * 取得目标页面的内容
	 * @param link
	 * @return
	 */
	public abstract String getTargetContent(String link);
	
	/**
	 * 取目标页面内的分页，若没有分页，则应该返回包含自己link的List集合
	 * @param link
	 * @return
	 */
	public abstract List<String> checkPageOfLinks(String link);
	
}
