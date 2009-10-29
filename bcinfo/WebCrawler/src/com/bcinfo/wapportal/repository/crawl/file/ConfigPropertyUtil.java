/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 下午12:02:58<br>
 */
public class ConfigPropertyUtil {

	public Properties getConfigProperty(){
		Properties property = null;
		InputStream in = null;
		try{
			String os = System.getenv("OS");
			if(os!=null){
				in = new BufferedInputStream (new FileInputStream("src/config.properties"));
			}else{
				in = new BufferedInputStream (new FileInputStream("/usr/local/oracle/apache-tomcat-6.0.18/webcrawler/config/config.properties"));
				if(in != null){
					System.out.println(" 成功读取/usr/local/oracle/apache-tomcat-6.0.18/webcrawler/config/config.properties ");
				}else{
					System.out.println(" 读取/usr/local/oracle/apache-tomcat-6.0.18/webcrawler/config/config.properties失败 ");
				}
			}
			property = new Properties();
			property.load(in);
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return property;
	}
}
