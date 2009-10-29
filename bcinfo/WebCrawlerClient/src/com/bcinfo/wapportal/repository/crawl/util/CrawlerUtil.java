/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author dongq
 * 
 *         create time : 2009-9-10 下午04:26:20<br>
 *         抓取工具类<br>
 */
public final class CrawlerUtil {
	
	/**
	 * 取得链接的顶级地址
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public static String extractLinkHeader(String link) throws Exception{
		String http = "";
		if(link.indexOf("http://")!=-1){
			String tmp = link.substring("http://".length());
			http = "http://" + tmp.substring(0, tmp.indexOf("/"));
		}
		return http;
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
			completeLink = httpHeader + link;
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
			//content = content.replaceAll("$$", "$");
			content = content.replaceAll("&nbsp;", " ");//&nbsp;
			content = content.replaceAll("&shy;", "-");
		}catch(Exception e){
			content = pageContent;
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
}
