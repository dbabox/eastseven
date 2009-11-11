/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.BufferedInputStream;
import java.io.File;
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
	
	public Properties getConfigProperty(String fileName){
		Properties property = null;
		InputStream in = null;
		try{
			//取得当前工程所在的根目录
			String userDir = System.getProperty("user.dir");
			
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//生产环境,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/"+fileName));
				}else{
					//开发环境
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/"+fileName));
				}
			}else{
				//未找到工程路径
				System.out.println("未找到工程路径,无法加载"+fileName+"文件");
			}
			
			property = new Properties();
			property.load(in);
			in.close();
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		return property;
	}
	
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
