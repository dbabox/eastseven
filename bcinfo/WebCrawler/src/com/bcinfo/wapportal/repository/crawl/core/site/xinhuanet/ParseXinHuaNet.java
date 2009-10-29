/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:56:56
 */
public class ParseXinHuaNet extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseXinHuaNet.class);
	
	private ParagraphTag paragraphTag;
	
	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}
	
	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
			
			String content = this.getPageContent(link, "id", "Content");
			if(content!=null&&!"".equals(content)){
				//Div id=Content ��ҳ����ڷ�ҳ
				Parser parser = new Parser();
				parser.setInputHTML(content);
				NodeList nodeList = parser.parse(null);
				if(nodeList!=null && nodeList.size()>0){
					NodeIterator iter = nodeList.elements();
					while(iter.hasMoreNodes()){
						Node node = iter.nextNode();
						if(node instanceof ParagraphTag){
							ParagraphTag p = (ParagraphTag)node;
							if("pagelink".equals(p.getAttribute("class")))
								this.paragraphTag = p;
						}
					}
				}
				if(paragraphTag!=null){
					nodeList = paragraphTag.getChildren();
					NodeIterator iter = nodeList.elements();
					while(iter.hasMoreNodes()){
						Node node = iter.nextNode();
						if(node instanceof LinkTag){
							LinkTag linkTag = (LinkTag)node;
							if(!linkTag.getLinkText().contains("1"))
								links.add(linkTag.extractLink());
						}
					}
				}
			}
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("ȡ�»���["+link+"]��ҳʧ��");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "class", "xilanwz-x");
			if("".equals(content)){
				content = this.getPageContent(link, "id", "Content");
				if(this.paragraphTag!=null){
					content = content.replaceAll(this.paragraphTag.toHtml(), replacement);
				}
			}
			if("".equals(content)){
				content = this.getPageContent(link, "id", "xhw");
			}
			if("".equals(content)){
				content = this.getPageContent(link, "td", "id", "xhw");
			}
			content = this.commonParseContent(content);
			
			//�»���
			content = content.replaceAll("\\[����.*��̳\\]", replacement);
			content = content.replaceAll("�������Ĺ۵㡣�����������������Է��Բ�������ع涨����ע��󷢱����ۡ�  ������֪", replacement);
			content = content.replaceAll("=", replacement);
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			//��ҳ��ǩ:[1][2][3][4]
			content = content.replaceAll("\\[\\d+\\]", replacement);
			if(log.isDebugEnabled()){
				//System.out.println("------------------------"+link+"-------------------------------");
				//System.out.println(content);
			}
		}catch(Exception e){
			System.out.println("�����»���ҳ��["+link+"]����ʧ��");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return content;
	}

}
