/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-9-10 下午04:26:20<br>
 *         抓取工具类<br>
 */
public final class CrawlerUtil {

	private static Logger log = Logger.getLogger(CrawlerUtil.class);
	
	public static String getFileDownloadAddress(String url){
		String link = "";
		
		try {
			HttpClient client = new HttpClient();
			GetMethod method = new GetMethod(url);
			method.setFollowRedirects(true);
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.info("Method failed: " + method.getStatusLine());
			}
			link = method.getURI().toString();
		} catch (Exception e) {
			String msg = e.getMessage();
			//TODO 针对下载文件名含空格的处理
			if(msg.contains("Invalid redirect location:")&&msg.contains("http:")){
				//Invalid redirect location: http://down.waptw.com//upload/file/090410/soft/PhatNotes5.4Build5.621.0321 for WM5.cab
				return msg.substring(msg.indexOf("http:"));
			} else {
				if(log.isDebugEnabled()) e.printStackTrace();
				log.info("取得"+url+"文件下载地址报错");
			}
		}
		
		return link;
	}
	
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
	 * 废弃的方法
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
			//System.out.println("取得页面["+url+"]内的所有超链接地址失败");
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
	
	/**
	 * 取入口地址页面内的所有链接<br>
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FolderBO> getAllLinks(String url){
		List<FolderBO> links = null;
		try{
			Parser parser = new Parser(url);
			
			//取页面编码，无则默认为GBK
			String charset = CrawlerUtil.getCharset(url);
			parser.setEncoding(charset);
			//计算链接标题的平均长度
			int avgLength = 3;
			int words = 0;
			int linkNum = 1;
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodeList!=null&&nodeList.size()>0){
				Node[] nodes = nodeList.toNodeArray();
				for(Node node : nodes){
					LinkTag linkTag = (LinkTag)node;
					words += linkTag.getLinkText().length();
					linkNum++;
				}
				if(words>0){
					avgLength = words/linkNum;
				}
				links = new ArrayList<FolderBO>();
				List<LinkTag> list = new ArrayList<LinkTag>();
				for(Node node : nodes){
					LinkTag linkTag = (LinkTag)node;
					int len = linkTag.getLinkText().length();
					if(len >= avgLength){
						list.add(linkTag);
					}
				}
				Iterator<LinkTag> iter = new UniqueFilterIterator(list.iterator());
				while(iter.hasNext()){
					LinkTag linkTag = iter.next();
					String link = linkTag.extractLink();
					String title = linkTag.getLinkText();
					if(log.isDebugEnabled()){
						System.out.println(" 待抓取的链接["+link+"]["+title+"]");
					}
					FolderBO folder = new FolderBO();
					folder.setLink(link);
					folder.setTitle(title);
					links.add(folder);
				}
			}
			
			//取大于平均长度的链接
			//if("ISO-8859-1".equalsIgnoreCase(parser.getEncoding())) parser.setEncoding("GBK");
				
			//TODO 该问题待查
			//解析静态页面内容，这样可以避免链接数据存放在js脚本中的情况
			//String inputHTML = parser.parse(null).toHtml();
			//parser.setInputHTML(inputHTML);
			/*
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
				
				if(link == null || "".equals(link)) continue;
				if(!CrawlerUtil.canCrwal(link)) continue;
				if(link.equals(url)) continue;
				if(title.contains("详细")||title.contains("更多")) continue;
				
				if(!link.startsWith("http://")){
					link = CrawlerUtil.addLinkHeader(link, httpHeader);
				}
				//if(!Pattern.matches("http://soft.tompda.com/c/softs/\\d+/s_\\d+.html", link)) continue;
				if(log.isDebugEnabled()) log.info("     待抓取的链接："+link);
				FolderBO folder = new FolderBO();
				folder.setLink(link);
				folder.setTitle(title);
				links.add(folder);
			}
			*/
		}catch(Exception e){
			//System.out.println("取得页面["+url+"]内的所有超链接地址失败");
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
			if(log.isDebugEnabled()) log.debug(url+" | "+parser.getEncoding());
			
			if(!"GBK".equalsIgnoreCase(parser.getEncoding())) parser.setEncoding("GBK");
			
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class));
			NodeIterator iter = nodeList.elements();
			String data = "";
			while(iter.hasMoreNodes()){
				ScriptTag node = (ScriptTag)iter.nextNode();
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
				log.info("未取得页面["+url+"]脚本数据");
			}
		}catch(Exception e){
			log.info("取得新浪四川页面["+url+"]内的所有超链接地址失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return links;
	}
	
	/**
	 * 取得链接的顶级地址
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
	 * 给没有http://的链接添加完整路径
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
	 * 剔除文字中的连续<br/>标签，若抛出异常，则返回原输入字符串
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
	 * 格式化特殊字符
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
			content = content.replaceAll("&#8230;", "…");
		}catch(Exception e){
			content = pageContent;
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return content;
	}
	
	/**
	 * 将爬取的资源保存到文件中
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
	 * 将爬取过的地址记录到日志文件中
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
	 * 向xml文件最后追加内容
	 * 
	 */
	public static synchronized void writeFileAppand(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			if(fileLength>8) randomFile.seek(fileLength - 8);
			randomFile.writeBytes(content + "\n</page>");

			randomFile.close();
		} catch (IOException e) {
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
	}
	
	public static String getCharset(String link) {
		String charset = "GBK";
		
		try {
			Parser parser = new Parser(link);
			
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(MetaTag.class));
			if(nodeList!=null&&nodeList.size()>0){
				Node[] nodes = nodeList.toNodeArray();
				for(Node n : nodes){
					MetaTag meta = (MetaTag)n;
					String value = meta.getAttribute("content");
					if(value!=null&&!"".equals(value)&&value.toLowerCase().contains("charset")){
						charset = value.substring(value.lastIndexOf("=")+1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return charset.toUpperCase();
	}
	
	//TODO TEST
	public static void main(String[] args) {
		//System.out.println(CrawlerUtil.getFileDownloadAddress("http://www.moxiu.com/down.html?rid=6137935&file=theme/sisdd/ce/a1f/cea1f1d6/moxiu1261670130.sis"));
		try {
			String link = "http://mobile.zol.com.cn/more/2_426.shtml";
			Parser parser = new Parser(link);
			
			NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(MetaTag.class));
			Node[] nodes = nodeList.toNodeArray();
			for(Node n : nodes){
				MetaTag meta = (MetaTag)n;
				String value = meta.getAttribute("content");
				if(value!=null&&!"".equals(value)&&value.toLowerCase().contains("charset")){
					System.out.println(value.substring(value.lastIndexOf("=")+1));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
