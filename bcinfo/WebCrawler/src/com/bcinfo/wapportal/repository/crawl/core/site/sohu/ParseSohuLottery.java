/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.site.sohu;

import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;

/**
 * @author dongq
 * 
 *         create time : 2009-11-11 ÉÏÎç10:38:27<br>
 *         ²ÊÆ±<br>
 *         http://lottery.sports.sohu.com/open<br>
 */
public class ParseSohuLottery extends AbstractHtmlParseTemplete{

	private static Logger log = Logger.getLogger(ParseSohuLottery.class);
	
	
	public static void main(String[] args) {

	}


	@Override
	public List<String> checkPageOfLinks(String link) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTargetContent(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String simpleParse(String link) {
		String content = null;
		try{
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}
		return content;
	}
}
