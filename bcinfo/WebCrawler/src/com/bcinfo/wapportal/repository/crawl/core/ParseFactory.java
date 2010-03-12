/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.core.site.ParseDefault;
import com.bcinfo.wapportal.repository.crawl.core.site.huanqiu.ParseHuanQiu;
import com.bcinfo.wapportal.repository.crawl.core.site.others.Parse17DM;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseCdqss;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseCocPlay;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseComic_Yesky;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseCooCook;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseCosPlay;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseIFeng;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseIZhuti;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseMoxiu;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParsePcladyAstro;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParsePicDmguo;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseSkycn;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseTianya;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseTompad;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseYala;
import com.bcinfo.wapportal.repository.crawl.core.site.others.ParseZolMobile;
import com.bcinfo.wapportal.repository.crawl.core.site.others.Parse3g37;
import com.bcinfo.wapportal.repository.crawl.core.site.pcpop.ParsePcpopMobile;
import com.bcinfo.wapportal.repository.crawl.core.site.pcpop.ParsePcpopMovie;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQ;
import com.bcinfo.wapportal.repository.crawl.core.site.qq.ParseQQForum;
import com.bcinfo.wapportal.repository.crawl.core.site.sina.ParseSina;
import com.bcinfo.wapportal.repository.crawl.core.site.sohu.ParseSohu;
import com.bcinfo.wapportal.repository.crawl.core.site.tom.ParseTom;
import com.bcinfo.wapportal.repository.crawl.core.site.travel21cn.ParseTravel_21cn;
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
	
	public static Parse getParseInstance(String url) {
		log.info("简单工厂模式:"+url);
		if (url.contains(".qq.") && !url.contains("bbs.ent.qq")) {
			return new ParseQQ();
		} else if (url.contains("bbs.ent.qq")) {
			return new ParseQQForum();
		} else if (url.contains(".tom.")) {
			return new ParseTom();
		} else if (url.contains(".sohu.")) {
			return new ParseSohu();
		} else if (url.contains(".sina.")) {
			return new ParseSina();
		} else if (url.contains(".xinhuanet.")/*||url.contains("/xinhua/")*/) {
			return new ParseXinHuaNet();
		} else if(url.contains("www.news.cn")){
			return new ParseNewsCn();
		} else if(url.contains(".huanqiu.")){
			return new ParseHuanQiu();
		} else if(url.contains("www.pcpop.com")){
			return new ParsePcpopMobile();
		}else if(url.contains("movie.pcpop.")){
			return new ParsePcpopMovie();
		} else if(url.contains("astro.pclady.")){
			return new ParsePcladyAstro();
		} else if(url.contains(".coocook.")){
			return new ParseCooCook();
		} else if(url.contains(".51yala.")){
			return new ParseYala();
		} else if(url.contains("mobile.zol.")){
			return new ParseZolMobile();
		} else if(url.contains(".moxiu.")) {
			return new ParseMoxiu();
		} else if(url.contains(".izhuti.")) {
			return new ParseIZhuti();
		} else if(url.contains("sports.163.com")) {
			return new Parse163();
		} else if(url.contains("news.ifeng.com")) {
			return new ParseIFeng();
		} else if (url.contains(".tianya.")) {
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
		} else if(url.contains("www.3g37.com")){
			return new Parse3g37();
		} else if(url.contains("travel.21cn.com")){
			return new ParseTravel_21cn();
		}else if(url.contains("fb.cdqss.com")){
			return new ParseCdqss();
		}else if(url.contains("pic.dmguo.com")){
			return new ParsePicDmguo();
		}else if(url.contains("wangyou.pcgames.com.cn")){
			return new ParseCosPlay();
		}else if(url.contains("www.cocplay.com")){
			return new ParseCocPlay();
		}else if(url.contains("comic.yesky.com")){
			return new ParseComic_Yesky();
		}else if(url.contains("news.17dm.com")){
			return new Parse17DM();
		}
		
		
		
		
		
		
		
		
		
		else{
			return null;
		}

	}
}
