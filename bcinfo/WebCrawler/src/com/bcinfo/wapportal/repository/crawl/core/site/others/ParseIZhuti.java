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
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-11-30 下午02:34:21<br>
 *         手机频道:手机主题下载<br>
 */
public class ParseIZhuti extends AbstractHtmlParseTemplete implements Parse{

	private static Logger log = Logger.getLogger(ParseIZhuti.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("取IZHUTI[" + link + "]分页失败");
			if(log.isDebugEnabled()) e.printStackTrace();
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			String imgPathSet = "";
			String mobileType = "";
			String filePathSet = "";
			
			Parser parser = new Parser(link);
			parser.setEncoding("GB2312");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Div div = (Div)iter.nextNode();
					if("zhuti_img show_img".equals(div.getAttribute("class"))){
						Node node = div.getChild(1);
						if(node instanceof LinkTag){
							Node _node = node.getFirstChild();
							if(_node instanceof ImageTag){
								ImageTag imageTag = (ImageTag)_node;
								imgPathSet += imageTag.getImageURL() + ",";
							}
						}
					}
				}
			}
			parser.reset();
			nodeList = parser.extractAllNodesThatMatch(new TagNameFilter("td"));
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Node node = iter.nextNode();
					if(node instanceof TableColumn){
						//height="25" class="font2" style="line-height:20px; padding-bottom:3px"
						TableColumn td = (TableColumn) node;
						if("25".equals(td.getAttribute("height"))&&"font2".equals(td.getAttribute("class"))&&"line-height:20px; padding-bottom:3px".equals(td.getAttribute("style"))){
							mobileType = td.toHtml();
						}
					}
				}
			}
			
			parser.reset();
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList != null && nodeList.size() > 0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String text = linkTag.getAttribute("href");
					if(text!=null&&text.contains("download.php")){
						filePathSet += linkTag.extractLink() + ",";
					}
				}
			}

			content = "";
			if(!"".equals(imgPathSet)){
				content += imgPathSet + "|";
			}
			if(!"".equals(mobileType)){
				content += this.commonParseContent(mobileType) + "|";
			}
			if(!"".equals(filePathSet)){
				content += filePathSet;
			}
			
		} catch (Exception e) {
			//System.out.println("解析IZHUTI页面[" + link + "]内容失败");
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

	//TODO TEST
	public static void main(String[] args) {
		//String link = "http://www.izhuti.com/zhuti/46507/";
		//ParseIZhuti p = new ParseIZhuti();
		//System.out.println(p.parse(link));
	}
}
