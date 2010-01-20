/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-12-22 下午12:48:49<br>
 *         手机频道：软件下载<br>
 *         soft.159.com<br>
 */
public class Parse159 extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(Parse159.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("取["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			String imgPathSet = "";
			String softContent = "";
			String filePathSet = "";
			
			Parser parser = new Parser(link);
			parser.setEncoding("GBK");
			//图片
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Div div = (Div)iter.nextNode();
					String attrValue = div.getAttribute("class");
					if(!"".equals(attrValue)&&"center1_3".equals(attrValue)){
						NodeList list = div.searchFor(ImageTag.class, true);
						if(list!=null&&list.size()>0){
							NodeIterator _iter = list.elements();
							while(_iter.hasMoreNodes()){
								ImageTag imageTag = (ImageTag)_iter.nextNode();
								imgPathSet = imageTag.getImageURL()+",";
								break;
							}
						}
						break;
					}
				}
			}
			//介绍
			softContent = this.getPageContent(link, "class", "softInfoCon");
			softContent = this.commonParseContent(softContent);
			softContent = softContent.replaceAll("\\|", ",");
			
			//文件
			parser.reset();
			parser.setURL(link);
			parser.setEncoding("GBK");
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String value = linkTag.getLinkText();
					if(!"".equals(value)&&value.contains("[本地下载]")){
						filePathSet = linkTag.extractLink();
						break;
					}
				}
			}
			if(!"".equals(filePathSet)){
				parser.reset();
				parser.setURL(filePathSet);
				parser.setEncoding("GBK");
				System.out.println(parser.parse(null).toHtml());
			}
			
			content = "";
			content += imgPathSet + "|";
			content += softContent + "|";
			content += filePathSet;
			
		}catch(Exception e){
			log.info("解析页面["+link+"]内容失败");
			e.printStackTrace();
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
		String link = "http://soft.159.com/soft/200912/15216.html";
		//Parse159 p = new Parse159();
		//System.out.println(p.parse(link));
		try {
			//http://soft.159.com/DownSof.aspx?MobleId=15216&t=net
			link = "http://soft.159.com/DownSof.aspx?MobleId=15216&t=net";
			URL url = new URL(link);
			URLConnection http = url.openConnection();
			System.out.println(http.getURL());;
			InputStream is = http.getInputStream();
			if(is != null){
				System.out.println(is);
			}else{
				System.out.println("InputStream is null");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
