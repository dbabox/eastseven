/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;




/**
 * @author dongq
 * 
 *         create time : 2009-10-22 ÉÏÎç11:58:15
 */
public class MainTest {

	public static void main(String[] args) throws Exception{
		String url = "http://www.news.cn/xinhua/";
		Parser parser = new Parser(url);
		parser.setEncoding("GBK");
		NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
		if(nodeList!=null && nodeList.size()>0){
			NodeIterator iter = nodeList.elements();
			while(iter.hasMoreNodes()){
				LinkTag linkTag = (LinkTag)iter.nextNode();
				String title = linkTag.getLinkText();
				String link = linkTag.extractLink();
				System.out.println(link+" | "+title);
				if(link.contains("xinhuanet.com")){
					
				}else if(link.contains("www.news.cn")){
					parser = new Parser(link);
					parser.setEncoding("GBK");
					NodeList _nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(TableTag.class));
					if(_nodeList!=null && _nodeList.size()>0){
						NodeIterator it = _nodeList.elements();
						while(it.hasMoreNodes()){
							TableTag tableTag = (TableTag)it.nextNode();
							if("txt".equals(tableTag.getAttribute("class"))){
							System.out.println(tableTag.toHtml());
							System.out.println("");}
						}
					}
				}
			}
		}
	}

}
