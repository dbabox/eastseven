/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.DebugUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-3 ����11:45:20
 */
public class ParsePcladyAstro extends AbstractHtmlParseTemplete implements
		Parse {

	private static Logger log = Logger.getLogger(ParsePcladyAstro.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
			
			String content = this.getPageContent(link, "id", "artNav");
			if(!"".equals(content)){
				Parser parser = new Parser();
				parser.setInputHTML(content);
				NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
				
				if(log.isDebugEnabled()) DebugUtil.printNodeList(nodeList);
				
				if(nodeList!=null && nodeList.size()>0){
					NodeIterator iter = nodeList.elements();
					while(iter.hasMoreNodes()){
						LinkTag linkTag = (LinkTag)iter.nextNode();
						if(!link.equals(linkTag.extractLink()))
							links.add(linkTag.extractLink());
					}
				}
			}
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("ȡASTRO.PCLADY[" + link + "]��ҳʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "class", "words");
			Parser parser = new Parser();
			parser.setInputHTML(content);
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(TableTag.class));
			if(nodeList!=null && nodeList.size()>0){
				 Node node = nodeList.elementAt(0);
				 content = node.toHtml();
			}
			
			content = this.commonParseContent(content);
			
			content = content.replaceAll("���ͼƬ������һҳ", replacement);
			content = content.replaceAll("&ldquo;", "��");
			content = content.replaceAll("&rdquo;", "��");
			content = content.replaceAll("&mdash;", "����");
			content = content.replaceAll("PClady����ר�� δ���������ת�أ�", replacement);
			content = content.replaceAll("\\(ͼƬ��Դ��PConline��Ӱ����\\)", replacement);
		} catch (Exception e) {
			//System.out.println("����ASTRO.PCLADYҳ��[" + link + "]����ʧ��");
			if(log.isDebugEnabled()) e.printStackTrace();
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

	// TODO ������
	public static void main(String[] args) {
		String url = "http://astro.pclady.com.cn/horoscopes/love/lovers/0910/464515.html";
		ParsePcladyAstro p = new ParsePcladyAstro();
		String cnt =  p.parse(url);
		//System.out.println(cnt);
	}
}
