/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.HtmlParse;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.core.ParseFactory;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;

/**
 * @author dongq
 * 
 *         create time : 2009-11-30 下午03:39:03<br>
 *         针对手机频道<br>
 */
public class WebCrawlerMobileZhutiImpl implements WebCrawler {

	private static final Logger log = Logger.getLogger(WebCrawlerMobileZhutiImpl.class);

	private HtmlParse htmlParse;
	private FileOperation fileOperation;
	private Parse parseService;

	public WebCrawlerMobileZhutiImpl() {
		this.htmlParse = new HtmlParseDefaultImpl();
		this.fileOperation = new FileOperation();
	}

	@Override
	public List<FolderBO> crawl(String folderId, String url) {
		List<FolderBO> folders = null;

		try {
			// 取得可用地址
			List<FolderBO> usableFolders = htmlParse.getUsableCrawlLink(folderId, url);
			parseService = ParseFactory.getParseInstance(url);
			if (parseService != null) {
				folders = new ArrayList<FolderBO>();
				int count = 1;
				for (FolderBO folder : usableFolders) {
					String title = folder.getTitle();
					String link = folder.getLink();
					String content = parseService.parse(link);
					String imgPathSet = "";
					String filePathSet = "";
					// TODO 记录日志,不管成功与否
					fileOperation.writeLog(folderId, link);

					if (content != null && !"".equals(content)) {
						FolderBO usableFolder = new FolderBO();
						usableFolder.setTitle(title);
						usableFolder.setLink(link);
						usableFolder.setFolderId(folderId);
						String[] strs = content.split("\\|");
						for (int index = 0; index < strs.length; index++) {
							switch (index) {
							case 0:
								imgPathSet = strs[index];
								break;
							case 1:
								content = strs[index];
								break;
							case 2:
								filePathSet = strs[index];
								break;
							default:
								break;
							}
						}
						usableFolder.setFilePathSet(filePathSet);
						usableFolder.setImgPathSet(imgPathSet);
						usableFolder.setContent("<img src=\""+imgPathSet.replaceAll(",", "")+"\" alt=\"pic\" />"+content);
						
						folders.add(usableFolder);
					}

					count++;
				}
			}
		} catch (Exception e) {
			log.info("[目标栏目:" + folderId + " " + url + "]抓取失败");
			e.printStackTrace();
			log.error(e);
		}

		return folders;
	}

}
