/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-9-10 ����04:26:20<br>
 *         ץȡ������<br>
 */
public final class CrawlerUtil {

	private static Logger log = Logger.getLogger(CrawlerUtil.class);
	
	public static Boolean canCrwal(String link){
		if(link.indexOf("http://blog.")!=-1 
				|| link.indexOf("http://video.")!=-1 
				|| link.indexOf("http://forum.")!=-1 
				|| link.indexOf("/photo/")!=-1 
				|| link.indexOf("http://you.video.")!=-1
				|| link.indexOf("http://pic.") != -1
				//|| link.indexOf("http://astro.") != -1 
				|| link.indexOf("http://test.") != -1 
				|| link.indexOf("/gallery_") != -1 
				|| link.indexOf("http://alive.") != -1
				|| link.indexOf("javascript") != -1
				|| link.indexOf("/bbs/") != -1
				|| link.indexOf("data[i].url") != -1){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * �����ķ���
	 * @param url
	 * @return
	 */
	@Deprecated
	public static List<LinkTag> getAllLinkTag(String url){
		List<LinkTag> linkTags = null;
		try{
			Parser parser = new Parser(url);
			
			parser.setEncoding("GBK");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			NodeIterator iter = nodeList.elements();
			linkTags = new ArrayList<LinkTag>();
			while(iter.hasMoreNodes()){
				LinkTag linkTag = (LinkTag)iter.nextNode();
				linkTags.add(linkTag);
			}
		}catch(Exception e){
			System.out.println("ȡ��ҳ��["+url+"]�ڵ����г����ӵ�ַʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return linkTags;
	}
	
	public static boolean isLegal(String link){
		boolean bln = false;
		
		return bln;
	}
	
	public static List<FolderBO> getAllLinks(String url){
		List<FolderBO> links = null;
		try{
			Parser parser = new Parser(url);
			
			if(log.isDebugEnabled()) System.out.println(url+" | "+parser.getEncoding());
			
			System.out.println(" ���� "+parser.getEncoding());
			
			if(!"GBK".equalsIgnoreCase(parser.getEncoding())) parser.setEncoding("GBK");
				
			//TODO ���������
			//������̬ҳ�����ݣ��������Ա����������ݴ����js�ű��е����
			//String inputHTML = parser.parse(null).toHtml();
			//parser.setInputHTML(inputHTML);
			
			NodeList nodeList = new NodeList();
			try{
				nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			}catch(EncodingChangeException e){
				parser = Parser.createParser(ReadHttpFile.getPageContent(url), "GBK");
				nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			}
			
			String httpHeader = CrawlerUtil.extractLinkHeader(url);
			
			NodeIterator iter = nodeList.elements();
			links = new ArrayList<FolderBO>();
			while(iter.hasMoreNodes()){
				LinkTag linkTag = (LinkTag)iter.nextNode();
				String link = linkTag.extractLink();
				String title = linkTag.getLinkText();
				
				//System.out.println("  "+link);
				
				if(link == null || "".equals(link)) continue;
				if(!CrawlerUtil.canCrwal(link)) continue;
				if(link.equals(url)) continue;
				if(title.contains("��ϸ")||title.contains("����")) continue;
				
				if(!link.startsWith("http://")){
					link = CrawlerUtil.addLinkHeader(link, httpHeader);
				}
				
				if(log.isDebugEnabled()) System.out.println("     ��ץȡ�����ӣ�"+link);
				FolderBO folder = new FolderBO();
				folder.setLink(link);
				folder.setTitle(title);
				links.add(folder);
			}
		}catch(Exception e){
			System.out.println("ȡ��ҳ��["+url+"]�ڵ����г����ӵ�ַʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return links;
	}
	
	public static List<FolderBO> getAllLinksForSC(String url){
		List<FolderBO> links = null;
		try{
			Parser parser = new Parser(url);
			if(log.isDebugEnabled()){
				log.debug(url+" | "+parser.getEncoding());
				System.out.println(parser.parse(null).toHtml());
				//parser.reset();
			}
			if(!"GBK".equalsIgnoreCase(parser.getEncoding()))
				parser.setEncoding("GBK");
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class));
			NodeIterator iter = nodeList.elements();
			String data = "";
			while(iter.hasMoreNodes()){
				ScriptTag node = (ScriptTag)iter.nextNode();
				
				if(log.isDebugEnabled()){
					System.out.println("script   :"+node.getScriptCode());
				}
				
				if(node.getScriptCode().contains("var data=[")){
					data = node.getScriptCode().trim();
					break;
				}
			}
			String httpHeader = extractLinkHeader(url);
			if(!"".equals(data)){
				
				data = data.substring(data.indexOf("[")+1, data.indexOf("]")).replaceAll("\\},", "");
				links = new ArrayList<FolderBO>();
				String[] datas = data.split("\\{");
				for(int i=0;i<datas.length;i++){
					String _data = datas[i].replaceAll("", "").trim();
					String[] tmp = _data.split(",");
					FolderBO folder = new FolderBO();
					for(int j=0;j<tmp.length;j++){
						String val = tmp[j].replaceAll("\"", "").trim();
						
						if(val.contains("title")){
							String title = val.replaceAll("title:", "");
							folder.setTitle(title);
						}else if(val.contains("url")){
							String link = val.replaceAll("url:", "");
							if(!link.startsWith("http:")){
								link = addLinkHeader(link, httpHeader);
							}
							folder.setLink(link);
						}
					}
					links.add(folder);
				}
			}else{
				System.out.println("δȡ��ҳ��["+url+"]�ű�����");
			}
		}catch(Exception e){
			System.out.println("ȡ�������Ĵ�ҳ��["+url+"]�ڵ����г����ӵ�ַʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return links;
	}
	
	/**
	 * ȡ�����ӵĶ�����ַ
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public static String extractLinkHeader(String link) {
		try{
			String http = "";
			if(link.indexOf("http://")!=-1){
				String tmp = link.substring("http://".length());
				http = "http://" + tmp.substring(0, tmp.indexOf("/"));
			}
			return http;
		}catch(Exception e){
			return link;
		}
	}
	
	/**
	 * ��û��http://�������������·��
	 * @param link
	 * @param httpHeader
	 * @return
	 * @throws Exception
	 */
	public static String addLinkHeader(String link, String httpHeader) throws Exception{
		String completeLink = "";
		if(link.indexOf("http://")== -1){
			completeLink = httpHeader + "/" + link;
		}else
			completeLink = link;
		return completeLink;
	}
	
	/**
	 * �޳������е�����<br/>��ǩ�����׳��쳣���򷵻�ԭ�����ַ���
	 * @param pageContent
	 * @return
	 */
	public static String formatContent(String pageContent){
		String content = "";
		try{
			String[] contents = pageContent.split("<br/>");
			if(contents != null && contents.length > 0){
				for(int i=0;i<contents.length;i++){
					String _content = contents[i].trim();
					if(_content.length() > 0) content += "<br/>"+_content;
				}
			}else
				content = pageContent;
		}catch(Exception e){
			//e.printStackTrace();
			content = pageContent;
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return content;
	}
	
	/**
	 * ��ʽ�������ַ�
	 * @param pageContent
	 * @return
	 */
	public static String formatSpecialWords(String pageContent){
		String content = "";
		try{
			String replacement = "";
			content = pageContent.replaceAll("&lt;", replacement);
			content = content.replaceAll("&gt;",replacement);
			content = content.replaceAll("&apos;", "'");
			content = content.replaceAll("&quot;", "\"");
			content = content.replaceAll("&ldquo;", "\"");
			content = content.replaceAll("&rdquo;", "\"");
			//
			content = content.replaceAll("&nbsp;", " ");//&nbsp;
			content = content.replaceAll("&shy;", "-");
			content = content.replaceAll("&#8230;", "��");
		}catch(Exception e){
			content = pageContent;
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return content;
	}
	
	/**
	 * ����ȡ����Դ���浽�ļ���
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static boolean writeFile(String fileName, String content){
		boolean bln = false;
		FileOutputStream out = null;
		try{
			File file = new File("C:/Download/"+fileName+".txt");
			//if(file.exists()) file.delete();
			out = new FileOutputStream(file,true);
			out.write(content.getBytes());
			out.close();
		}catch(Exception e){
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return bln;
	}
	
	/**
	 * ����ȡ���ĵ�ַ��¼����־�ļ���
	 * @param logFilePath
	 * @param folderId
	 * @param link
	 */
	public static void writeLog(String logFilePath, String folderId, String link){
		try{
			String item = "\n<item>";
			item += "\n<link>"+link+"</link>";
			item += "\n<parentfolder>"+folderId+"</parentfolder>";
			item += "\n</item>";
			writeFileAppand(logFilePath, item);
		}catch(Exception e){
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
	}
	
	/**
	 * ��xml�ļ����׷������
	 * 
	 */
	public static synchronized void writeFileAppand(String fileName, String content) {
		try {
			// ��һ����������ļ���������д��ʽ
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// �ļ����ȣ��ֽ���
			long fileLength = randomFile.length();
			// ��д�ļ�ָ���Ƶ��ļ�β��
			if(fileLength>8) randomFile.seek(fileLength - 8);
			randomFile.writeBytes(content + "\n</page>");

			randomFile.close();
		} catch (IOException e) {
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
	}
	
}
