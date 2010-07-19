/**
 * 
 */
package com.bcinfo.crawl.dao.log.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.log.database.DatabaseConnection;
import com.bcinfo.crawl.dao.log.service.CrawlerLogMonitor;

/**
 * @author dongq
 * 
 *         create time : 2010-7-13 上午10:39:01
 */
public class CrawlerLogMonitorImpl implements CrawlerLogMonitor {

	private static final Log log = LogFactory.getLog(CrawlerLogMonitorImpl.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public boolean clearCrawlerLogs(long channelId) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "DELETE FROM CRAWLER_LOG WHERE CHANNEL_ID = ?";
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			int count = pst.executeUpdate();
			log.info("清除频道["+channelId+"]["+count+"]条日志记录");
			conn.commit();
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
	public void displayAllCrawlerLogs() {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT URL,CREATE_TIME FROM CRAWLER_LOG ORDER BY CHANNEL_ID ASC,CREATE_TIME DESC";
		String displayMsg = "\n";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			int index = 1;
			displayMsg += "\n频道抓取记录：";
			while(rs.next()) {
				
				String url = rs.getString("URL");
				String date = sdf.format(new Date(rs.getTimestamp("CREATE_TIME").getTime()));
				displayMsg += "\n" + index+"."+date+" : "+url;
				index++;
			}
			displayMsg += "\n";
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		log.info(displayMsg);
	}

	@Override
	public void displayCrawlerLogs(long channelId) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT URL,CREATE_TIME FROM CRAWLER_LOG WHERE CHANNEL_ID = ? ORDER BY CREATE_TIME DESC";
		String displayMsg = "\n";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			int index = 1;
			displayMsg += "\n频道["+channelId+"]抓取记录：";
			while(rs.next()) {
				
				String url = rs.getString("URL");
				String date = sdf.format(new Date(rs.getTimestamp("CREATE_TIME").getTime()));
				displayMsg += "\n" + index+"."+date+" : "+url;
				index++;
			}
			displayMsg += "\n";
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		log.info(displayMsg);
	}

	@Override
	public List<Map<String, String>> getAllCrawlerLogs() {
		List<Map<String, String>> crawlerLogs = new ArrayList<Map<String, String>>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT CHANNEL_ID,URL,CREATE_TIME FROM CRAWLER_LOG ORDER BY CHANNEL_ID ASC,CREATE_TIME DESC";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()) {
				Map<String, String> e = new HashMap<String, String>();
				String channelId = String.valueOf(rs.getLong("CHANNEL_ID"));
				String url = rs.getString("URL");
				String date = sdf.format(new Date(rs.getTimestamp("CREATE_TIME").getTime()));
				e.put("channelId", channelId);
				e.put("url", url);
				e.put("date", date);
				crawlerLogs.add(e);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return crawlerLogs;
	}
	
	@Override
	public List<Map<String, String>> getCrawlerLogs(long channelId) {
		List<Map<String, String>> crawlerLogs = new ArrayList<Map<String, String>>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT URL,CREATE_TIME FROM CRAWLER_LOG WHERE CHANNEL_ID = ? ORDER BY CREATE_TIME DESC";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			while(rs.next()) {
				Map<String, String> e = new HashMap<String, String>();
				String url = rs.getString("URL");
				String date = sdf.format(new Date(rs.getTimestamp("CREATE_TIME").getTime()));
				e.put("channelId", String.valueOf(channelId));
				e.put("url", url);
				e.put("date", date);
				crawlerLogs.add(e);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return crawlerLogs;
	}
	
	@Override
	public List<String> getChannelIds() {
		List<String> channelIds = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT distinct CHANNEL_ID FROM CRAWLER_LOG ORDER BY CHANNEL_ID ASC";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()) {
				String channelId = String.valueOf(rs.getLong("CHANNEL_ID"));
				channelIds.add(channelId);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return channelIds;
	}
	
	@Override
	public long getCrawlerLogCountOfAll() {
		long count = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT count(1) log_size FROM CRAWLER_LOG";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if(rs.next()) count = rs.getLong("log_size");
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		return count;
	}
	
	@Override
	public long getCrawlerLogCount(long channelId) {
		long count = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "SELECT count(1) log_size FROM CRAWLER_LOG WHERE CHANNEL_ID = ?";
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			if(rs.next()) count = rs.getLong("log_size");
		} catch (Exception e) {
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		return count;
	}
	
	private void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void rollback(Connection conn) {
		try {
			if (conn != null)
				conn.rollback();
		} catch (Exception e) {
			log.error(e);
		}
	}
}
