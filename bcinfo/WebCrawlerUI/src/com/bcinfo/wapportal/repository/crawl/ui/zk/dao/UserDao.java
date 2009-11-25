package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * @author dongq
 * 
 *         create time : 2009-11-24 ÉÏÎç10:03:53
 */
public class UserDao extends Dao {

	public UserBean getUser(String userName){
		UserBean user = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.user_id,a.user_name,a.user_password,a.local_code,a.usert_status,to_char(a.create_time,'yyyy-mm-dd') create_time from twap_public_user a where a.user_name = ? ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			rs = pst.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setCreateTime(rs.getString("create_time"));
				user.setLocalCode(rs.getString("local_code"));
				user.setPassword(rs.getString("user_password"));
				user.setUserId(rs.getLong("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setUserStatus(rs.getString("usert_status"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return user;
	}
}
