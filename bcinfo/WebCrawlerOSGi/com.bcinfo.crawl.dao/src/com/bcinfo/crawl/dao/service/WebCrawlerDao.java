/**
 * 
 */
package com.bcinfo.crawl.dao.service;

import java.sql.Connection;

import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-5-14 ����10:06:51
 */
public interface WebCrawlerDao {

	public String getSystemDate(String pattern);
	
	/**
	 * ����
	 * @param resource
	 * @param conn
	 * @return
	 */
	public Boolean save(Resource resource, Connection conn);
	
	/**
	 * ��������
	 * @param resources
	 * @param conn
	 * @return
	 */
	public Boolean saveBatch(Resource[] resources, Connection conn);
	
	public Boolean saveBatch(Object[] resources, Connection conn);
	
	/**
	 * ��Դ�Ƿ��Ѿ�����
	 * @param resource
	 * @return true-���ڣ�false-������
	 */
	public Boolean isExist(Resource resource);
}
