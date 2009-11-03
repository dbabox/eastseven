/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.site.huanqiu.ParseHuanQiu;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQ;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQForum;
import com.bcinfo.wapportal.repository.crawl.core.site.sina.ParseSina;
import com.bcinfo.wapportal.repository.crawl.core.site.sohu.ParseSohu;
import com.bcinfo.wapportal.repository.crawl.core.site.tom.ParseTom;
import com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet.ParseNewsCn;
import com.bcinfo.wapportal.repository.crawl.core.site.xinhuanet.ParseXinHuaNet;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 下午01:18:16<br>
 *         解析工厂类,提供可解析的具体实现类,若没有则会返回null
 */
public final class ParseFactory {

	private static Logger log = Logger.getLogger(ParseFactory.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Parse getParseInstance(String url) {
		
		if (url.contains(".qq.") && !url.contains("http://bbs.")) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseQQ");
			}
			return new ParseQQ();
		} else if (url.contains(".qq.") && url.contains("http://bbs.")) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseQQForum");
			}
			return new ParseQQForum();
		} else if (url.contains(".tom.")) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseTom");
			}
			return new ParseTom();
		} else if (url.contains(".sohu.")) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseSohu");
			}
			return new ParseSohu();
		} else if (url.contains(".sina.")) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseSina");
			}
			return new ParseSina();
		} else if (url.contains(".xinhuanet.")/*||url.contains("/xinhua/")*/) {
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseXinHuaNet");
			}
			return new ParseXinHuaNet();
		} else if(url.contains("www.news.cn")){
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseNewsCn");
			}
			return new ParseNewsCn();
		} else if(url.contains(".huanqiu.")){
			if(log.isDebugEnabled()){
				System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseHuanQiu");
			}
			return new ParseHuanQiu();
		} else {
			return null;
		}

	}
}
