/**
 * 
 */
package org.dongq.analytics.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author eastseven
 * 
 */
public class DbHelper {

	final static Log logger = LogFactory.getLog(DbHelper.class);
	
	public static String driver = "org.hsqldb.jdbcDriver";
	public static String url = "jdbc:derby:database";
	
	static {
		DbUtils.loadDriver(driver);
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			logger.debug(url);
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
