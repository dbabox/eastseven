/**
 * 
 */
package org.dongq.demo;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.SpringVersion;

/**
 * @author dongq
 * 
 */
public final class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Hello Maven");
		URL url = new URL("http://www.china-pub.com/196660");
		int timeoutMillis = 10 * 1000;
		Document doc = Jsoup.parse(url, timeoutMillis);
		Elements moreInfos = doc.select("div[class=more-infos]");
		
		String html = moreInfos.html();
		System.out.println(SpringVersion.getVersion());
		
		
	}

}
