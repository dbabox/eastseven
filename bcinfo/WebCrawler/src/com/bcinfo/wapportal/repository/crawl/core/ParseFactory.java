/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.site.ParseDefault;
import com.bcinfo.wapportal.repository.crawl.core.site.huanqiu.ParseHuanQiu;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseCooCook;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseIFeng;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseIZhuti;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseMoxiu;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParsePcladyAstro;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseSkycn;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseTianya;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseTompad;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseYala;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseZolMobile;
import com.bcinfo.wapportal.repository.crawl.core.site.pcpop.ParsePcpopMobile;
import com.bcinfo.wapportal.repository.crawl.core.site.pcpop.ParsePcpopMovie;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQ;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQForum;
import com.bcinfo.wapportal.repository.crawl.core.site.sina.ParseSina;
import com.bcinfo.wapportal.repository.crawl.core.site.sohu.ParseSohu;
import com.bcinfo.wapportal.repository.crawl.core.site.tom.ParseTom;
import com.bcinfo.wapportal.repository.crawl.core.site.wangyi.Parse163;
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
	
	//private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Parse getParseInstance(String url) {
		
		if (url.contains(".qq.") && !url.contains("bbs.ent.qq")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseQQ");
			}
			return new ParseQQ();
		} else if (url.contains("bbs.ent.qq")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseQQForum");
			}
			return new ParseQQForum();
		} else if (url.contains(".tom.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseTom");
			}
			return new ParseTom();
		} else if (url.contains(".sohu.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseSohu");
			}
			return new ParseSohu();
		} else if (url.contains(".sina.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseSina");
			}
			return new ParseSina();
		} else if (url.contains(".xinhuanet.")/*||url.contains("/xinhua/")*/) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseXinHuaNet");
			}
			return new ParseXinHuaNet();
		} else if(url.contains("www.news.cn")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseNewsCn");
			}
			return new ParseNewsCn();
		} else if(url.contains(".huanqiu.")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseHuanQiu");
			}
			return new ParseHuanQiu();
		} else if(url.contains("www.pcpop.com")){
			////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParsePcpopMobile");
			return new ParsePcpopMobile();
		}else if(url.contains("movie.pcpop.")){
			////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParsePcpopMovie");
			return new ParsePcpopMovie();
		} else if(url.contains("astro.pclady.")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParsePcladyAstro");
			}
			return new ParsePcladyAstro();
		} else if(url.contains(".coocook.")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseCooCook");
			}
			return new ParseCooCook();
		} else if(url.contains(".51yala.")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseYala");
			}
			return new ParseYala();
		} else if(url.contains("mobile.zol.")){
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseZolMobile");
			}
			return new ParseZolMobile();
		} else if(url.contains(".moxiu.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseMoxiu");
			}
			return new ParseMoxiu();
		} else if(url.contains(".izhuti.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseIZhuti");
			}
			return new ParseIZhuti();
		} else if(url.contains("sports.163.com")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类Parse163");
			}
			return new Parse163();
		} else if(url.contains("news.ifeng.com")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseIFeng");
			}
			return new ParseIFeng();
		} else if (url.contains(".tianya.")) {
			if(log.isDebugEnabled()){
				////System.out.println(sdf.format(new Date())+" : 创建 "+url+" 的解析类ParseTianya");
			}
			return new ParseTianya();
		} else if (url.contains("soft.tompda.com")) {
			return new ParseTompad();
		} else if (url.contains("sj.skycn.com")) {
			return new ParseSkycn();
		} else if(url.contains("www.china.com.cn")) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(ParseDefault.TAG_NAME, "div");
			map.put(ParseDefault.ATTRIBUTE_NAME, "id");
			map.put(ParseDefault.ATTRIBUTE_VALUE, "artibody");
			return new ParseDefault(map);
		} else {
			return null;
		}

	}
}
