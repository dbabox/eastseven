/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-12-6 ÏÂÎç02:02:45
 */
public final class QuartzDaoService extends Dao{

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ZK", "wap", "wap");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static Connection getConnection(String driver, String url, String user, String pwd) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public List<String> getAllTablesName() {
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = OracleUtil.getConnection();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, "QRTZ%", new String[]{"TABLE"});
			while(rs.next()){
				if(!"".equals(rs.getString("TABLE_NAME"))){
					list.add(rs.getString("TABLE_NAME"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<String> getTableColumnName(String tableName) {
		List<String> list = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select * from " + tableName +" where 1=0");
			rs = pst.executeQuery();
			if(rs != null){
				ResultSetMetaData rsMeta = rs.getMetaData();
				for(int column = 1;column <= rsMeta.getColumnCount();column++){
					String key = rsMeta.getColumnName(column);
					list.add(key);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<Map<String, Object>> getListAll(String tableName) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select * from " + tableName);
			rs = pst.executeQuery();
			if(rs != null){
				list = new ArrayList<Map<String,Object>>();
				ResultSetMetaData rsMeta = rs.getMetaData();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					for(int column = 1;column <= rsMeta.getColumnCount();column++){
						String key = rsMeta.getColumnName(column);
						Object value = rs.getObject(key);
						map.put(key, value);
						
					}
					list.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<Map<String, Object>> getListAll() {
		List<Map<String, Object>> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select * from qrtz_job_details");
			rs = pst.executeQuery();
			if(rs != null){
				list = new ArrayList<Map<String,Object>>();
				ResultSetMetaData rsMeta = rs.getMetaData();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					for(int column = 1;column <= rsMeta.getColumnCount();column++){
						String key = rsMeta.getColumnName(column);
						Object value = rs.getObject(key);
						map.put(key, value);
						
					}
					list.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return list;
	}
	
	public static void main(String[] args) {
		String userDir = (String)System.getProperties().get("user.dir");
		System.out.println(userDir);
		Connection conn = getConnection("org.apache.derby.jdbc.EmbeddedDriver","jdbc:derby:E:\\dev\\eclipse\\quartz","quartz","quartz");
		if(conn != null){
			System.out.println(conn);
		}else{
			System.out.println("connection is null");
		}
		
		List<Map<String, Object>> list = new QuartzDaoService().getListAll();
		if(list!=null&&!list.isEmpty()){
			for(Map<String, Object> map : list){
				Set<String> keySet = map.keySet();
				for(String key : keySet){
					System.out.println(key+":"+map.get(key));
				}
				System.out.println("------------------------");
			}
		}
	}
}
