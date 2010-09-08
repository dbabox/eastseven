/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.CrawlResource;

/**
 * @author dongq
 * 
 *         create time : 2009-10-18 下午04:18:05
 */
public class CrawlResourceDao {

	public void saveResources(List<CrawlResource> resources, String userName) {
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into TWAP_PUBLIC_CRAWL_RESOURCE_STA(RES_ID,CHANNEL_ID,RES_TITLE,RES_LINK,RES_CONTENT) " +
				"values(SEQ_TWAP_PUBLIC_CRAWL_RESOURCE.Nextval,?,?,?,?) ";
		try {
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for(CrawlResource resource : resources){
				pst.setLong(1, resource.getChannelId());
				pst.setString(2, resource.getTitle());
				pst.setString(3, resource.getLink());
				pst.setString(4, userName);
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			close(conn, pst, rs);
		}
	}
	
	public Boolean updateCrawlResourceStatus(String[] resIds){
		Boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_resource a set a.res_status = '1' where a.res_id = ? ";
		
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for(int i=0;i<resIds.length;i++){
				pst.setLong(1, Long.parseLong(resIds[i]));
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			bln = true;
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
		return bln;
	}
	
	public CrawlResource getCrawlResourceDetail(Long resId){
		CrawlResource resource = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.res_id,a.channel_id,a.res_title,a.res_link,a.res_content,a.res_text,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time,a.res_status,a.res_img_path_set,a.res_file_path_set from twap_public_crawl_resource a where a.res_id = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, resId);
			rs = pst.executeQuery();
			if(rs.next()){
				
				Clob clob = rs.getClob("res_text");
				Reader inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				//data是读出并需要返回的数据，类型是String
				String data = new String(new String(c).getBytes(),"GBK");
				
				inStream.close();
				
				resource = new CrawlResource();
				resource.setChannelId(rs.getLong("channel_id"));
				resource.setContent(data);
				resource.setCreateTime(rs.getString("create_time"));
				resource.setLink(rs.getString("res_link"));
				resource.setResId(rs.getLong("res_id"));
				resource.setStatus(rs.getString("res_status"));
				resource.setTitle(rs.getString("res_title"));
				resource.setImgPathSet(rs.getString("res_img_path_set"));
				resource.setFilePathSet(rs.getString("res_file_path_set"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return resource;
	}
	
	public int getCount(Long channelId){
		int count = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(a.res_id) from twap_public_crawl_resource a where a.channel_id = ? order by a.res_id desc ";
		try{
			conn = JavaOracle.getConn();
			pst =conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			
			rs = pst.executeQuery();
			if(rs.next()) count = rs.getInt(1);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return count;
	}
	
	public int getCount(Long channelId, String title, String status){
		int count = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		try{
			conn = JavaOracle.getConn();
			if(channelId == null) channelId = 0L;
			title = (title==null || "".equals(title)) ? " 1=1 " : " a.res_title like '%" + title + "%' ";
			if("-1".equals(status)){
				status = " 1=1 ";
			}else{
				status = " a.res_status = " + status;
			}
			sql = " select count(a.res_id) from twap_public_crawl_resource a where exists(select 1 from twap_public_channel t where a.channel_id = t.channel_id start with t.channel_id = ? connect by prior t.channel_id = t.channel_pid) and "+title+" and "+status+" ";
			System.out.println(sql);
			pst =conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			
			rs = pst.executeQuery();
			if(rs.next()) count = rs.getInt(1);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return count;
	}
	
	/**
	 * 
	 * @return List<Map<String, String>> [resId,localCode,localChannelId,operation]
	 */
	public List<Map<String, String>> getAutoSendCrawlResources(){
		List<Map<String, String>> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.res_id,b.local_code,b.local_channel_id,b.operation from twap_public_crawl_resource a,twap_public_channel_mapping_a b where a.channel_id = b.channel_id  ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			list = new ArrayList<Map<String,String>>();
			Map<String, String> map = null;
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("resId", String.valueOf(rs.getLong("res_id")));
				map.put("localCode", rs.getString("local_code"));
				map.put("localChannelId", rs.getString("local_channel_id"));
				map.put("operation", String.valueOf(rs.getInt("operation")));
				list.add(map);
			}
			 
		}catch(Exception e){
			e.printStackTrace();
			Log.error(e);
		}finally{
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<CrawlResource> getAllCrawlResources(Long channelId, String title, String status, int start, int end){
		List<CrawlResource> list = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		try{
			conn = JavaOracle.getConn();
			if(channelId != null){
				title = (title==null||"".equals(title))?" 1=1 ":" a.res_title like '%"+title+"%' ";
				if("-1".equals(status)){
					status = " 1=1 ";
				}else{
					status = " a.res_status = " + status;
				}
				sql = " select * from (select * from (select a.res_id,a.channel_id,a.res_title,a.res_link,to_char(a.create_time,'yy/mm/dd/hh24:mm:ss') create_time,decode(a.res_status,'0','未审','1','已审') res_status, dbms_lob.getlength(a.res_text) res_text_len, nvl(a.res_img_path_set,'') res_img_path_set, rownum row_num from twap_public_crawl_resource a where exists(select 1 from twap_public_channel t where a.channel_id = t.channel_id start with t.channel_id = ? connect by prior t.channel_id = t.channel_pid) and "+title+" and "+status+" and rownum <= ? order by a.res_id desc) b) c where c.row_num >= ? order by c.res_id desc ";
				
				System.out.println(sql);
				
				pst = conn.prepareStatement(sql);
				pst.setLong(1, channelId);
				pst.setInt(2, end);
				pst.setInt(3, start);
				
			}else{
				sql = " select a.res_id,a.channel_id,a.res_title,a.res_link,to_char(a.create_time,'yy/mm/dd/hh24:mm:ss') create_time from twap_public_crawl_resource a order by a.res_id desc ";
				pst = conn.prepareStatement(sql);
			}
			
			rs = pst.executeQuery();
			list = new ArrayList<CrawlResource>();
			CrawlResource resource = null;
			while(rs.next()){
				resource = new CrawlResource();
				resource.setChannelId(rs.getLong("channel_id"));
				resource.setCreateTime(rs.getString("create_time"));
				resource.setLink(rs.getString("res_link"));
				resource.setResId(rs.getLong("res_id"));
				resource.setStatus(rs.getString("res_status"));
				resource.setTitle(rs.getString("res_title"));
				//TODO 临时处理
				resource.setText(rs.getObject("res_text_len").toString());
				
				String picStr = rs.getString("res_img_path_set");
				if("".equals(picStr)||picStr==null){
					resource.setPics("0");
				}else{
					resource.setPics(String.valueOf(picStr.split(",").length));
				}
				
				list.add(resource);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return list;
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
