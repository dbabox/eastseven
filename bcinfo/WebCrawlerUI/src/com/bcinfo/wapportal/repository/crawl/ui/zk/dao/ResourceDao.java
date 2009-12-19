package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.CLOB;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ResourceBean;

/**
 * 
 * @author dongq
 * 
 * create time : 2009-11-24 下午02:24:15
 */
public class ResourceDao extends Dao {

	public Boolean delete(List<String> list) {
		Boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " delete twap_public_crawl_resource a where a.res_id = ? ";

		try {
			if(list != null && !list.isEmpty()){
				conn = OracleUtil.getConnection();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				for(String resId : list){
					pst.setLong(1, Long.parseLong(resId));
					pst.addBatch();
				}
				pst.executeBatch();
				conn.setAutoCommit(true);
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
	
	public Boolean modifyResourceContentOrTitle(Long resId, String title, String content) {
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_resource a set a.res_title = ?,a.res_text = ?,a.res_status = '0' where a.res_id = ? ";
		String version = null;
		
		try{
			conn = OracleUtil.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			version = metaData.getDatabaseProductVersion();
			System.out.println(" oracle "+version+" update clob ");
			if(version != null && version.contains("10g")){
				//Oracle 10g 可以直接更新LOB字段的值
				pst = conn.prepareStatement(sql);
				pst.setString(1, title);
				pst.setString(2, content);
				pst.setLong(3, resId);
				pst.executeUpdate();
			}else{
				pst = conn.prepareStatement(" update twap_public_crawl_resource a set a.res_title = ?,a.res_text = EMPTY_CLOB() where a.res_id = ? ");
				pst.setString(1, title);
				pst.setLong(2, resId);
				pst.executeUpdate();
				conn.setAutoCommit(false);
				//Oracle 9i 必须通过流来更新LOB字段的值
				pst = conn.prepareStatement("select res_text from twap_public_crawl_resource where res_id = ? for update");
				pst.setLong(1, resId);
				rs = pst.executeQuery();
				if(rs.next()){
					CLOB clob = (CLOB)rs.getClob("res_text");
					//Oracle 9i 写法
					Writer w = clob.getCharacterOutputStream();
					BufferedWriter writer = new BufferedWriter(w);
					BufferedReader reader = new BufferedReader(new StringReader(content));
					int len = 0;
					int off = 0;
					char[] cbuf = new char[1024];
					while((len = reader.read(cbuf))!=-1) writer.write(cbuf, off, len);
					writer.close();
					reader.close();
					pst = conn.prepareStatement(sql);
					pst.setString(1, title);
					pst.setClob(2, clob);
					pst.setLong(3, resId);
					pst.executeUpdate();
					conn.setAutoCommit(true);
				}
			}
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean updateResource(Long resId) {
		Boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_resource a set a.res_status = '1' where a.res_id = ? ";

		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, resId);
			int i = pst.executeUpdate();
			conn.commit();
			if (i > 0)
				bln = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			close(conn, pst, rs);
		}

		return bln;
	}

	public Boolean updateResource(List<Long> list) {
		Boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update twap_public_crawl_resource a set a.res_status = '1' where a.res_id = ? ";

		try {
			if(list != null && !list.isEmpty()){
				conn = OracleUtil.getConnection();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				for(Long resId : list){
					pst.setLong(1, resId);
					pst.addBatch();
				}
				pst.executeBatch();
				conn.setAutoCommit(true);
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

	public List<ResourceBean> getResourceList(Long channelId, String status, String title, String date, String dateEnd) {
		System.out.println("getResourceList(Long "+channelId+", String "+status+", String "+title+", String "+date+", String "+dateEnd+")");
		List<ResourceBean> list = new ArrayList<ResourceBean>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		String condition = " and 1 = 1 ";
		if(title != null && !"".equals(title)){
			condition = " and a.res_title like '"+title+"%' ";
		}
		//TODO Orcale9i与10g在使用connect by时9i不能使用nocycle关键字，估计是9i还没有该关键字的缘故
		String sql = " select a.res_id,a.channel_id,(select c.channel_name from twap_public_channel c where a.channel_id=c.channel_id) channel_name,a.res_title,a.res_link,a.res_content,a.res_img_path_set,a.res_status,to_char(a.create_time,'yy/mm/dd hh24:mi:ss') create_time from twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid) and a.res_status = ? and (to_char(a.create_time,'yyyy-mm-dd') <= ? and to_char(a.create_time,'yyyy-mm-dd') >= ?) "+condition+" order by a.create_time desc,a.res_id desc ";

		try {
			System.out.println(sql);
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.setString(2, status);
			pst.setString(4, date);
			pst.setString(3, dateEnd);
			rs = pst.executeQuery();
			ResourceBean bean = null;
			while (rs.next()) {
				bean = new ResourceBean();

				bean.setChannelId(rs.getLong("channel_id"));
				bean.setChannelName(rs.getString("channel_name"));
				bean.setContent(rs.getString("res_content"));
				bean.setCreateTime(rs.getString("create_time"));
				bean.setLink(rs.getString("res_link"));
				bean.setResId(rs.getLong("res_id"));
				bean.setStatus(rs.getString("res_status"));
				bean.setTitle(rs.getString("res_title"));
				bean.setImgPathSet(rs.getString("res_img_path_set"));

				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		return list;
	}

	public ResourceBean getResource(Long crawlId){
		ResourceBean bean = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.res_id,a.channel_id,a.res_title,a.res_link,a.res_content,a.res_img_path_set,a.res_file_path_set,a.res_text,a.res_status,to_char(a.create_time,'yy/mm/dd hh24:mi:ss') create_time from twap_public_crawl_resource a where a.res_id=?";
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, crawlId);
			rs = pst.executeQuery();
			if(rs.next()){
				bean = new ResourceBean();
				Clob clob = rs.getClob("res_text");
				Reader inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				// data是读出并需要返回的数据，类型是String
				String data = new String(new String(c).getBytes(), "GBK");

				inStream.close();
				
				bean.setChannelId(rs.getLong("channel_id"));
				//bean.setChannelName(rs.getString("channel_name"));
				bean.setContent(data);
				bean.setCreateTime(rs.getString("create_time"));
				bean.setLink(rs.getString("res_link"));
				bean.setResId(rs.getLong("res_id"));
				bean.setStatus(rs.getString("res_status"));
				bean.setTitle(rs.getString("res_title"));
				bean.setImgPathSet(rs.getString("res_img_path_set"));
				
				bean.setPics((rs.getString("res_img_path_set")!=null && !"".equals(rs.getString("res_img_path_set"))?String.valueOf(rs.getString("res_img_path_set").split(",").length):"0"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		return bean;
	}
	
	public List<ResourceBean> getResourceList(Long channelId, String status, String title, String date, String dateEnd, int start, int end) {
		System.out.println("getResourceList(Long "+channelId+", String "+status+", String "+title+", String "+date+", int "+start+", int "+end+")");
		List<ResourceBean> list = new ArrayList<ResourceBean>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String condition = " and 1 = 1 ";
		if(title != null && !"".equals(title)){
			condition = " and a.res_title like '"+title+"%' ";
		}
		String sql = " select rownum row_num,a.res_id,a.channel_id,(select c.channel_name from twap_public_channel c where a.channel_id=c.channel_id) channel_name,a.res_title,a.res_link,a.res_content,a.res_text,a.res_img_path_set,decode(a.res_status,'0','未审','1','已审') res_status,to_char(a.create_time,'yy/mm/dd hh24:mi:ss') create_time from twap_public_crawl_resource a where exists(select 1 from twap_public_channel b where a.channel_id = b.channel_id start with b.channel_id = ? connect by prior b.channel_id = b.channel_pid) and a.res_status = ? and( to_char(a.create_time,'yyyy-mm-dd') <= ? and to_char(a.create_time,'yyyy-mm-dd')>=?)"+condition+" and rownum <= ? order by a.create_time desc,a.res_id desc ";
		sql = " select * from ( select * from ( " + sql
				+ " ) b ) c where c.row_num >= ? order by c.create_time desc,c.res_id desc";
		try {
			//System.out.println(sql);
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.setString(2, status);
			pst.setString(4, date);
			pst.setString(3, dateEnd);
			pst.setInt(5, end);
			pst.setInt(6, start);
			rs = pst.executeQuery();
			ResourceBean bean = null;
			Reader inStream = null;
			Clob clob = null;
			while (rs.next()) {
				bean = new ResourceBean();
				clob = rs.getClob("res_text");
				inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				// data是读出并需要返回的数据，类型是String
				String data = new String(new String(c).getBytes(), "GBK");

				inStream.close();

				bean.setChannelId(rs.getLong("channel_id"));
				bean.setChannelName(rs.getString("channel_name"));
				bean.setContent(data);
				bean.setCreateTime(rs.getString("create_time"));
				bean.setLink(rs.getString("res_link"));
				bean.setResId(rs.getLong("res_id"));
				bean.setStatus(rs.getString("res_status"));
				bean.setTitle(rs.getString("res_title"));
				bean.setImgPathSet(rs.getString("res_img_path_set"));
				
				bean.setPics((rs.getString("res_img_path_set")!=null && !"".equals(rs.getString("res_img_path_set"))?String.valueOf(rs.getString("res_img_path_set").split(",").length):"0"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		return list;
	}
}
