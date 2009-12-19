/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.domain.Channel;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.Dao;
import common.Logger;

/**
 * @author dongq
 * 
 *         create time : 2009-12-11 ÉÏÎç10:02:58
 */
public class AppLogDao extends Dao {

	private static final Logger log = Logger.getLogger(AppLogDao.class);
	
	public Map<String, String> getCatchSizeList(Long channelId) {
		Map<String, String> map = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			List<Channel> list = new ChannelDao().getChannels(channelId);
			if(list!=null&&!list.isEmpty()){
				conn = OracleUtil.getConnection();
				map = new HashMap<String, String>();
				String sql = "select nvl(sum(a.log_catch_count),0) from twap_app_log_webcrawler a where exists(select 1 from twap_public_channel b where a.log_channel_id = b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid) and (to_char(a.create_time,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') )";
				for(Channel channel : list){					
					pst = conn.prepareStatement(sql);
					pst.setLong(1, channel.getChannelId());
					log.debug(sql.replaceAll("\\?", channel.getChannelId().toString()));
					rs = pst.executeQuery();
					if(rs.next()){
						map.put(channel.getChannelName(), String.valueOf(rs.getLong(1)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return map;
	}
	
	public Long getCatchSize(Long channelId, String startDate, String endDate) {
		Long size = 0L;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select nvl(sum(a.log_catch_count),0) from twap_app_log_webcrawler a where exists(select 1 from twap_public_channel b where a.log_channel_id = b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid) and (to_char(a.create_time,'yyyy-mm-dd')<=? and to_char(a.create_time,'yyyy-mm-dd')>=?)");
			pst.setLong(1, channelId);
			pst.setString(3, startDate);
			pst.setString(2, endDate);
			rs = pst.executeQuery();
			if(rs.next()) size = rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(conn, pst, rs);
		}
		
		return size;
	}
	
	public List<AppLog> getAppLogList(Long channelId, String startDate, String endDate) {
		List<AppLog> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select rownum row_num,a.log_message,(select channel_name from twap_public_channel t where t.channel_id = a.log_channel_id) channel_name,a.log_channel_id,a.log_url,a.log_catch_count,to_char(a.create_time,'yy/mm/dd hh24:mi:ss') create_time  from twap_app_log_webcrawler a where exists(select 1 from twap_public_channel b where a.log_channel_id = b.channel_id start with b.channel_id=? connect by prior b.channel_id=b.channel_pid) and (to_char(a.create_time,'yyyy-mm-dd')<=? and to_char(a.create_time,'yyyy-mm-dd')>=?) order by a.create_time desc");
			pst.setLong(1, channelId);
			pst.setString(3, startDate);
			pst.setString(2, endDate);
			rs = pst.executeQuery();
			list = new ArrayList<AppLog>();
			while(rs.next()){
				AppLog appLog = new AppLog(rs.getString("log_message"), rs.getLong("log_channel_id"), rs.getString("log_url"), rs.getLong("log_catch_count"));
				appLog.setChannelName(rs.getString("channel_name"));
				appLog.setCreateTime(rs.getString("create_time"));
				appLog.setLogId(rs.getLong("row_num"));
				list.add(appLog);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
}
