package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.SubscribeBean;


/**
 * @author dongq
 * 
 *         create time : 2009-11-24 上午10:44:52
 */
public class SubscribeDao extends Dao {

	public List<SubscribeBean> getAutoSubscribeList(Long userId, Long channelId){
		List<SubscribeBean> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.mapping_id,a.channel_id,(select t.channel_name from twap_public_channel t where t.channel_id = a.channel_id) channel_name,decode(a.local_code,'028','四川','0791','江西','未知') local_code,a.local_channel_id,a.user_id,to_char(a.create_time,'yyyy-mm-dd') create_time,decode(a.operation,1,'写入',2,'更新') operation from twap_public_channel_mapping_a a where a.user_id = ? and exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid ) ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, userId);
			pst.setLong(2, channelId);
			rs = pst.executeQuery();
			list = new ArrayList<SubscribeBean>();
			SubscribeBean bean = null;
			while(rs.next()){
				bean = new SubscribeBean();
				bean.setChannelId(rs.getLong("channel_id"));
				bean.setChannelName(rs.getString("channel_name"));
				bean.setCreateTime(rs.getString("create_time"));
				bean.setLocalCode(rs.getString("local_code"));
				bean.setLocalFolderId(rs.getString("local_channel_id"));
				bean.setMappingId(rs.getLong("mapping_id"));
				bean.setUserId(rs.getLong("user_id"));
				bean.setOperation(rs.getString("operation"));
				list.add(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<SubscribeBean> getSubscribeList(Long userId, Long channelId){
		List<SubscribeBean> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.mapping_id,a.channel_id,(select t.channel_name from twap_public_channel t where t.channel_id = a.channel_id) channel_name,decode(a.local_code,'028','四川','0791','江西','未知') local_code,a.local_channel_id,a.user_id,to_char(a.create_time,'yyyy-mm-dd') create_time from twap_public_channel_mapping a where a.user_id = ? and exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid ) ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, userId);
			pst.setLong(2, channelId);
			rs = pst.executeQuery();
			list = new ArrayList<SubscribeBean>();
			SubscribeBean bean = null;
			while(rs.next()){
				bean = new SubscribeBean();
				bean.setChannelId(rs.getLong("channel_id"));
				bean.setChannelName(rs.getString("channel_name"));
				bean.setCreateTime(rs.getString("create_time"));
				bean.setLocalCode(rs.getString("local_code"));
				bean.setLocalFolderId(rs.getString("local_channel_id"));
				bean.setMappingId(rs.getLong("mapping_id"));
				bean.setUserId(rs.getLong("user_id"));
				
				list.add(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public Boolean saveAutoSubscribeBean(SubscribeBean bean){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_channel_mapping_a(mapping_id,channel_id,local_code,local_channel_id,user_id,operation) values(seq_twap_public_chl_mapping.nextval,?,?,?,?,?) ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, bean.getChannelId());
			pst.setString(2, bean.getLocalCode());
			pst.setString(3, bean.getLocalFolderId());
			pst.setLong(4, bean.getUserId());
			pst.setLong(5, Long.parseLong(bean.getOperation()));
			if(pst.executeUpdate()>0){
				conn.commit();
				bln = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean save(SubscribeBean bean){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_channel_mapping(mapping_id,channel_id,local_code,local_channel_id,user_id) values(seq_twap_public_chl_mapping.nextval,?,?,?,?) ";
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, bean.getChannelId());
			pst.setString(2, bean.getLocalCode());
			pst.setString(3, bean.getLocalFolderId());
			pst.setLong(4, bean.getUserId());
			if(pst.executeUpdate()>0){
				conn.commit();
				bln = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean deleteSubscribeBeans(List<SubscribeBean> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete from twap_public_channel_mapping where channel_id = ? and user_id = ? and local_code = ? and local_channel_id = ? ";
		
		try {
			conn = OracleUtil.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for(SubscribeBean bean : list){
				pst.setLong(1, bean.getChannelId());
				pst.setLong(2, bean.getUserId());
				
				if("四川".equals(bean.getLocalCode())){
					pst.setString(3, "028");
				}else if("江西".equals(bean.getLocalCode())){
					pst.setString(3, "0791");
				}
				
				pst.setString(4, bean.getLocalFolderId());
				pst.addBatch();
				System.out.println(sql+"["+bean+"]");
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
	
	public Boolean delete(List<Long> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete from twap_public_channel_mapping where mapping_id = ? ";
		
		try{
			if(list != null && !list.isEmpty()){
				conn = OracleUtil.getConnection();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				if(list.size() > 1){
					for(Long id : list){
						pst.setLong(1, id);
						pst.addBatch();
					}
					pst.executeBatch();
				}else{
					pst.setLong(1, list.get(0));
					pst.executeUpdate();
				}
				conn.setAutoCommit(true);
				conn.commit();
				bln = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean deleteAuto(List<Long> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete from twap_public_channel_mapping_a where mapping_id = ? ";
		
		try{
			if(list != null && !list.isEmpty()){
				conn = OracleUtil.getConnection();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				if(list.size() > 1){
					for(Long id : list){
						pst.setLong(1, id);
						pst.addBatch();
					}
					pst.executeBatch();
				}else{
					pst.setLong(1, list.get(0));
					pst.executeUpdate();
				}
				conn.setAutoCommit(true);
				conn.commit();
				bln = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
}
