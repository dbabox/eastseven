package com.bcinfo.wapportal.repository.crawl.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.bcinfo.wapportal.repository.crawl.util.ConfigPropertyUtil;

public class JavaOracle {

	public JavaOracle() {}
	
	private static String className = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@218.205.231.65:1521:fxk";
	private static String user = "scwap";//
	private static String password = "scwap";//hello520tshirt
	
	public static Connection getConn() {
		Connection conn = null;
		try {
			Properties p = new ConfigPropertyUtil().getConfigProperty();
			className = p.getProperty("db.className");
			url = p.getProperty("db.url");
			user = p.getProperty("db.user");
			password = p.getProperty("db.password");
			// OracleDriver exist check
			Class.forName(className);
			// Get Connection
			conn = DriverManager.getConnection(url, user, password);
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
	
	/**
	 * �Զ������ݿ����ӣ���Ҫ���ڲ���
	 * @param className oracle.jdbc.driver.OracleDriver
	 * @param url jdbc:oracle:thin:@218.205.231.65:1521:fxk
	 * @param user scwap
	 * @param password scwap
	 * @return
	 */
	public static Connection getTestConn(){
		return getConn("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@218.205.231.65:1521:fxk","scwap","scwap");
	}
	
	public static void main(String[] args) {
		System.out.println(getConn());
	}
}