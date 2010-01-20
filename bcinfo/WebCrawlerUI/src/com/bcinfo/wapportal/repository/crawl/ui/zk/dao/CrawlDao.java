package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.CrawlBean;

/**
 * @author dongq
 * 
 *         create time : 2009-11-23 下午04:22:40
 */
public class CrawlDao extends Dao {

	public Boolean save(CrawlBean crawlBean) {
		return save(crawlBean.getChannelId(), crawlBean.getCrawlUrl());
	}
	
	public Boolean save(Long channelId, String url) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_crawl_list(crawl_id,channel_id,crawl_url) values(seq_twap_public_crawl_list.nextval,?,?) ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.setString(2, url);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean delete(CrawlBean crawlBean) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete from twap_public_crawl_list a where a.crawl_id = ?";
		
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, crawlBean.getCrawlId());
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean delete(List<CrawlBean> crawlBeans) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete from twap_public_crawl_list a where a.crawl_id = ?";
		
		try {
			conn = OracleUtil.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			
			for(CrawlBean crawlBean : crawlBeans){
				pst.setLong(1, crawlBean.getCrawlId());
				pst.addBatch();
			}
			pst.executeBatch();
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean updateStatus(List<CrawlBean> crawlBeans) {
		boolean bln = false;
		List<Long> list = new ArrayList<Long>();
		String status = "";
		for(CrawlBean bean : crawlBeans){
			list.add(bean.getCrawlId());
			status = bean.getCrawlStatus();
		}
		bln = updateStatus(list, status);
		return bln;
	}
	
	public Boolean updateStatus(List<Long> list, String status) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_list a set a.crawl_status = ? where a.crawl_id = ? ";
		
		try{
			conn = OracleUtil.getConnection();
			if(list != null && !list.isEmpty()){
				if(list.size() > 1){
					conn.setAutoCommit(false);
					pst = conn.prepareStatement(sql);
					for(Long id : list){
						pst.setString(1, status);
						pst.setLong(2, id);
						pst.addBatch();
					}
					pst.executeBatch();
					conn.setAutoCommit(true);
					conn.commit();
					bln = true;
				}else{
					pst = conn.prepareStatement(sql);
					pst.setString(1, status);
					pst.setLong(2, list.get(0));
					pst.executeUpdate();
					conn.commit();
					bln = true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public List<CrawlBean> getCrawlList(Long channelId){
		List<CrawlBean> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.crawl_id,a.channel_id,(select t.channel_name from twap_public_channel t where a.channel_id = t.channel_id) channel_name,a.crawl_url,decode(a.crawl_status,'0','停用','1','正常') crawl_status,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_crawl_list a where exists( select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid ) ";
		
		try{
			conn = OracleUtil.getConnection();
			if(channelId == null){
				sql = " select a.crawl_id,a.channel_id,(select t.channel_name from twap_public_channel t where a.channel_id = t.channel_id) channel_name,a.crawl_url,decode(a.crawl_status,'0','停用','1','正常') crawl_status,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_crawl_list a ";
				pst = conn.prepareStatement(sql);
			}else{
				pst = conn.prepareStatement(sql);
				pst.setLong(1, channelId);
			}
			rs = pst.executeQuery();
			list = new ArrayList<CrawlBean>();
			CrawlBean bean = null;
			while(rs.next()){
				bean = new CrawlBean();
				bean.setChannelId(rs.getLong("channel_id"));
				bean.setChannelName(rs.getString("channel_name"));
				bean.setCrawlId(rs.getLong("crawl_id"));
				bean.setCrawlStatus(rs.getString("crawl_status"));
				bean.setCrawlUrl(rs.getString("crawl_url"));
				bean.setCreateTime(rs.getString("create_time"));
				list.add(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
}
