/**
 * 
 */
package com.bcinfo.crawl.dao.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bcinfo.crawl.dao.service.WebCrawlerDaoXE;
import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-6-9 ÏÂÎç02:29:49
 */
public class WebCrawlerDaoImplXe implements WebCrawlerDaoXE {

	@Override
	public String getSystemDate(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isExist(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean save(Resource resource, Connection conn) {
		boolean bln = false;
		final String sql = " insert into twap_public_crawl_resource(res_id,channel_id,res_title,res_link,res_text,res_status) values(seq_twap_public_crawl_resource.nextval,?,?,?,?,?) ";
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setLong(1, resource.getSite().getChannelId());
			pst.setString(2, resource.getTitle());
			pst.setString(3, resource.getLink());
			pst.setString(4, resource.getContent());
			pst.setString(5, resource.getStatus());
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			if(conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return bln;
	}

	@Override
	public Boolean saveBatch(Resource[] resources, Connection conn) {
		boolean bln = false;

		PreparedStatement pst = null;
		final String sql = " insert into twap_public_crawl_resource(res_id,channel_id,res_title,res_link,res_text,res_status) values(seq_twap_public_crawl_resource.nextval,?,?,?,?,?) ";
		
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			
			for(Resource resource : resources) {
				pst.setLong(1, resource.getSite().getChannelId());
				pst.setString(2, resource.getTitle());
				pst.setString(3, resource.getLink());
				pst.setString(4, resource.getContent());
				pst.setString(5, resource.getStatus());
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(pst != null) {
				try {
					pst.clearParameters();
					pst.clearBatch();
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return bln;
	}

	@Override
	public Boolean saveBatch(Object[] resources, Connection conn) {
		boolean bln = false;
		
		Resource[] res = new Resource[resources.length];
		for(int index=0; index<resources.length; index++) {
			res[index] = (Resource)resources[index];
		}
		
		bln = saveBatch(res, conn);
		
		return bln;
	}

}
