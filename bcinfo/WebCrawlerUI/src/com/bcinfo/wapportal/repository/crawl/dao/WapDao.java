/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.Dao;

/**
 * @author dongq
 * 
 *         create time : 2009-12-16 ÏÂÎç12:26:04
 */
public class WapDao extends Dao {

	public List<String> getAll() {
		List<String> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select key_value from wap_filter");
			rs = pst.executeQuery();
			list = new ArrayList<String>();
			while(rs.next()){
				String key = rs.getString("key_value");
				key = new String(key.getBytes("GBK"));
				list.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		
		return list;
	}
}
