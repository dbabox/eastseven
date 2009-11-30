/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.impl.DaoServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-16 ����02:55:37<br>
 *         �Ѻ�����ռ��
 */
public class WebCrawlerAstroSohuImpl extends AbstractHtmlParseTemplete
		implements WebCrawler {

	private static final Logger log = Logger
			.getLogger(WebCrawlerAstroSohuImpl.class);

	@Override
	public List<String> checkPageOfLinks(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;

		try {
			content = this.getPageContent(link, "id", "contentText");
			if (content == null || "".equals(content))
				content = this.getPageContent(link, "td", "class", "wz12_3B07");

			content = this.commonParseContent(content);

			content = content.replaceAll(RegexUtil.REGEX_SPAN, replacement);
			content = content.replaceAll(RegexUtil.REGEX_EM, replacement);
			content = content.replaceAll("\\[����˵����\\]", replacement);
			content = RegexUtil.eliminateString(RegexUtil.REGEX_SCRIPT_START,
					RegexUtil.REGEX_SCRIPT_END, content);
		} catch (Exception e) {
			System.out.println("����sohuҳ��[" + link + "]����ʧ��");
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
		}

		return content;
	}

	@Override
	public List<FolderBO> crawl(String folderId, String url) {
		List<FolderBO> folders = null;

		try {
			String link = url;
			String content = "";
			
			content = getTargetContent(link);
			if(content == null || "".equals(content)) return null;
			//������������Դ�ı���
			String title = "";
			title = RegexUtil.extractContentWithRegex("\\d+��\\d+��\\d+��", content);
			DaoService dao = new DaoServiceDefaultImpl();
			//title = dao.getChannelName(Long.parseLong(folderId));
			
			if(dao.isExistCrawlResource(Long.parseLong(folderId), title)){
				log.info("Ƶ��["+folderId+"]����ռ������["+title+"]�Ѵ���");
				return null;
			}else{
				//TODO ɾ�����е����ݣ�������������Ŀֻ��һ�����µļ�¼
				if(dao.deleteCrawlResource(Long.parseLong(folderId))){
					log.info("Ƶ��["+folderId+"]����ռ��������������ɹ�");
				}else{
					log.info("Ƶ��["+folderId+"]����ռ�������������ʧ��");
				}
			}
			
			folders = new ArrayList<FolderBO>();
			folders.add(new FolderBO(folderId, title, link, content));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		return folders;
	}
	
	//http://act1.astro.women.sohu.com/yuncheng_xingzuo_new.php?type=d&xingzuo=1
	public static void main(String[] args) {
		String link = "http://act1.astro.women.sohu.com/yuncheng_xingzuo_new.php?type=d&day_select=tomorrow&xingzuo=1";
		WebCrawlerAstroSohuImpl crawler = new WebCrawlerAstroSohuImpl();
		String content = crawler.getTargetContent(link);
		System.out.println(content);
		String title = RegexUtil.extractContentWithRegex("\\d+��\\d+��\\d+��", content);
		System.out.println(title);
	}
}
