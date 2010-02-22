/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**
 * @author dongq
 * 
 *         create time : 2010-2-22 ÏÂÎç04:31:20
 */
public class Parse3G37 extends AbstractHtmlParseTemplete implements Parse {

	private static final Logger log = Logger.getLogger(Parse3G37.class);
	
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try{
			links = new ArrayList<String>();
			links.add(link);
			log.debug("sdafdafdfhladfasdj");
		}catch(Exception e){
			links = new ArrayList<String>();
			links.add(link);
			log.info("È¡TOMPDA["+link+"]·ÖÒ³Ê§°Ü");
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(String link) {
		return this.simpleParse(link);
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

}
