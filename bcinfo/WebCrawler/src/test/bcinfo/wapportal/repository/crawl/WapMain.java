/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.util.DebugUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-11-17 ÉÏÎç10:18:05<br>
 */
public class WapMain {

	public static void main(String[] args) throws Exception {
		String url = "http://theme.3g.cn/sort_list.aspx?ftp=18&sid=&wid=0&waped=";
		Parser parser = new Parser(url);
		String html = parser.parse(null).toHtml();
		System.out.println(html);
		System.out.println("");
		System.out.println(" -------------------------------------------------- ");
		parser.reset();
		NodeList nodeList = parser.parse(null);
		DebugUtil.printNodeList(nodeList);
	}

}
