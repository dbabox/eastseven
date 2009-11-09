/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 下午12:02:58<br>
 */
public class ConfigPropertyUtil {

	public static Properties getLog4jProperty(){
		Properties property = null;
		InputStream in = null;
		try{
			//取得当前工程所在的根目录
			String userDir = System.getProperty("user.dir");
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//生产环境,文件放在config目录下,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/log4j.properties"));
				}else{
					//开发环境,文件放在src目录下
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/log4j.properties"));
				}
			}else{
				//未找到工程路径
				System.out.println("未找到工程路径,无法加载log4j.properties文件");
			}
			property = new Properties();
			property.load(in);
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return property;
	}
	
	public Properties getConfigProperty(){
		Properties property = null;
		InputStream in = null;
		try{
			//取得当前工程所在的根目录
			String userDir = System.getProperty("user.dir");
			
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//生产环境,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/config.properties"));
				}else{
					//开发环境
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/config.properties"));
				}
			}else{
				//未找到工程路径
				System.out.println("未找到工程路径,无法加载config.properties文件");
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
