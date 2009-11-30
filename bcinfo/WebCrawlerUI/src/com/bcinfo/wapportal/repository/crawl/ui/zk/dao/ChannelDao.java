package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-11-24 ÏÂÎç02:23:14
 */
public class ChannelDao extends Dao {

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
					pst = conn.prepareStatement(sql);
					pst.setLong(1, bean.getChannelPid());
					pst.setString(2, bean.getChannelName());
					pst.setString(3, bean.getChannelPath());
					pst.setString(4, bean.getChannelIndex());
					count = pst.executeUpdate();
				}
				conn.commit();
				if (count > 0)
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

	public Boolean delete(List<Long> list) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			if (list != null && !list.isEmpty()) {
				conn = OracleUtil.getConnection();
				for(Long id : list){
					//twap_public_crawl_resource
					pst = conn.prepareStatement("delete twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid)");
					pst.setLong(1, id);
					pst.executeUpdate();
					//twap_public_crawl_list
					pst = conn.prepareStatement("delete twap_public_crawl_list a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid)");
					pst.setLong(1, id);
					pst.executeUpdate();
					//twap_public_channel_mapping_a
					pst = conn.prepareStatement("delete twap_public_channel_mapping_a a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid)");
					pst.setLong(1, id);
					pst.executeUpdate();
					//twap_public_channel_mapping
					pst = conn.prepareStatement("delete twap_public_channel_mapping a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid)");
					pst.setLong(1, id);
					pst.executeUpdate();
					//twap_public_channel
					pst = conn.prepareStatement("delete twap_public_channel a start with a.channel_id = ? connect by prior a.channel_id=a.channel_pid");
					pst.setLong(1, id);
					pst.executeUpdate();
				}
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
}
