/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;

/**
 * @author dongq
 * 
 *         create time : 2009-11-11 ÉÏÎç10:41:07<br>
 */
public class LotteryMain extends AbstractHtmlParseTemplete {

	public static void main(String[] args) throws Exception {
		//http://lottery.sports.sohu.com/open/index_df.shtml
		//
		String url = "http://lottery.sports.sohu.com/open/index_df.shtml";
		//String cnt = new LotteryMain().getTargetContent(url);
		//System.out.println(cnt);
		//System.out.println(new Parser(url).parse(null).toHtml());
		Parser parser = new Parser(url);
		NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(TableTag.class));
		TableTag tableTag = null;
		if(nodeList != null){
			NodeIterator iter = nodeList.elements();
			while(iter.hasMoreNodes()){
				TableTag table = (TableTag)iter.nextNode();
				if("kj_tablelist01".equals(table.getAttribute("class"))){
					tableTag = table;
				}
			}
		}
		
		if(tableTag != null){
			TableRow[] rows = tableTag.getRows();
			Map<String, Integer> map = new HashMap<String, Integer>();
			for(TableRow row : rows){
				
				System.out.println("*****"+row.getColumnCount()+"************************************************");
				String html = row.toHtml();
				if(html.contains("class=\"td_title02") && row.getColumnCount() == 8){
					System.out.println(row.getColumns()[0]);
				}
				//System.out.println("----------------------------------------------------");
				//System.out.println(row.toHtml());
				System.out.println("");
			}
		}
	}

	@Override
	public List<String> checkPageOfLinks(String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetContent(String link) {
		String content = null;
		try{
			String tagName = "table";
			String attrName = "class";
			String attrValue = "kj_tablelist01";
			content = this.getPageContent(link, tagName, attrName, attrValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}

}
