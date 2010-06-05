/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;

/**
 * @author dongq
 * 
 *         create time : 2009-10-23 ÏÂÎç01:57:21
 */
public class UserDao {

	public String getUserName(Long userId) throws Exception {
		String userName = "";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.user_name from twap_public_user a where a.user_id = ?";
		conn = JavaOracle.getConn();
		pst = conn.prepareStatement(sql);
		pst.setLong(1, userId);
		rs = pst.executeQuery();
		if(rs.next()) userName = rs.getString("user_name");
		
		if(rs != null) rs.close();
		if(pst != null) pst.close();
		if(conn != null) conn.close();
		
		return userName;
	}
	
	public Long getLoginUserId(String userName, String password){
		Long userId = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.user_id from twap_public_user a where a.user_name = ? and a.user_password = ?";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if(rs.next()){
				userId = rs.getLong("user_id");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return userId;
	}
	
	public String getUserIdLocal(Long userId){
		String localCode = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.local_code from twap_public_user a where a.user_id = ?";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, userId);
			rs = pst.executeQuery();
			if(rs.next()){
				localCode = rs.getString("local_code");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return localCode;
	}
	
	void close(Connection conn, PreparedStatement pst, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pst != null){
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
