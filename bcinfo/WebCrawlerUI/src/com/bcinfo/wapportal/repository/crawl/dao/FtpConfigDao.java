/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;

/**
 * @author dongq
 * 
 *         create time : 2009-10-21 ÏÂÎç01:29:35
 */
public class FtpConfigDao {

	public Map<String, String> getFtpConfig(String code){
		Map<String, String> ftp = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.ftp_host,a.ftp_user,a.ftp_password,a.ftp_dir,a.ftp_port from twap_public_ftp a where a.local_code = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, code);
			rs = pst.executeQuery();
			if(rs.next()){
				ftp = new HashMap<String, String>();
				ftp.put("ftp_host", rs.getString("ftp_host"));
				ftp.put("ftp_user", rs.getString("ftp_user"));
				ftp.put("ftp_password", rs.getString("ftp_password"));
				ftp.put("ftp_dir", rs.getString("ftp_dir"));
				ftp.put("ftp_port", rs.getString("ftp_port"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return ftp;
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
