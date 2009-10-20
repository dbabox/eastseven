/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ÏÂÎç07:45:15
 */
public class Test {

	public static void main(String[] args) throws Exception {
		String url = "http://sc.sina.com.cn/news/gngjnews/list.html";
		Parser parser = new Parser(url);
		NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class));
		NodeIterator iter = nodeList.elements();
		String data = "";
		while(iter.hasMoreNodes()){
			ScriptTag node = (ScriptTag)iter.nextNode();
//			System.out.println("**************************************************");
//			System.out.println(node.getScriptCode());
//			System.out.println("---------------------------------------------------");
			if(node.getScriptCode().contains("var data=[")) data = node.getScriptCode().trim();
		}
		
		System.out.println(data);
		System.out.println("---------------------------------------------------");
		data = data.substring(data.indexOf("[")+1, data.indexOf("]")).replaceAll("\\},", "");
		System.out.println(data);
		System.out.println("**************************************************");
		
		String[] datas = data.split("\\{");
		for(int i=0;i<datas.length;i++){
			String _data = datas[i].replaceAll("", "").trim();
			System.out.println(i+" | "+_data);
			System.out.println("");
			String[] tmp = _data.split(",");
			for(int j=0;j<tmp.length;j++){
				String val = tmp[j].replaceAll("\"", "").trim();
				if(val.contains("title")){
					String title = val.replaceAll("title:", "");
				}else if(val.contains("url")){
					String link = val.replaceAll("url:", "");
					if(link.startsWith("http:")){
						
					}
				}
			}
		}
	}

}
