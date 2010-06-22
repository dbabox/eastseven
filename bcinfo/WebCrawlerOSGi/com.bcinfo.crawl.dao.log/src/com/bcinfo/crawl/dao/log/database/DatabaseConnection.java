/**
 * 
 */
package com.bcinfo.crawl.dao.log.database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dongq
 * 
 * 			create time : 2010-5-12 上午11:40:51
 */
public final class DatabaseConnection {

	private static final Log log = LogFactory.getLog(DatabaseConnection.class);
	
	private static String className = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String url = "jdbc:derby:?;create=true";
	private static String user = "crawlerlog";
	private static String password = "crawlerlog";
	
	static {
		try {
			url = url.replace("?", System.getProperty("user.dir") + "/crawlerlog");
			Class.forName(className);
			log.info("抓取日志数据库URL:" + url);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return conn;
	}
}
