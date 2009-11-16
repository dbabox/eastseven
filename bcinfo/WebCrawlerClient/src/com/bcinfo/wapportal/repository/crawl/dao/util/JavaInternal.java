/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

/**
 * @author dongq
 * 
 *         create time : 2009-11-16 ����09:56:31<br>
 *         �ڴ����ݿ�����
 */
public class JavaInternal {

	private static Logger log = Logger.getLogger(JavaInternal.class);
	
	private static String url = "";
	private static String user = "sa";
	private static String password = "";
	private static final String className = "org.hsqldb.jdbcDriver";
		
	static{
		try{
			String userDir = System.getProperty("user.dir")+"/config/database/";
			url = "jdbc:hsqldb:file:"+userDir+"internalDB";
			Class.forName(className);
			log.info("�ڴ����ݿ��������سɹ�["+url+"]");
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("�ڴ����ݿ����ӳ���");
		}
		
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(url, user, password);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("ȡ���ڴ����ݿ�����ʧ��");
		}
		return conn;
	}
	
	public static void main(String[] args) {
		System.out.println(getConnection());
	}

}
