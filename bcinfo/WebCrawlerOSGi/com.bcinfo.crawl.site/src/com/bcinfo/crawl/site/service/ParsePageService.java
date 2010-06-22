/**
 * 
 */
package com.bcinfo.crawl.site.service;

import com.bcinfo.crawl.domain.model.Resource;


/**
 * @author dongq
 * 
 *         create time : 2010-5-17 обнГ09:32:21<br>
 */
public interface ParsePageService {

	public Resource getPageContent(String link);
}
