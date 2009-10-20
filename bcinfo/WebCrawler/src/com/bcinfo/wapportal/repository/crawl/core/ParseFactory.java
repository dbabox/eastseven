/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQ;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQForum;
import com.bcinfo.wapportal.repository.crawl.core.site.sina.ParseSina;
import com.bcinfo.wapportal.repository.crawl.core.site.sohu.ParseSohu;
import com.bcinfo.wapportal.repository.crawl.core.site.tom.ParseTom;
import com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet.ParseXinHuaNet;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����01:18:16
 */
public final class ParseFactory {

	private static Logger log = Logger.getLogger(ParseFactory.class);
	
	public static Parse getParseInstance(String url) {

		if (url.contains(".qq.") && !url.contains("http://bbs.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseQQ");
			}
			return new ParseQQ();
		} else if (url.contains(".qq.") && url.contains("http://bbs.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseQQForum");
			}
			return new ParseQQForum();
		} else if (url.contains(".tom.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseTom");
			}
			return new ParseTom();
		} else if (url.contains(".sohu.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseSohu");
			}
			return new ParseSohu();
		} else if (url.contains(".sina.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseSina");
			}
			return new ParseSina();
		} else if (url.contains(".xinhuanet.")) {
			if(log.isDebugEnabled()){
				log.debug("���� "+url+" �Ľ�����ParseXinHuaNet");
			}
			return new ParseXinHuaNet();
		} else {
			return null;
		}

	}
}
