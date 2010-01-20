/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-12-22 下午02:38:37<br>
 *         手机频道：软件下载<br>
 *         sj.skycn.com<br>
 */
public class ParseSkycn extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(ParseSkycn.class);

	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			log.info("取[" + link + "]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {

			String imgPathSet = "";
			String softContent = "";
			String filePathSet = "";
			
			Parser parser = new Parser(link);
			parser.setEncoding("GBK");
			
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Div div = (Div)iter.nextNode();
					if("col1".equals(div.getAttribute("class"))){
						//图片
						NodeList list = div.searchFor(ImageTag.class, true);
						ImageTag imageTag = (ImageTag)list.elementAt(0);
						if(imageTag.getImageURL().contains("pic_down")) continue;
						imgPathSet = imageTag.getImageURL() + ",";
						
					}else if("col2".equals(div.getAttribute("class"))){
						//文字
						NodeList list = div.searchFor(TableColumn.class, true);
						if(list!=null&&list.size()>0){
							NodeIterator _iter = list.elements();
							while(_iter.hasMoreNodes()){
								TableColumn td = (TableColumn)_iter.nextNode();
								if("3".equals(td.getAttribute("colspan"))){
									softContent = td.getChildrenHTML();
									softContent = softContent.replaceAll("\\|", ",");
									softContent = softContent + "<br/>";
									break;
								}
							}
						}
					}else if("buttons".equals(div.getAttribute("class"))){
						//文件
						NodeList list = div.searchFor(LinkTag.class, true);
						if(list!=null&&list.size()>0){
							NodeIterator _iter = list.elements();
							while(_iter.hasMoreNodes()){
								LinkTag linkTag = (LinkTag)_iter.nextNode();
								String href = linkTag.getAttribute("href");
								if(!"".equals(href)&&href.contains("down.php?id")){
									filePathSet = CrawlerUtil.getFileDownloadAddress(linkTag.extractLink());
									break;
								}
							}
						}
					}else if("intro".equals(div.getAttribute("id"))){
						softContent += this.commonParseContent(div.toHtml());
					}
				}
			}
			
			content = "";
			content += imgPathSet + "|";
			content += softContent + "|";
			content += filePathSet.replaceAll(" ", "_");
			
		} catch (Exception e) {
			log.info("解析页面[" + link + "]内容失败");
			if (log.isDebugEnabled())
				e.printStackTrace();
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

	// TODO TEST
	public static void main(String[] args) {
		String link = "http://sj.skycn.com/p/15/15521/index.shtml";
		Parse p = new ParseSkycn();
		System.out.println(p.parse(link));
	}
}
