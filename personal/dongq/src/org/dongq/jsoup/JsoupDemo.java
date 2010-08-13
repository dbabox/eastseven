/**
 * 
 */
package org.dongq.jsoup;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author dongq
 * 
 *         create time : 2010-8-9 上午09:31:29
 */
public final class JsoupDemo {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://down.tech.sina.com.cn/list/16_3_1.html");
		int timeoutMillis = 3 * 1000;
		Document doc = Jsoup.parse(url, timeoutMillis);
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			System.out.println(link);
		}

	}

}
