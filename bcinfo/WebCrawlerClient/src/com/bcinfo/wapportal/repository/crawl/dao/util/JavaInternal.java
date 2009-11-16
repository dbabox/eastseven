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
 *         create time : 2009-11-16 上午09:56:31<br>
 *         内存数据库连接
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
			log.info("内存数据库启动加载成功["+url+"]");
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("内存数据库连接出错");
		}
		
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(url, user, password);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.error("取得内存数据库连接失败");
		}
		return conn;
	}
	
	public static void main(String[] args) {
		System.out.println(getConnection());
	}

}
