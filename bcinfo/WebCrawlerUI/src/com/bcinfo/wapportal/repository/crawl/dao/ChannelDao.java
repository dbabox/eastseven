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
import com.bcinfo.wapportal.repository.crawl.domain.Channel;

/**
 * @author dongq
 * 
 *         create time : 2009-10-18 ÏÂÎç04:30:59
 */
public class ChannelDao {

	public Boolean saveChannel(Long channelPid, String channelName){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_channel(channel_id,channel_pid,channel_name) values(seq_twap_public_channel.nextval,?,?) ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelPid);
			pst.setString(2, channelName);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
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
	
	public List<Channel> getChannels(){
List<Channel> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
			conn = JavaOracle.getConn();
			sql = " select a.channel_id,a.channel_pid,concat(to_char(replace(rpad(level,level,level),level,'--')),a.channel_name) channel_name,a.channel_path,a.channel_index,to_char(a.create_time,'yyyy-mm-dd hh24:mm:ss') create_time from twap_public_channel a start with a.channel_id = 0 connect by nocycle prior a.channel_id = a.channel_pid ";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			
			rs = pst.executeQuery();
			list = new ArrayList<Channel>();
			Channel channel = null;
			while(rs.next()){
				channel = new Channel();
				channel.setChannelId(rs.getLong("channel_id"));
				channel.setChannelIndex(rs.getString("channel_index"));
				channel.setChannelName(rs.getString("channel_name"));
				channel.setChannelPath(rs.getString("channel_path"));
				channel.setChannelPid(rs.getLong("channel_pid"));
				channel.setCreateTime(rs.getString("create_time"));
				list.add(channel);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return list;
	}
	
	public List<Channel> getChannels(Long channelId){
		List<Channel> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
			conn = JavaOracle.getConn();
			if(channelId != null){
				sql = " select a.channel_id,a.channel_pid,a.channel_name,a.channel_path,a.channel_index,to_char(a.create_time,'yyyy-mm-dd hh24:mm:ss') create_time from twap_public_channel a where a.channel_pid = ? order by a.channel_id ";
				pst = conn.prepareStatement(sql);
				pst.setLong(1, channelId);
			}else{
				sql = " select a.channel_id,a.channel_pid,a.channel_name,a.channel_path,a.channel_index,to_char(a.create_time,'yyyy-mm-dd hh24:mm:ss') create_time from twap_public_channel a order by a.channel_pid,a.channel_id ";
				pst = conn.prepareStatement(sql);
			}
			
			rs = pst.executeQuery();
			list = new ArrayList<Channel>();
			Channel channel = null;
			while(rs.next()){
				channel = new Channel();
				channel.setChannelId(rs.getLong("channel_id"));
				channel.setChannelIndex(rs.getString("channel_index"));
				channel.setChannelName(rs.getString("channel_name"));
				channel.setChannelPath(rs.getString("channel_path"));
				channel.setChannelPid(rs.getLong("channel_pid"));
				channel.setCreateTime(rs.getString("create_time"));
				list.add(channel);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return list;
	}
	
	public String getChannelTreeForDTree(List<Channel> list){
		String tree = null;
		
		try{
			if(list != null && !list.isEmpty()){
				tree = "var d = new dTree('d');" +"\n";
				//tree += "d.add(0,-1,'ÆµµÀ','channel_tree.jsp','ÆµµÀ','');"+"\n";
				String node = "";
				for(Channel channel : list){
					/*
					 *  id	Number	Unique identity number.
						pid	Number	Number refering to the parent node. The value for the root node has to be -1.
						name	String	Text label for the node.
						url	String	Url for the node.
						title	String	Title for the node.
						target	String	Target for the node.
						icon	String	Image file to use as the icon. Uses default if not specified.
						iconOpen	String	Image file to use as the open icon. Uses default if not specified.
						open	Boolean	Is the node open.
						
						Example
						
						mytree.add(1, 0, 'My node', 'node.html', 'node title', 'mainframe', 'img/musicfolder.gif');
					 * */
					if(channel.getChannelId() == 0){
						node = "d.add("+channel.getChannelId()+",-1,'"+channel.getChannelName()+"','channel_tree.jsp','"+channel.getChannelName()+"','');";
					}else{
						//./crawl_resource_list.jsp?method=init
						//./CrawlResourceServlet?method=list
						node = "d.add("+channel.getChannelId()+","+channel.getChannelPid()+",'"+channel.getChannelName()+"','./CrawlResourceServlet?method=list&channelId="+channel.getChannelId()+"','"+channel.getChannelName()+"','mainFrame');";
					}
					
					if(!"".equals(node))
						tree += node +"\n";
				}
				tree += " document.write(d);"+"\n";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return tree;
	}
	
	public String getChannelTree(List<Channel> list){
		String tree = null;
		
		try{
			if(list != null && !list.isEmpty()){
				tree = "";
				for(Channel channel : list){
					//tree.nodes["406_1127"] = "text:toString; url:Article.asp; data:id=127";
					String node = "tree.nodes[\""+(channel.getChannelPid()+1)+"_"+(channel.getChannelId()+1)+"\"] = ";
					node += "\"text:"+channel.getChannelName()+" ("+this.getCount(channel.getChannelId())+")"+"; url:./crawl_resource_list.jsp?method=init; ";
					node += " target:mainFrame; ";
					node += "data:channelId="+channel.getChannelId()+"\";" +"\n";
					
					tree += node;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return tree;
	}
	
	public String getChannelTree(){
		String tree = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select c.*,(select count(a.res_id) from twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id = c.channel_id connect by prior b.channel_id = b.channel_pid)) res_num from twap_public_channel c";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			tree = "";
			while(rs.next()){
				String node = "tree.nodes[\""+(rs.getLong("channel_pid")+1)+"_"+(rs.getLong("channel_id")+1)+"\"] = ";
				node += "\"text:"+rs.getString("channel_name")+" ("+rs.getLong("res_num")+")"+"; url:./crawl_resource_list.jsp?method=init; ";
				node += " target:mainFrame; ";
				node += "data:channelId="+rs.getLong("channel_id")+"\";" +"\n";
				
				tree += node;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return tree;
	}
	
	private int getCount(Long id){
		int count = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(a.res_id) from twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id=b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid) ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, id);
			rs =pst.executeQuery();
			if(rs.next()) count = rs.getInt(1);
			
		}catch(Exception e){
			e.printStackTrace();
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}finally{
			close(conn, pst, rs);
		}
		return count;
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
