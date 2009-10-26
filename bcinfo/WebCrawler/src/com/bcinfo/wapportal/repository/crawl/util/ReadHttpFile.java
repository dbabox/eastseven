package com.bcinfo.wapportal.repository.crawl.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadHttpFile {

	/**
	 * ��ȡhttp�ļ�����
	 * */
	public static String getPageContent(String strUrl) {
		//��ȡ�����ҳ
    	StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        try {
        	URL newUrl = new URL(strUrl);
        	HttpURLConnection hConnect = (HttpURLConnection) newUrl.openConnection();	            
		    //��ȡ����
        	BufferedReader rd = new BufferedReader(new InputStreamReader(hConnect.getInputStream()));
        	int ch;
        	for (int length = 0; (ch = rd.read()) > -1; length++) buffer.append((char) ch);
        	rd.close();
        	hConnect.disconnect();
        	return buffer.toString().trim();
        } catch (Exception e) {
		    // return "����:��ȡ��ҳʧ�ܣ�";
        	return "";
        }
		        
    }
	
	public String getPageContent_utf(String strUrl)
    {
    	//��ȡ�����ҳ
    	StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
	        try {
		             URL newUrl = new URL(strUrl);
		             HttpURLConnection hConnect = (HttpURLConnection) newUrl.openConnection();	            
		            //��ȡ����
		             BufferedReader rd = new BufferedReader(new InputStreamReader(hConnect.getInputStream(),"utf-8"));
		             int ch;
		             for (int length = 0; (ch = rd.read()) > -1; length++)
		            	 buffer.append((char) ch);
		            		rd.close();
		            		hConnect.disconnect();
		            		return buffer.toString().trim();
		        } catch (Exception e) {
		            // return "����:��ȡ��ҳʧ�ܣ�";
		            return "";
		        }
		        
    }
	
}
