/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.sina;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceFileImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:53:52
 */
public class ParseSina extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseSina.class);
	
	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}
	
	@Override
	public Boolean parse(String folderId, String title, String link) {
		Boolean bln = false;
		
		try{
			//抓取内容
			String content = this.simpleParse(link);
			//分段处理
			
			//保存入库
			DaoService daoService = new DaoServiceFileImpl();
			List<FolderBO> folders = new ArrayList<FolderBO>();
			folders.add(new FolderBO(folderId, title, link, content));
			bln = daoService.saveCrawlResource(folders);
		}catch(Exception e){
			log.info("页面["+link+"]["+title+"]解析失败");
			if(log.isDebugEnabled()) log.error(e);
		}
		
		return bln;
	}
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			System.out.println("取sina["+link+"]分页失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		
		try{
			content = this.getPageContent(link, "id", "artibody");
			content = this.commonParseContent(content);
			//内容中包含脚本标签
			if(content.contains("<style") 
					|| content.contains("<span") 
					|| content.contains("<script")
					|| content.contains("<script language=\"javascript\" type=\"text/javascript\">")){
				
				content = content.replaceAll("已有<span class=\"f_c00\">_COUNT_</span>条评论</span>我要评论", replacement);
				content = RegexUtil.replaceAll("<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>.*?</[sS][cC][rR][iI][pP][tT]>", content);
				content = RegexUtil.replaceAll("<[sS][tT][yY][lL][eE]\\s+[^>]+>.*?</[sS][tT][yY][lL][eE]>", content);
				content = RegexUtil.replaceAll("<[sS][tT][yY][lL][eE]>.*?</[sS][tT][yY][lL][eE]>", content);
				content = RegexUtil.replaceAll("<[sS][pP][aA][nN]\\s+[^>]+>.*?</[sS][pP][aA][nN]>", content);
				
				content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			}
			content = content.replaceAll("\\(\\)", replacement);
			
			//格式化
			/**/
			content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ESC_SPACE, replacement);
			content = content.replaceAll(RegexUtil.REGEX_FORMAT_SPACE, replacement);
		}catch(Exception e){
			System.out.println("解析sina页面["+link+"]内容失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return content;
	}

}
