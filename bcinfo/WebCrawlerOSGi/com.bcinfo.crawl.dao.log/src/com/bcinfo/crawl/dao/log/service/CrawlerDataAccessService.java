/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-6-2 上午08:49:07<br>
 *         提供Derby数据库的操作<br>
 *         主要操作TWAP_PUBLIC_CRAWL_RESOURCE表，以供测试之用<br>
 */
public interface CrawlerDataAccessService {

	public Boolean save(Resource resource);
}
