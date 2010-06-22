/**
 * 
 */
package com.bcinfo.crawl.dao.log.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.log.database.DatabaseConnection;
import com.bcinfo.crawl.dao.log.service.CrawlerDataAccessService;
import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-6-2 ÉÏÎç08:51:24
 */
public class CrawlerDataAccessServiceImpl implements CrawlerDataAccessService {

	private static final Log log = LogFactory.getLog(CrawlerDataAccessServiceImpl.class);
	
	@Override
	public Boolean save(Resource resource) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "INSERT INTO TWAP_PUBLIC_CRAWL_RESOURCE(CHANNEL_ID, RES_TITLE, RES_LINK, RES_CONTENT) VALUES(?,?,?,?)";
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, resource.getChannel().getId());
			pst.setString(2, resource.getTitle());
			pst.setString(3, resource.getLink());
			pst.setString(4, resource.getContent());
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
