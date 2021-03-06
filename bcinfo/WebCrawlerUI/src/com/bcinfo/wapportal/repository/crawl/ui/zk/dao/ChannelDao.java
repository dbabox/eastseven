package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import common.Logger;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-11-24 下午02:23:14
 */
public class ChannelDao extends Dao {

	private static final Logger log = Logger.getLogger(ChannelDao.class);
	
	public boolean batchExecuteSQLSentence(String[] sqlScript) {
		boolean bln = true;//执行中
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			if(sqlScript!=null&&sqlScript.length>0){
				conn = JavaOracle.getConn();
				conn.setAutoCommit(false);
				
				pst = conn.prepareStatement("select sysdate from dual");
				for(String sql : sqlScript){
					sql = sql.trim();
					if(sql!=null&&!"".equals(sql)){
						pst.executeUpdate(sql);
						log.debug("Batch Execution SQL:"+sql);
					}
				}
				
				conn.setAutoCommit(true);
				conn.commit();
				bln = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean isLeaf(ChannelBean bean) {
		boolean leaf = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(1) from twap_public_channel a where a.channel_pid = ? ";

		if (bean != null) {
			try {
				conn = OracleUtil.getConnection();
				pst = conn.prepareStatement(sql);
				pst.setLong(1, bean.getChannelId());
				rs = pst.executeQuery();
				if (rs.next()) {
					if (rs.getInt(1) == 0)
						leaf = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(conn, pst, rs);
			}
		}

		return leaf;
	}

	public ChannelBean getChannelBean(Long channelId) {
		ChannelBean bean = null;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.channel_id,a.channel_pid,a.channel_name,a.channel_path,a.channel_index,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_channel a where a.channel_id = ? ";

		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			if (rs.next()) {
				bean = new ChannelBean(channelId, rs.getLong("channel_pid"), rs
						.getString("channel_name"), rs
						.getString("channel_path"), rs
						.getString("channel_index"), rs
						.getString("create_time"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}

		return bean;
	}

	public List<ChannelBean> getChannelList(Long channelId) {
		List<ChannelBean> list = new ArrayList<ChannelBean>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.channel_id,a.channel_pid,a.channel_name,a.channel_path,a.channel_index,to_char(a.create_time,'yy/mm/dd hh24:mi:ss') create_time from twap_public_channel a where a.channel_pid = ? and a.channel_id <> a.channel_pid order by a.channel_id asc";

		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add(new ChannelBean(rs.getLong("channel_id"), rs
						.getLong("channel_pid"), rs.getString("channel_name"),
						rs.getString("channel_path"), rs
								.getString("channel_index"), rs
								.getString("create_time")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		return list;
	}
	
	public Boolean save(ChannelBean bean) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_channel(channel_id,channel_pid,channel_name,channel_path,channel_index) values(seq_twap_public_channel.nextval,?,?,?,?) ";
		int count = 0;
		try {
			if (bean != null) {
				conn = OracleUtil.getConnection();
				if (bean.getChannelId() != null) {
					sql = " update twap_public_channel a set a.channel_pid = ?,a.channel_name = ?,a.channel_path = ?,a.channel_index = ? where a.channel_id = ? ";
					pst = conn.prepareStatement(sql);
					pst.setLong(1, bean.getChannelPid());
					pst.setString(2, bean.getChannelName());
					pst.setString(3, bean.getChannelPath());
					pst.setString(4, bean.getChannelIndex());
					pst.setLong(5, bean.getChannelId());
					count = pst.executeUpdate();
				} else {
					//添加节点时，需要更新节点关系
					pst = conn.prepareStatement(sql);
					pst.clearParameters();
					pst.setLong(1, bean.getChannelPid());
					pst.setString(2, bean.getChannelName());
					pst.setString(3, bean.getChannelPath());
					pst.setString(4, "1");//index的值暂时用来标识节点关系:0-非叶子节点;1-叶子节点
					count = pst.executeUpdate();
					if(bean.getChannelPid()!=null&&bean.getChannelPid()!=0L){
						conn.commit();
						pst = conn.prepareStatement("update twap_public_channel a set a.channel_index = ? where a.channel_id = ?");
						pst.setString(1, "0");
						pst.setLong(2, bean.getChannelPid());
						count = pst.executeUpdate();
					}
				}
				System.out.println(count);
				conn.commit();
				bln = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}
		return bln;
	}

	public Boolean delete(Long channelId) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String[] sqlScript = {"delete twap_app_log_webcrawler a where exists(select 1 from twap_public_channel b where a.log_channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)",
									"delete twap_public_channel_mapping a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)",
									"delete twap_public_channel_mapping_a a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)",
									"delete twap_public_crawl_list a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)",
									"delete twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)",
									"delete twap_public_channel a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id="+channelId+" connect by prior b.channel_id=b.channel_pid)"};
			bln = batchExecuteSQLSentence(sqlScript);
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}

		return bln;
	}
}
