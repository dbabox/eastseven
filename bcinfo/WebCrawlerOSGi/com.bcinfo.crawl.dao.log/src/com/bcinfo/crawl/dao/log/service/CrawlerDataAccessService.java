/**
 * 
 */
package com.bcinfo.crawl.dao.log.service;

import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-6-2 ����08:49:07<br>
 *         �ṩDerby���ݿ�Ĳ���<br>
 *         ��Ҫ����TWAP_PUBLIC_CRAWL_RESOURCE���Թ�����֮��<br>
 */
public interface CrawlerDataAccessService {

	public Boolean save(Resource resource);
}
