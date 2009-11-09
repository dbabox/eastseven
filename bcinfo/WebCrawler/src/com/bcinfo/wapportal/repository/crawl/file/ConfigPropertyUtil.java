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
 *         create time : 2009-10-21 ����12:02:58<br>
 */
public class ConfigPropertyUtil {

	public static Properties getLog4jProperty(){
		Properties property = null;
		InputStream in = null;
		try{
			//ȡ�õ�ǰ�������ڵĸ�Ŀ¼
			String userDir = System.getProperty("user.dir");
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//��������,�ļ�����configĿ¼��,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/log4j.properties"));
				}else{
					//��������,�ļ�����srcĿ¼��
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/log4j.properties"));
				}
			}else{
				//δ�ҵ�����·��
				System.out.println("δ�ҵ�����·��,�޷�����log4j.properties�ļ�");
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
			//ȡ�õ�ǰ�������ڵĸ�Ŀ¼
			String userDir = System.getProperty("user.dir");
			
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//��������,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/config.properties"));
				}else{
					//��������
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/config.properties"));
				}
			}else{
				//δ�ҵ�����·��
				System.out.println("δ�ҵ�����·��,�޷�����config.properties�ļ�");
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
