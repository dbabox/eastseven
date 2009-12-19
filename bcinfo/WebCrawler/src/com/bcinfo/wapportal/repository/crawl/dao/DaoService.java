/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.util.List;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-15 ����01:42:08<br>
 *         ���ݿ���ز�������
 *
 */
public interface DaoService {

	public List<CrawlList> getCrawlLists(String status);

	public List<CrawlList> getCrawlLists(Long folderId);

	public Boolean isExistCrawlResource(Long channelId, String title);
	
	/**
	 * ȡ��Ƶ������
	 * @param channelId
	 * @return
	 */
	public String getChannelName(Long channelId);
	
	/**
	 * ����ץȡ��Դ
	 * @param folders
	 * @return
	 */
	public Boolean saveCrawlResource(List<FolderBO> folders);

	/**
	 * �������֮ǰ��ץȡ����
	 * @return
	 */
	public Boolean clearCrawlResource();
	
	/**
	 * ɾ��Ƶ���µ���������,���ô洢����ʵ��,�η�������
	 * @param channelId
	 * @return
	 */
	public Boolean deleteCrawlResource(Long channelId);
	
	/**
	 * ץȡ��־��¼
	 * @param appLog
	 * @return
	 */
	public Boolean saveLog(AppLog appLog);
	
	@Deprecated
	public Boolean isAnyTriggerFired();
	
	/**
	 * ��ʼ��Quartz���ݿ�
	 * @param sqlFile
	 * @return
	 */
	public Boolean initQuartzDatabase(String sqlFile);
	
}
