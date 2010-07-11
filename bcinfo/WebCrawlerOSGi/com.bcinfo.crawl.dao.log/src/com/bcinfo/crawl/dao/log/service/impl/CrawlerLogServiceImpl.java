/**
 * 
 */
package com.bcinfo.crawl.dao.log.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.log.database.DatabaseConnection;
import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.domain.model.CrawlerLog;

/**
 * @author dongq
 * 
 *         create time : 2010-6-1 上午11:14:40
 */
public class CrawlerLogServiceImpl implements CrawlerLogService {

	private static final Log log = LogFactory.getLog(CrawlerLogServiceImpl.class);
	
	@Override
	public boolean save(CrawlerLog crawlerLog) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "INSERT INTO CRAWLER_LOG(CHANNEL_ID, URL) VALUES(?,?)";
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(  1, crawlerLog.getChannelId());
			pst.setString(2, crawlerLog.getUrl());
			//int update = 
			pst.executeUpdate();
			//log.info("日志保存:" + update);
			conn.commit();
			bln = true;
		} catch (Exception e) {
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	@Override
	public boolean save(CrawlerLog[] logs) {
		boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "INSERT INTO CRAWLER_LOG(CHANNEL_ID, URL) VALUES(?,?)";
		
		try {
			conn = DatabaseConnection.getConnection();
			conn.setAutoCommit(false);
			
			pst = conn.prepareStatement(sql);
			for(CrawlerLog crawlerLog : logs) {
				pst.setLong(  1, crawlerLog.getChannelId());
				pst.setString(2, crawlerLog.getUrl());
				pst.addBatch();
			}
			int[] batch = pst.executeBatch();
			log.info("批量保存:" + batch.length);
			conn.commit();
			conn.setAutoCommit(true);
			bln = true;
		} catch (Exception e) {
			log.error(e);
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	@Override
	public List<CrawlerLog> get(Long id) {
		List<CrawlerLog> crawlerLogs = new ArrayList<CrawlerLog>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT CHANNEL_ID, URL FROM CRAWLER_LOG WHERE CHANNEL_ID = ?";
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, id);
			rs = pst.executeQuery();
			while(rs.next()) {
				crawlerLogs.add(new CrawlerLog(id, rs.getString("URL")));
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return crawlerLogs;
	}

	

	private void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(pst != null) pst.close();
			if(conn != null) conn.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	private void rollback(Connection conn) {
		try {
			if(conn != null) conn.rollback();
		} catch (Exception e) {
			log.error(e);
		}
	}
}
