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
 *         create time : 2009-10-21 ÏÂÎç12:02:58<br>
 */
public class ConfigPropertyUtil {

	public Properties getConfigProperty(){
		Properties property = null;
		try{
			//TODO E:/dev/eclipse_jee_galileo_spring/workspace/WebCrawlerUI/src/
			InputStream in = new BufferedInputStream (new FileInputStream("E:/dev/eclipse_jee_galileo_spring/workspace/WebCrawlerUI/src/config.properties"));
			property = new Properties();
			property.load(in);
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return property;
	}
}
