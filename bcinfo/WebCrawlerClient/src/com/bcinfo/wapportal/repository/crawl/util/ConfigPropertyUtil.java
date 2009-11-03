/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 下午12:02:58<br>
 */
public class ConfigPropertyUtil {

	private static Logger log = Logger.getLogger(ConfigPropertyUtil.class);
	
	public Properties getConfigProperty(){
		Properties property = null;
		InputStream in = null;
		try{
			String os = System.getenv("OS");
			if(os!=null){
				in = new BufferedInputStream (new FileInputStream("src/config.properties"));
			}else{
				try{
					//四川
					in = new BufferedInputStream (new FileInputStream("/usr/local/jboss-3.2.7/server/default/deploy/webcrawlerclient/config/config.properties"));
				}catch(FileNotFoundException e){
					in = null;
				}
				if(in==null){
					try{
						//江西
						in = new BufferedInputStream (new FileInputStream("/usr/local/bea/apache-tomcat-6.0.18/webcrawlerclient/config/config.properties"));
					}catch(FileNotFoundException e){
						in = null;
					}
				}
				
				if(log.isDebugEnabled()){
					if(in != null){
						System.out.println(" InputStream is not null... ");
					}else{
						System.out.println(" InputStream not null... ");
					}
				}
			}
			
			property = new Properties();
			if(in!=null){
				property.load(in);
				in.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return property;
	}
}
