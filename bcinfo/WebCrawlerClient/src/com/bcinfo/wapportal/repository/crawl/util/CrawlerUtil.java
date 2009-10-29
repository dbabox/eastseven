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
 *         create time : 2009-9-10 ����04:26:20<br>
 *         ץȡ������<br>
 */
public final class CrawlerUtil {
	
	/**
	 * ȡ�����ӵĶ�����ַ
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
	 * ��û��http://�������������·��
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
			//content = content.replaceAll("$$", "$");
			content = content.replaceAll("&nbsp;", " ");//&nbsp;
			content = content.replaceAll("&shy;", "-");
		}catch(Exception e){
			content = pageContent;
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
}
