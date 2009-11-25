package com.bcinfo.wapportal.repository.crawl.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author dongq
 * 
 */
public class OracleUtil {
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("proxool.oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {
		System.out.println(getConnection());
	}
}
