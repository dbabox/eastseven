package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.List;

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
 * 
 * 3g37�ֻ���������
 */

public class Parse3g37 extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(Parse3g37.class);
	
	
	public Parse3g37(String url) {
		
	}
	
	public Parse3g37() {
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

	@Override
	public List<String> checkPageOfLinks(String link) {
		// TODO Auto-generated method stub
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);
		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}	
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		// TODO Auto-generated method stub
		String content="";
		String imgPath="";
		String mobileType="";
		String downPath="";
		
		try{
			//���ͼ���ַ
			Parser parser=new Parser(link);
			parser.setEncoding("gb2312");
			NodeList imgList=parser.extractAllNodesThatMatch(new NodeClassFilter(Div.class));
			if(imgList!=null && imgList.size()>0){
				for(NodeIterator item=imgList.elements();item.hasMoreNodes();){
					Div div = (Div)item.nextNode();
					if("left1".equals(div.getAttribute("id"))){
//						log.debug("�Ƿ���ͼ���ַ�ڵ�:"+("left1".equals(div.getAttribute("id"))));
						Node node = div.getChild(0);
						if(node instanceof LinkTag){
							Node _node = node.getFirstChild();
							if(_node instanceof ImageTag){
								ImageTag imageTag = (ImageTag)_node;
								imgPath += imageTag.getImageURL() + ",";
//								log.debug("ͼ���ַ:"+imgPath);
							}
						}
					}
					
					//ŵ����S60V5
					if("left1-s60v5".equals(div.getAttribute("id"))){
						Node node = div.getChild(0);
						if(node instanceof LinkTag){
							Node _node = node.getFirstChild();
							if(_node instanceof ImageTag){
								ImageTag imageTag = (ImageTag)_node;
								imgPath += imageTag.getImageURL() + ",";
//								log.debug("ͼ���ַ:"+imgPath);
							}
						}
					}
					
					
				}
				
			}
			parser.reset();
			
			//����ֻ�֧�ֻ���
//			log.debug("����ֻ�֧�ֻ���");
			NodeList typeList = parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "cen1")));
			for(NodeIterator iter = typeList.elements();iter.hasMoreNodes();){
				Node node = iter.nextNode();
				mobileType = node.toHtml();
				
			}
			mobileType = this.commonParseContent(mobileType);
			
			String[] str={};
			str=mobileType.split("<br/>");
			//ŵ����
			if(str.length==3){
			mobileType=str[2];
			}
			//ŵ����S60V5
			if(str.length==4){
				mobileType=str[3];
				}
			
			str=mobileType.split(" + ");
			mobileType=str[0];
			//20100225 creat
			//�����������
			str=mobileType.split("�ղ���ʾ");
			mobileType=str[0];
			
			
			parser.reset();
			
//			//������ص�ַ
//			log.debug("������ص�ַ");
//			NodeFilter filter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "cen4"));
//			NodeList downList = parser.extractAllNodesThatMatch(filter);
//			log.debug("downList======"+downList.size());
//			if(downList!=null && downList.size()>0){
//				for(NodeIterator iter = downList.elements();iter.hasMoreNodes();){
//					log.debug("test");
//					Div div = (Div)iter.nextNode();
//					Node node = div.getChild(1);
//					log.debug("node:===="+node);
//					if(node instanceof LinkTag){
//						Node _node = node.getFirstChild();
//						log.debug("=====_node===="+_node);
//						if(_node instanceof ImageTag){
//							ImageTag imageTag = (ImageTag)_node;
//							downPath += imageTag.getImageURL() + ",";
//						}
//						log.debug("downPath===="+downPath);
//						
//					}
//				}
//			}
			
			//������ص�ַ
//			log.debug("������ص�ַ");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList!=null && nodeList.size()>0){
				NodeIterator iter = nodeList.elements();
				while(iter.hasMoreNodes()){
					LinkTag linkTag = (LinkTag)iter.nextNode();
					String hrefTag = linkTag.getAttribute("href");
					//if(hrefTag!=null&&hrefTag.contains("http://down.3g37.com/Nokia/Up0818/200911/2009111208273847.sis")){
					if(hrefTag!=null&&hrefTag.contains("http://down.3g37.com")){	
						downPath = linkTag.extractLink();
						break;
					}
				}
			}
			
			
			
			if(!"".equals(imgPath)){
				content+=imgPath+"|";
			}
			if(!"".equals(downPath)){
				content += downPath+"|";
			}
			if(!"".equals(mobileType)){
				content += this.commonParseContent(mobileType);
			}
			
		}catch(Exception e){
			if(log.isDebugEnabled()){
			e.printStackTrace();
			}
		}
		
		
		return content;
	}

	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String link="http://www.3g37.com/Nokia/Nokia03/200911/79210.html"; //ŵ����
//		String link="http://www.3g37.com/Motoskin/moto4/200806/8340.html"; //Ħ������   ---������ʾ����ȷ
//		String link="http://www.3g37.com/zhuti/S60V5/87382.html";			//ŵ����S60V5
//		String link="http://www.3g37.com/SEskin/SE13/201001/86093.html";	//���� 
//		String link="http://www.3g37.com/zhuti/sj/86480.html";				//����
		Parse3g37 p=new Parse3g37(link);
		System.out.println(p.parse(link));
	}

}
