package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**  
 * @author lvhs 	
 * E-mail:lvhs@flying-it.com  
 * @version 创建时间：2010-3-3 下午02:50:07  
 * 类说明  :抓取http://fb.cdqss.com/chwl/list.php?chwl=chwl&catPath=0,1,5页面
 */
public class ParseCdqss extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log=Logger.getLogger(ParseCdqss.class);
		
	public ParseCdqss() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		try{
			links=new ArrayList<String>();
			links.add(link);
			String page=this.getPageContent(link,"div","id","cont");
			if(!"".equals(page)){
				Parser parser=new Parser(link);
				parser.setEncoding("UTF-8");
				
				
				NodeList nodeList=parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div") , new HasAttributeFilter("class","pagination")));
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
						Div node=(Div)iter.nextNode();
						if(node!=null){
							NodeFilter filter=new NodeClassFilter(LinkTag.class);
							NodeList linkTags = node.getChildren().extractAllNodesThatMatch(filter);
							for(NodeIterator iters=linkTags.elements();iters.hasMoreNodes();){
								LinkTag l_tag=(LinkTag)iters.nextNode();
								
								String link_s=l_tag.extractLink();
								if(link_s!=null && !"".trim().equals(link_s)){
								links.add(link_s);
								}
							}
						}
					}
				}
			}
			
			//排除重复的link
			if(links.size()>1){
				Iterator<String> iters=new UniqueFilterIterator(links.iterator());
				List<String> lis=new ArrayList<String>();
				while(iters.hasNext()){
					lis.add(iters.next());
				}
				if(!lis.isEmpty()){
					links.clear();
					links.addAll(lis);
				}
			}
			
			if(log.isDebugEnabled()){
				for(String _link : links){
					log.debug("分页地址:"+_link);
				}
			}
			
		}catch(Exception e){
			links=new ArrayList<String>();
			links.add(link);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
			
		}
		
		return links; 
	}

	@Override
	public String getTargetContent(String link) {
		String content="";
		try {
			//获得网页图片地址和网页文本内容
			Parser parser=new Parser(link);
			parser.setEncoding("UTF-8");
			NodeList nodeList=parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div") , new HasAttributeFilter("id","cont")));
			if(nodeList!=null && nodeList.size()>0){
				Div div = (Div)nodeList.elementAt(0);
				content = div.toHtml();
			}
			if(!"".equals(content)){
				content = this.commonParseContent(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return content;
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(String link) {
		// TODO Auto-generated method stub
		return this.simpleParse(link);
	}

	public static void main(String[] args) {
		String link="http://fb.cdqss.com/html/news/5/2010-03/20100302-093349_1.htm";
		ParseCdqss pc=new ParseCdqss();
		System.out.println(pc.parse(link));
	}

}
