/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.qq;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:51:34
 */
public class ParseQQForum extends AbstractHtmlParseTemplete implements Parse {

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
			//论坛帖子不存在分页问题
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取QQ论坛["+link+"]分页失败");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "main_content_div");
		}catch(Exception e){
			System.out.println("解析QQ论坛页面["+link+"]内容失败");
			content = null;
		}
		
		return content;
	}

}
