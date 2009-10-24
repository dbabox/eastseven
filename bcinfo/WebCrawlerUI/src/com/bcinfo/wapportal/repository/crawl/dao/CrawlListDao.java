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
import com.bcinfo.wapportal.repository.crawl.domain.CrawlList;

/**
 * @author dongq
 * 
 *         create time : 2009-10-18 上午11:33:41<br>
 */
public class CrawlListDao {
	
	public List<CrawlList> getAllCrawlList(){
		List<CrawlList> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			String sql = "select a.crawl_id,a.channel_id, (select b.channel_name from twap_public_channel b where a.channel_id=b.channel_id) channel_name,a.crawl_url,decode(a.crawl_status,'1','正常','0','停用','未知') crawl_status,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_crawl_list a order by a.channel_id,a.create_time,a.crawl_id";
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			list = new ArrayList<CrawlList>();
			CrawlList crawl = null;
			while(rs.next()){
				crawl = new CrawlList();
				crawl.setCrawlId(rs.getLong("crawl_id"));
				crawl.setChannelId(rs.getLong("channel_id"));
				crawl.setChannelName(rs.getString("channel_name"));
				crawl.setCrawlUrl(rs.getString("crawl_url"));
				crawl.setCrawlStatus(rs.getString("crawl_status"));
				crawl.setCreateTime(rs.getString("create_time"));
				list.add(crawl);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return list;
	}
	
	public Boolean saveCrawlList(Long channelId, String url){
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "insert into twap_public_crawl_list(crawl_id,channel_id,crawl_url) values(seq_twap_public_crawl_list.nextval,?,?)";
		
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.setString(2, url);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			if(conn != null){
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
	
	public Boolean updateBatch(String[] ids,String status){
		boolean bln =false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_list a set a.crawl_status = ? where a.crawl_id = ? ";
		
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for(String id : ids){
				pst.setString(1, status);
				pst.setLong(2, Long.parseLong(id));
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			bln = true;
		}catch(Exception e){
			if(conn != null){
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
