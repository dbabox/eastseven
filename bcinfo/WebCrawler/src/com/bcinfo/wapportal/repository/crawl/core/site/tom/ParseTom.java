/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.tom;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:56:04
 */
public class ParseTom extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseTom.class);
	
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
			if(link.contains("yule.tom.com")){
				log.debug("   搜索第一页是否有分页标签 "+link);
				//搜索第一页是否有分页标签
				String content = this.getPageContent(link, "id", "content_body");
				content = this.commonParseContent(content);
				if(content.contains("<option")){
					String options = RegexUtil.extractContentWithRegex("<option\\s+[^>]+>", content);
					String[] tmp = options.split("<option");
					for(int i=0;i<tmp.length;i++){
						String var = tmp[i].trim();
						if(!var.contains("selected")&&var.length()>0){
							String pageLink = link.substring(0, link.lastIndexOf("/")+1) + var.substring(var.indexOf("'")+1,var.lastIndexOf("'"));
							links.add(pageLink);
							log.debug("   搜索第一页有分页标签 "+pageLink);
						}
					}
				}else{
					log.debug("   没有分页标签");
				}
			}else if(link.contains("images.yule.tom.com")){
				//TODO 暂时不处理
				//http://images.yule.tom.com/vw/158532-1.html
			}else{
				log.debug("   没有分页标签");
			}
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取TOM["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "content_body");
			content = this.commonParseContent(content);
			
			content = content.replaceAll("更多高清大图（\\d+张）", replacement);
			content = content.replaceAll("[进入图集]", replacement);
			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll(RegexUtil.REGEX_IFRAME, replacement);
			content = content.replaceAll("\\[\\]", replacement);
			content = content.replaceAll("\\(\\)", replacement);
			content = content.replaceAll("\\（\\）", replacement);
			content = content.replaceAll("\\(责编：.*\\)", replacement);
			content = content.replaceAll("<MUTILPAGENAV>", replacement);
			//去掉分页标签
			content = RegexUtil.eliminateString(RegexUtil.REGEX_SELECT_START, RegexUtil.REGEX_SELECT_END, content);
		}catch(Exception e){
			System.out.println("解析TOM页面["+link+"]内容失败");
		}
		
		return content;
	}

}
