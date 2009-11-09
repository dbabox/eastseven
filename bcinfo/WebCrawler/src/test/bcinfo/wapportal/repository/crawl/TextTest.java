/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.util.DebugUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-27 ÏÂÎç04:34:14
 */
public class TextTest {

	public static void main(String[] args) throws Exception{
		String url = "http://www.coocook.com/index.php/action_viewnews_itemid_57071.html";
		Parser parser = new Parser(url);
//		NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
//		DebugUtil.printNodeList(nodeList);
		NodeList nodeList = DebugUtil.getNodeList(url, "div");
		DebugUtil.printNodeList(nodeList);
		NodeIterator iter = nodeList.elements();
		while(iter.hasMoreNodes()){
			
		}
	}

}
