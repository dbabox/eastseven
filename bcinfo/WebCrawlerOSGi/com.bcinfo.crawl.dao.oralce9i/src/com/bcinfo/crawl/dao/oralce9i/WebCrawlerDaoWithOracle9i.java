/**
 * 
 */
package com.bcinfo.crawl.dao.oralce9i;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.CLOB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Resource;

/**
 * @author dongq
 * 
 *         create time : 2010-6-17 上午09:59:44
 */
public class WebCrawlerDaoWithOracle9i implements WebCrawlerDao {

	private static final Log log = LogFactory.getLog(WebCrawlerDaoWithOracle9i.class);
	
	private final String selectSQL = "select res_text from twap_public_crawl_resource where res_id = ? for update";
	private final String insertSQL = "insert into twap_public_crawl_resource(channel_id,res_title,res_link,res_img_path_set,res_text,res_id,res_file_path_set,create_time) values(?,?,?,?,empty_clob(),?,?,sysdate)";
	private final String updateSQL = "update twap_public_crawl_resource set res_text = ? where res_id = ?";
	
	@Override
	public String getSystemDate(String pattern) {
		return null;
	}

	@Override
	public Boolean isExist(Resource resource) {
		return null;
	}

	@Override
	public Boolean save(Resource resource, Connection conn) {
		return null;
	}

	@Override
	public Boolean saveBatch(Resource[] resources, Connection conn) {
		boolean bln = false;

		try {
			if(conn == null) conn = DatabaseConnection.getConnection();
			conn.setAutoCommit(false);
			insert(resources, conn);
			update(resources, conn);
			conn.commit();
			conn.setAutoCommit(true);
			bln = true;
		} catch (Exception e) {
			log.error(e);
			if(conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return bln;
	}

	@Override
	public Boolean saveBatch(Object[] resources, Connection conn) {
		boolean bln = false;
		
		Resource[] resourceArray = new Resource[resources.length];
		int index = 0;
		for(Object object : resources) {
			resourceArray[index] = (Resource)object;
			index++;
		}
		
		bln = saveBatch(resourceArray, conn);
		
		return bln;
	}

	Long[] generateIds(Resource[] resources) throws Exception {
		Long[] ids = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "select seq_twap_public_crawl_resource.nextval from dual";
		conn = DatabaseConnection.getConnection();
		conn.setAutoCommit(false);
		ids = new Long[resources.length];
		for(int index = 0; index<ids.length; index++) {
			pst = conn.prepareStatement(sql);
			pst.clearBatch();
			pst.clearParameters();
			rs = pst.executeQuery();
			if(rs.next()) ids[index] = rs.getLong(1);
			if(rs != null) rs.close();
			if(pst != null) pst.close();
		}
		if(conn != null) conn.close();
		return ids;
	}
	
	void insert(Resource[] resources, Connection conn) throws Exception {
		PreparedStatement pst = null;
		pst = conn.prepareStatement(insertSQL);
		Long[] ids = generateIds(resources);
		int index = 0;
		for(Resource resource : resources) {
			Long id = ids[index];
			resource.setId(id);
			
			pst.setLong(1, resource.getChannel().getId());
			//TODO 关键字提示 <font style="color: #ff0000"></font>
			pst.setString(2, filterHandle(resource.getTitle()));
			pst.setString(3, resource.getLink());
			pst.setString(4, resource.getImgPath());
			pst.setLong(5, resource.getId());
			pst.setString(6, resource.getUserName());
			
			pst.addBatch();
			index++;
		}
		pst.executeBatch();
		pst.clearBatch();
		pst.clearParameters();
		pst.close();
		//conn.commit();
	}
	
	void update(Resource[] resources, Connection conn) throws Exception {
		for(Resource resource : resources) {
			PreparedStatement pst = null;
			ResultSet rs = null;
			String content = "";
			pst = conn.prepareStatement(selectSQL);
			pst.setLong(1, resource.getId());
			rs = pst.executeQuery();
			if(rs.next()) {
				//TODO 关键字提示 <font style="color: #ff0000"></font>
				content = resource.getContent();
				content = filterHandle(content);
				CLOB clob = (CLOB)rs.getClob("res_text");
				//Oracle 9i 写法
				//Writer w = clob.getCharacterOutputStream();
				//TODO Oracle 10g[10.2.0.1.0]
				Writer w = clob.setCharacterStream(0L);// getCharacterOutputStream() ;
		        //  Deprecated. This method is deprecated. Use setCharacterStream( 0L ).
				BufferedWriter writer = new BufferedWriter(w);
				BufferedReader reader = new BufferedReader(new StringReader(content));
				int len = 0;
				int off = 0;
				char[] cbuf = new char[1024];
				while((len = reader.read(cbuf))!=-1) writer.write(cbuf, off, len);
				writer.close();
				reader.close();
				pst = conn.prepareStatement(updateSQL);
				pst.setClob(1, clob);
				pst.setLong(2, resource.getId());
				pst.execute();
			}
			pst.clearParameters();
			if(rs != null) rs.close();
			if(pst != null) pst.close();
			
			conn.commit();
		}
	}
	
	List<String> getFilterKeyWordsList() {
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement("select key_value from wap_filter");
			rs = pst.executeQuery();
			while(rs.next()){
				String key = new String(rs.getString("key_value").getBytes("GBK"));
				list.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(rs, pst, conn);
		}
		return list;
	}
	
	String filterHandle(String content) {
		if(content == null || "".equals(content)) return content;
		try {
			String cnt = content;
			//cnt.split(RegexUtil.REGEX_IMG)
			List<String> list = getFilterKeyWordsList();
			if(list!=null&&!list.isEmpty()){
				for(String key : list){
					if(key.equals("xin")) continue;
					cnt = cnt.replace(key, "<font style=\"color: #ff0000\">"+key+"</font>");
				}
			}
			return cnt;
		} catch (Exception e) {
			e.printStackTrace();
			return content;
		}
	}
	
	void rollback(Connection conn){
		try {
			if(conn != null) conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
	
	void close(ResultSet rs, PreparedStatement pst, Connection conn){
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(e);
			}
		if(pst!=null)
			try {
				pst.close();
			} catch (SQLException e) {
				log.error(e);
			}
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
	}
}
