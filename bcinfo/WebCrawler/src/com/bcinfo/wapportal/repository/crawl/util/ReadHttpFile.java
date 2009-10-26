package com.bcinfo.wapportal.repository.crawl.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadHttpFile {

	/**
	 * 读取http文件内容
	 * */
	public static String getPageContent(String strUrl) {
		//读取结果网页
    	StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        try {
        	URL newUrl = new URL(strUrl);
        	HttpURLConnection hConnect = (HttpURLConnection) newUrl.openConnection();	            
		    //读取内容
        	BufferedReader rd = new BufferedReader(new InputStreamReader(hConnect.getInputStream()));
        	int ch;
        	for (int length = 0; (ch = rd.read()) > -1; length++) buffer.append((char) ch);
        	rd.close();
        	hConnect.disconnect();
        	return buffer.toString().trim();
        } catch (Exception e) {
		    // return "错误:读取网页失败！";
        	return "";
        }
		        
    }
	
	public String getPageContent_utf(String strUrl)
    {
    	//读取结果网页
    	StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
	        try {
		             URL newUrl = new URL(strUrl);
		             HttpURLConnection hConnect = (HttpURLConnection) newUrl.openConnection();	            
		            //读取内容
		             BufferedReader rd = new BufferedReader(new InputStreamReader(hConnect.getInputStream(),"utf-8"));
		             int ch;
		             for (int length = 0; (ch = rd.read()) > -1; length++)
		            	 buffer.append((char) ch);
		            		rd.close();
		            		hConnect.disconnect();
		            		return buffer.toString().trim();
		        } catch (Exception e) {
		            // return "错误:读取网页失败！";
		            return "";
		        }
		        
    }
	
}
