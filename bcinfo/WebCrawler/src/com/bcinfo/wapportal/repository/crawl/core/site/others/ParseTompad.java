/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-12-22 上午09:40:19<br>
 *         手机频道：软件下载<br>
 *         soft.tompda.com<br>
 */
public class ParseTompad extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseTompad.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("取TOMPDA["+link+"]分页失败");
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
			//取下载地址
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String title = linkTag.getAttribute("title");
					if("[点击下载]".equals(title)){
						filePathSet = linkTag.extractLink();
						break;
					}
				}
			}
			if(!"".equals(filePathSet)){
				parser.reset();
				parser.setURL(filePathSet);
				parser.setEncoding("GBK");
				nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
				if(nodeList!=null && nodeList.size()>0){
					NodeIterator iter = nodeList.elements();
					while(iter.hasMoreNodes()){
						LinkTag linkTag = (LinkTag)iter.nextNode();
						String attrValue = linkTag.getAttribute("href");
						if(!"".equals(attrValue)&&attrValue.contains("ftp")){
							filePathSet = attrValue;
							break;
						}
					}
				}
				//System.out.println(nodeList.toHtml());
				
			}
			
			
			content = this.getPageContent(link, "id", "content_js");
			content = this.commonParseContent(content);
			String[] cnts = content.split(RegexUtil.REGEX_BR);
			for(int i=0;i<cnts.length;i++){
				String cnt = cnts[i].trim();
				if(!"".equals(cnt)){
					if(cnt.contains("<img")){
						//取图片地址
						imgPathSet = cnt + ",";
					}else{
						//取软件介绍
						softContent = cnt;
					}
				}
			}
			if("".equals(filePathSet)){
				return null;
			}else{
				content = "";
				content += imgPathSet+"|";
				content += softContent+"|";
				content += filePathSet;
			}
		}catch(Exception e){
			log.info("解析TOMPDA页面["+link+"]内容失败");
			if(log.isDebugEnabled()) e.printStackTrace();
			content = null;
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
		String link = "http://soft.tompda.com/c/softs/200833/s_11327.html";
		ParseTompad p = new ParseTompad();
		System.out.println(p.parse("\n"+link));
		
//		//ftp://softdown:softdown@wap.tompda.com/push/tool/DroidLive Player v2.0.1.apk
//		link = "ftp://softdown:softdown@wap.tompda.com/push/tool/DroidLive Player v2.0.1.apk";
//		try {
//			URL url = new URL(link);
//			URLConnection http = url.openConnection();
//			InputStream is = http.getInputStream();
//			if(is !=null){
//				System.out.println(is);
//				OutputStream os = new FileOutputStream(new File("C:/Download/DroidLive_Player_v2.0.1.apk"));
//				int bytesRead = 0;
//				byte[] buffer = new byte[8192];
//
//				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
//					os.write(buffer, 0, bytesRead);
//				}
//				is.close();
//				os.close();
//				System.out.println("down...");
//			}else{
//				System.out.println("InputStream is null");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
