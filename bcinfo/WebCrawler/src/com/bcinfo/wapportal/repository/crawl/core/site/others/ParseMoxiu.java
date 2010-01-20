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
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-11-30 下午02:33:46<br>
 *         手机频道:手机主题下载<br>
 */
public class ParseMoxiu extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseMoxiu.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			//System.out.println("取MOXIU[" + link + "]分页失败");
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
			
			//取图片地址
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					ImageTag imageTag = (ImageTag)iter.nextNode();
					if("imgshow".equals(imageTag.getAttribute("id"))){
						imgPathSet += imageTag.getImageURL() + ",";
						break;
					}
				}
			}

			//取机型描述
			parser.reset();
			nodeList = parser.extractAllNodesThatMatch(new TagNameFilter("li"));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					Node node = iter.nextNode();
					if(node instanceof Bullet){
						Bullet bullet = (Bullet)node;
						String attrValue = bullet.getAttribute("class");
						if("all".equals(attrValue)&&bullet.getStringText().contains("机型")){
							mobileType = bullet.getStringText();
							break;
						}
					}
				}
			}
			
			//取下载主题文件的路径
			parser.reset();
			nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					if("免费下载到电脑".equals(linkTag.getLinkText())){
						filePathSet += linkTag.extractLink() + ",";
						break;
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
				//content += filePathSet.replaceAll("=", filePathSet);
				filePathSet = "http://d1.tel.moxiu.com:8080/" + filePathSet.substring(filePathSet.lastIndexOf("=")+1);
				content += filePathSet;
			}
		} catch (Exception e) {
			//System.out.println("解析MOXIU页面[" + link + "]内容失败");
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
		//http://www.moxiu.com/themes/22/2009/1123/5889778.shtml
		//http://www.moxiu.com/themes/2/2009/1128/5925650.shtml
		
		//http://d1.tel.moxiu.com:8080/theme/sisdd/fe/9f3/fe9f3ed6/dream6_lyt.sis
		//http://www.moxiu.com/down.html?rid=5925650&file=theme/sisdd/fe/9f3/fe9f3ed6/dream6_lyt.sis
		String link = "http://www.moxiu.com/themes/2/2009/1128/5925650.shtml";
		ParseMoxiu p = new ParseMoxiu();
		System.out.println(p.parse(link));
	}
}
