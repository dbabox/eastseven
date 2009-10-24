package com.bcinfo.wapportal.repository.crawl.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JavaOracle {

	public JavaOracle() {}
	
	private static String className = "oracle.jdbc.driver.OracleDriver";
	//jdbc:oracle:thin:@218.205.231.65:1521:fxk
	private static String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	private static String user = "wapsc";//
	private static String password = "wapsc";//hello520tshirt
	
	public static Connection getConn() {
		Connection conn = null;
		try {
			// OracleDriver exist check
			//Class.forName(className);
			// Get Connection
			conn = DriverManager.getConnection("proxool.oracle");//DriverManager.getConnection(url, user, password);
		} catch (Exception ex) {
			ex.printStackTrace();
			//return null;
		}
		return conn;
	}
	
	public static Connection getConn(String className, String url, String user, String password){
		Connection conn = null;
		try {
			// OracleDriver exist check
			Class.forName(className);
			// Get Connection
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return conn;
	}
	
}
