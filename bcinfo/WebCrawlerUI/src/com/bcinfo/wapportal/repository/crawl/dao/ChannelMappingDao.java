/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.ChannelMapping;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 ÉÏÎç11:30:42
 */
public class ChannelMappingDao {

	public List<ChannelMapping> getChannelMappingList(Long channelId){
		List<ChannelMapping> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.mapping_id,a.channel_id,a.local_code,a.local_channel_id,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_channel_mapping a where a.channel_id = ?";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			list = new ArrayList<ChannelMapping>();
			ChannelMapping map = null;
			while(rs.next()){
				map = new ChannelMapping();
				map.setChannelId(rs.getLong("channel_id"));
				map.setCreateTime(rs.getString("create_time"));
				map.setLocalChannelId(rs.getString("local_channel_id"));
				map.setLocalCode(rs.getString("local_code"));
				map.setMappingId(rs.getLong("mapping_id"));
				list.add(map);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<ChannelMapping> getAllChannelMappingList(){
		List<ChannelMapping> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.mapping_id,a.channel_id,a.local_code,a.local_channel_id,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_channel_mapping a order by a.local_code,a.local_channel_id,a.channel_id";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			
			rs = pst.executeQuery();
			list = new ArrayList<ChannelMapping>();
			ChannelMapping map = null;
			while(rs.next()){
				map = new ChannelMapping();
				map.setChannelId(rs.getLong("channel_id"));
				map.setCreateTime(rs.getString("create_time"));
				map.setLocalChannelId(rs.getString("local_channel_id"));
				map.setLocalCode(rs.getString("local_code"));
				map.setMappingId(rs.getLong("mapping_id"));
				list.add(map);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public Boolean save(String localCode, String localChannelId, String channelId){
		Boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_channel_mapping(mapping_id,channel_id,local_code,local_channel_id) values(seq_twap_public_chl_mapping.nextval,?,?,?) ";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, Long.parseLong(channelId));
			pst.setString(2, localCode);
			pst.setString(3, localChannelId);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}finally{
			close(conn, pst, rs);
		}
		return bln;
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
