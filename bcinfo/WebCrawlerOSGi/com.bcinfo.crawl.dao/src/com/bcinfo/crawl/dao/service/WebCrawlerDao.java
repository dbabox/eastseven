/**
 * 
 */
package com.bcinfo.crawl.dao.service;

import java.sql.Connection;

import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-5-14 上午10:06:51
 */
public interface WebCrawlerDao {

	public String getSystemDate(String pattern);
	
	/**
	 * 保存
	 * @param resource
	 * @param conn
	 * @return
	 */
	public Boolean save(Resource resource, Connection conn);
	
	/**
	 * 批量保存
	 * @param resources
	 * @param conn
	 * @return
	 */
	public Boolean saveBatch(Resource[] resources, Connection conn);
	
	public Boolean saveBatch(Object[] resources, Connection conn);
	
	/**
	 * 资源是否已经存在
	 * @param resource
	 * @return true-存在；false-不存在
	 */
	public Boolean isExist(Resource resource);
}
