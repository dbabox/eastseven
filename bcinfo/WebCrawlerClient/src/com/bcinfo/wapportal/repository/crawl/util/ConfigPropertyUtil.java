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
 *         create time : 2009-10-21 ����12:02:58<br>
 */
public class ConfigPropertyUtil {

	private static Logger log = Logger.getLogger(ConfigPropertyUtil.class);
	
	public Properties getConfigProperty(String fileName){
		Properties property = null;
		InputStream in = null;
		try{
			//ȡ�õ�ǰ�������ڵĸ�Ŀ¼
			String userDir = System.getProperty("user.dir");
			
			if(userDir != null && !"".equals(userDir)){
				File configFolder = new File(userDir+"/config");
				if(configFolder.exists()){
					//��������,Windows or Linux
					in = new BufferedInputStream(new FileInputStream(userDir+"/config/"+fileName));
				}else{
					//��������
					in = new BufferedInputStream(new FileInputStream(userDir+"/src/"+fileName));
				}
			}else{
				//δ�ҵ�����·��
				System.out.println("δ�ҵ�����·��,�޷�����"+fileName+"�ļ�");
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
					//�Ĵ�
					in = new BufferedInputStream (new FileInputStream("/usr/local/jboss-3.2.7/server/default/deploy/webcrawlerclient/config/config.properties"));
				}catch(FileNotFoundException e){
					in = null;
				}
				if(in==null){
					try{
						//����
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
