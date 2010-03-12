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
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;

/**  
 * @author lvhs 	
 * E-mail:lvhs@flying-it.com  
 * @version 创建时间：2010-3-4 下午04:52:09  
 * 类说明  :抓取动漫国网站各网点图片(http://pic.dmguo.com/dongmanyuanhua/
								 ,http://pic.dmguo.com/jingmeibizhi/
								 ,http://pic.dmguo.com/dongmanzatu/
								 ,http://pic.dmguo.com/COSPLAY/
								 )
 */
public class ParsePicDmguo extends AbstractHtmlParseTemplete implements Parse{

	private static Logger log=Logger.getLogger(ParsePicDmguo.class);
	
	public ParsePicDmguo() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links=null;
		try{
			links=new ArrayList<String>();
			links.add(link);
			
			
//-------------------------------------------------------------------------------------------------------			
//废除			
//			String page=this.getPageContent(link, "div", "class", "infopic");
//			if(!"".equals(page)){
//				Parser parser=new Parser(link);
//				parser.setEncoding("gb2312");
//				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","viewpage"));
//				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
//				if(nodeList!=null && nodeList.size()>0){
//					//log.debug("nodeList----------"+nodeList);
//					NodeList childrenList = nodeList.elementAt(0).getChildren();
//					//log.debug("childrenList********"+childrenList);
//					Span span=null;
//					for(NodeIterator iter=childrenList.elements();iter.hasMoreNodes();){
//						Node node=iter.nextNode();
//						//log.debug("node--------"+node);
//						if(node instanceof Span){
//							span=(Span)node;
//						}
//					}
//					if(span!=null){
//						//log.debug("======="+span.toHtml().contains("下一页"));
//						boolean have_next=false;
//						have_next=span.toHtml().contains("下一页");
//						while(!have_next && !span.getAttribute("class").equals("disabled")){
//							String temp=span.getAttribute("class");
//							if(!temp.equals("disabled")){
////								log.debug("...");
//							}
//						}
//						
//					}
//					
//				}
//			}
			
//---------------------------------------------------------------------------------------------------		
			
			
			
			
			String page=this.getPageContent(link, "div","class","infopic");
			if(!"".equals(page)){
				Parser parser=new Parser(link);
				parser.setEncoding("gb2312");
				NodeFilter filter=new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","viewpage"));
				NodeList nodeList=parser.extractAllNodesThatMatch(filter);
				if(nodeList!=null && nodeList.size()>0){
					for(NodeIterator iter=nodeList.elements();iter.hasMoreNodes();){
						Div node=(Div)iter.nextNode();
						if(node!=null){
							filter=new NodeClassFilter(LinkTag.class);
							if(node.getChildCount()>0){
								NodeList node_list=node.getChildren().extractAllNodesThatMatch(filter);
								if(node_list!=null && node_list.size()>0){
									for(NodeIterator it=node_list.elements();it.hasMoreNodes();){
										LinkTag link_tag=(LinkTag)it.nextNode();
										String link_s=link_tag.extractLink();
										if(link_s!=null){
											links.add(link_s);
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(links.size()>1){
					Iterator<String> iterator=new UniqueFilterIterator(links.iterator());
					List<String> ls=new ArrayList<String>();
					while(iterator.hasNext()){
						ls.add(iterator.next());
					}
					if(!ls.isEmpty()){
						links.clear();
						links.addAll(ls);
					}
			}
			
			String temp=links.get(links.size()-1);
			if(!temp.equals(link)){
				String[] str={};
				str=temp.split("_");
				String url_head=str[0];
			
				if(str.length==2){
					temp=str[1];
					str=temp.split("\\.");
					if(str.length==2){
						temp=str[0];
					}	
				}
				int length=0;
				if(temp!=null && !"".equals(temp.trim()) && !"".equals(null)){
					length=Integer.parseInt(temp);
				}
				String http_url=null;
				String first_page=null;
				List<String> list=new ArrayList<String>();
				if(url_head!=null){
					String[] sss={};
					sss=url_head.split("index");
					first_page=sss[0];
				}
				list.add(first_page);
				for(int i=2;i<=length;i++){
					http_url=url_head+"_"+i+".shtml";
					if(http_url!=null){
						list.add(http_url);
						links.clear();
						links.addAll(list);
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
			NodeList imgList=parser.extractAllNodesThatMatch(new TagNameFilter("Div"));
			if(imgList!=null && imgList.size()>0){
				for(NodeIterator iters=imgList.elements();iters.hasMoreNodes();){
					Div node=(Div)iters.nextNode();
					
					if("infopic".equals(node.getAttribute("class"))){
						Node new_node=node.getChild(0);
						
							if(new_node instanceof ImageTag){
								ImageTag img=(ImageTag)new_node;
								content+=img.getImageURL()+",	";
							}
						
					}
				}
			}
			
			
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
//		String link="http://pic.dmguo.com/html/2009/1029/1087/";	//不超过10页
		String link="http://pic.dmguo.com/html/2010/0128/3956/";	//超过10页
		ParsePicDmguo pp=new ParsePicDmguo();
		System.out.println(pp.parse(link));

	}

}
