/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.util.DebugUtil;


/**
 * @author dongq
 * 
 *         create time : 2009-10-19 ÏÂÎç02:45:11
 */
public class TestMain {

	public static void main(String[] args) throws Exception{
		String url = "http://lottery.sports.sohu.com/open/sceexw.shtml";
		Parser parser = new Parser(url);
		System.out.println(parser.parse(null).toHtml());
		System.out.println(" *********************************************************************************************** ");
		parser.reset();
		NodeList nodeList = DebugUtil.getNodeList(url, "iframe");
		
		DebugUtil.printNodeList(nodeList);
//		NodeIterator iter = nodeList.elements();
//		while(iter.hasMoreNodes()){
//			TableTag tableTag = (TableTag)iter.nextNode();
//			DebugUtil.printNodeList(tableTag.getChildren());
//		}
		/*
		NodeIterator iter = nodeList.elements();
		Div cntDiv = null;
		while(iter.hasMoreNodes()){
			Div div = (Div)iter.nextNode();
			if("Content".equals(div.getAttribute("id"))) cntDiv = div;
		}
		ParagraphTag pageLink = null;
		if(cntDiv!=null){
			System.out.println(cntDiv.toHtml());
			nodeList = cntDiv.getChildren();
			DebugUtil.printNodeList(nodeList);
			iter = nodeList.elements();
			
			while(iter.hasMoreNodes()){
				Node node = iter.nextNode();
				if(node instanceof ParagraphTag){
					ParagraphTag p = (ParagraphTag)node;
					if("pagelink".equals(p.getAttribute("class"))){
						pageLink = p;
					}
				}
			}
		}
		if(pageLink!=null){
			System.out.println(pageLink.toHtml());
			nodeList = pageLink.getChildren();
			DebugUtil.printNodeList(nodeList);
			iter = nodeList.elements();
			while(iter.hasMoreNodes()){
				Node node = iter.nextNode();
				if(node instanceof LinkTag){
					LinkTag linkTag = (LinkTag)node;
					if(!linkTag.getLinkText().contains("1")){
						
					}
				}
			}
		}
		*/
	}

}
