/**
 * 
 */
package com.bcinfo.crawl.dao.oralce9i;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dongq
 * 
 * 			create time : 2010-5-12 上午11:40:51
 */
public final class DatabaseConnection {

	private static final Log log = LogFactory.getLog(DatabaseConnection.class);
	
	private static String className = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	private static String user = "scwap";
	private static String password = "scwap";
	
	static {
		try {
			final String name = System.getProperty("user.dir") + "/conf/jdbc.properties";
			Properties config = new Properties();
			config.load(new FileInputStream(name));
			className = config.getProperty("oracle9i.className");
			url = config.getProperty("oracle9i.url");
			user = config.getProperty("oracle9i.user");
			password = config.getProperty("oracle9i.password");
			Class.forName(className);
			log.info("JDBC配置文件加载成功["+name+"]");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
