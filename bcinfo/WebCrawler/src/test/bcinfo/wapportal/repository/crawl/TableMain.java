/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

/**
 * @author dongq
 * 
 *         create time : 2009-11-11 ÏÂÎç03:48:56
 */
public class TableMain {

	public static void main(String[] args) throws Exception {
		//http://lottery.sports.sohu.com/open/index_df.shtml
		String url = "http://lottery.sports.sohu.com/open/index_df.shtml";
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
		
		List<TableRow> list = null;
		
		if(tableTag != null){
			list = new ArrayList<TableRow>();
			
			TableRow[] rows = tableTag.getRows();
			for(TableRow row : rows){
				String html = row.toHtml();
				if(html.contains("class=\"td_title02")){
					list.add(row);
					//System.out.println(row.toHtml());
				}
			}
		}
		
		if(list != null){
			int g = 0;
			TableRow pointer = null;
			int index = 0;
			for(TableRow row : list){
				int t = row.getColumnCount();
				if(g == 0) g = t;
				if(pointer == null) pointer = row;
				
				System.out.println(index+"*********************************** "+t+"|"+g);
				//System.out.println(row.toHtml());
				//System.out.println("");
				index++;
			}
		}
	}

}
