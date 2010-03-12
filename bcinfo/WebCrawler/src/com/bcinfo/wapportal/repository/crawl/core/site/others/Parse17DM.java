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
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**  
 * @author lvhs 	
 * E-mail:lvhs@flying-it.com  
 * @version 创建时间：2010-3-9 上午11:35:01  
 * 类说明  :抓取17DM网站动漫图片(http://news.17dm.com/mhmt/bagua/index.shtml)
 */
public class Parse17DM extends AbstractHtmlParseTemplete implements Parse{

	private static Logger log=Logger.getLogger(Parse17DM.class);
	
	public Parse17DM() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		String url_head=null;
		String url_end=null;
		try{
			url_head=link.substring(0,link.lastIndexOf("/")+1);
			url_end=link.substring(link.lastIndexOf("/")+1,link.lastIndexOf("."));
			links=new ArrayList<String>();
			links.add(link);
			String page=this.getPageContent(link,"div","id","artibody");
			if(!"".equals(page)){
				Parser parser=new Parser(link);
				parser.setEncoding("gb2312");
				
				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","box1_1"));
				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
						Div node=(Div)iter.nextNode();
						if(node!=null){
							if(node.getChildCount()>0){
								NodeList list_s=node.getChild(1).getChildren();
								if(list_s!=null && list_s.size()>0){
									for(NodeIterator it=list_s.elements();it.hasMoreNodes();){
										if(it.nextNode() instanceof Bullet){
											Bullet bt=(Bullet)it.nextNode();
											String st=bt.getLastChild().getText();
											if(st!=null){
												links.add(st);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			List<String> list=new ArrayList<String>();;
			if(links.size()>1){
				Iterator<String> it=new UniqueFilterIterator(links.iterator());
				List<String> ls=new ArrayList<String>();
				while(it.hasNext()){
					ls.add(it.next());
				}
				if(!ls.isEmpty()){
					links.clear();
					links.addAll(ls);
				}
			}
		
			String lastPage=links.get(links.size()-1);
			if(!lastPage.equals(link)){
				String[] str={};
				str=lastPage.split("_");
			
				if(str.length==2){
					lastPage=str[1];
				}
				str=lastPage.split("\\.");
				lastPage=str[0];
				//log.debug("----:"+lastPage);
				int length=0;
				if(lastPage!=null && !"".equals(lastPage.trim()) && !"".equals(null)){
					length=Integer.parseInt(lastPage);
				}
				List<String> _lis=new ArrayList<String>();
				String pageUrl=null;
				_lis.add(link);
				for(int i=2;i<=length;i++){
					pageUrl=url_head+url_end+"_"+i+".shtml";
					if(pageUrl!=null){
						_lis.add(pageUrl);
						links.clear();
						links.addAll(_lis);
					}
				}
			}else{
				links.clear();
				links.add(link);
			}
			
			if(log.isDebugEnabled()){
				for(String _links:links){
					log.debug("当前分页："+_links);
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
		try{
			Parser parser=new Parser(link);
			parser.setEncoding("gb2312");
			NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","artibody"));
			NodeList nodeList=parser.extractAllNodesThatMatch(filter);
			if(nodeList!=null && nodeList.size()>0){
				for(NodeIterator it_e=nodeList.elements();it_e.hasMoreNodes();){
					Div node=(Div)it_e.nextNode();
					content+=node.toHtml();
				}
			}
			content=this.commonParseContent(content);
			content=content.replaceAll("<br/><br/><br/>", "<br/>");
			content=content.replaceAll("<br/><br/>", "<br/>");
			
		}catch(Exception e){
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
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




	public static void main(String[] args){
		String link="http://news.17dm.com/201001/25629.shtml";
//		String link="http://news.17dm.com/201001/27766.shtml";
		Parse17DM pdm=new Parse17DM();
		System.out.println(pdm.parse(link));
		

	}

}
