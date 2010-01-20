/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceType;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;
import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-15 下午01:42:55
 */
public class DaoServiceDefaultImpl implements DaoService {

	private static Logger log = Logger.getLogger(DaoServiceDefaultImpl.class);
	
	private String databaseProductVersion;
	
	public DaoServiceDefaultImpl() {
	}
	
	@Override
	public String getChannelName(Long channelId) {
		String name = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select a.channel_name from twap_public_channel a where a.channel_id = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			rs = pst.executeQuery();
			if(rs.next()) name = rs.getString("channel_name");
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}finally{
			close(rs, pst, conn);
		}
		
		return name;
	}
	
	@Override
	public List<CrawlList> getCrawlLists(String status) {
		List<CrawlList> list = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.crawl_id,a.channel_id,a.crawl_url,a.crawl_status,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_crawl_list a where a.crawl_status = ? order by a.channel_id,a.create_time,a.crawl_id";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, status);
			rs = pst.executeQuery();
			list = new ArrayList<CrawlList>();
			while(rs.next()){
				CrawlList crawlList = new CrawlList();
				crawlList.setCrawlId(rs.getLong("crawl_id"));
				crawlList.setChannelId(rs.getLong("channel_id"));
				crawlList.setCrawlUrl(rs.getString("crawl_url"));
				crawlList.setCrawlStatus(rs.getString("crawl_status"));
				crawlList.setCreateTime(rs.getString("create_time"));
				list.add(crawlList);
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			close(rs, pst, conn);
		}
		return list;
	}
	
	@Override
	public List<CrawlList> getCrawlLists(Long folderId) {
		
		List<CrawlList> list = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		//String sql = "select crawl_id,folder_id,url,status,to_char(create_time,'yyyy-mm-dd hh24:mi:ss') from twap_public_crawl_list where folder_id = ? and status = '1' order by crawl_id asc";
		try{
			
		}catch(Exception e){
			log.error(e);
		}finally{
			close(rs, pst, conn);
		}
		return list;
	}
	
	@Override
	public Boolean isExistCrawlResource(Long channelId, String title) {
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(1) from twap_public_crawl_resource a where a.channel_id = ? and a.res_title = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.setString(2, title);
			rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)>0) bln = true;
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public Boolean saveCrawlResource(List<FolderBO> folders) {
		Boolean bln = false;
		if(folders==null) return bln;
		Connection conn = null;
		PreparedStatement pst = null;
		//PreparedStatement pstUpdate = null;
		ResultSet rs = null;//res_content,RES_IMG_PATH_SET
		String sql = "insert into twap_public_crawl_resource(channel_id,res_title,res_link,res_img_path_set,res_file_path_set,res_id,res_text) values(?,?,?,?,?,empty_clob())";
		
		String link = "";
		String title = "";
		String channelId = "";
		String content = "";
		String imgPathSet="";
		String filePathSet="";
		try{
			/**/
			conn = JavaOracle.getConn();
			DatabaseMetaData metaData = conn.getMetaData();
			databaseProductVersion = metaData.getDatabaseProductVersion();
			
			if(databaseProductVersion.contains("10g")){
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				pst.clearBatch();
				int count = 0;
				List<FolderBO> list = new ArrayList<FolderBO>();
				List<Long> ids = getResourceId(folders);
				for(FolderBO folder : folders){
					link = folder.getLink();
					title = folder.getTitle();
					//TODO 关键字提示 <font style="color: #ff0000"></font>
					title = filterHandle(title);
					channelId = folder.getFolderId();
					//TODO 关键字提示 <font style="color: #ff0000"></font>
					content = filterHandle(folder.getContent());
					imgPathSet = folder.getImgPathSet();
					filePathSet = folder.getFilePathSet();
					pst.setLong(1, Long.valueOf(channelId));
					pst.setString(2, title);
					pst.setString(3, link);
					pst.setString(4, imgPathSet);
					pst.setString(5, filePathSet);
					pst.setLong(6, ids.get(count));
					pst.addBatch();
					
					folder.setId(ids.get(count));
					list.add(folder);
					
					count++;
				}
				
				pst.executeBatch();
				
				sql = "update twap_public_crawl_resource set res_text = ? where res_id = ?";
				pst = conn.prepareStatement(sql);
				pst.clearBatch();
				for(FolderBO folder : list){
					pst.setString(1, folder.getContent());
					pst.setLong(2, folder.getId());
					pst.addBatch();
				}
				pst.executeBatch();
				
				conn.commit();
				conn.setAutoCommit(true);
				bln = true;
			}else{
				//oracle 9i 使用方法
				List<FolderBO> list = insertBatch(folders);
				if(list!=null && !list.isEmpty()){
					for(FolderBO folder : list){
						boolean flag = update(folder);
					}
					bln = true;
				}
			}
		}catch(Exception e){
			log.error(e);
			log.info(link+" | "+title+" | 保存失败");
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}finally{
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	List<Long> getResourceId(List<FolderBO> list){
		List<Long> ids = null;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			conn = JavaOracle.getConn();
			if(list!=null && !list.isEmpty()){
				ids = new ArrayList<Long>();
				for(@SuppressWarnings("unused") FolderBO folder : list){
					pst = conn.prepareStatement("select seq_twap_public_crawl_resource.nextval from dual");
					rs = pst.executeQuery();
					if(rs.next()) ids.add(rs.getLong(1));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(rs, pst, conn);
		}
		return ids;
	}
	
	List<FolderBO> insertBatch(List<FolderBO> list){
		List<FolderBO> folders = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			List<Long> ids = getResourceId(list);
			if(ids !=null && !ids.isEmpty()){
				conn = JavaOracle.getConn();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement("insert into twap_public_crawl_resource(channel_id,res_title,res_link,res_img_path_set,res_text,res_id,res_file_path_set) values(?,?,?,?,empty_clob(),?,?)");
				pst.clearBatch();
				folders = new ArrayList<FolderBO>();
				int index = 0;
				for(FolderBO folder : list){
					pst.setLong(1, Long.parseLong(folder.getFolderId()));
					//TODO 关键字提示 <font style="color: #ff0000"></font>
					pst.setString(2, filterHandle(folder.getTitle()));
					pst.setString(3, folder.getLink());
					pst.setString(4, folder.getImgPathSet());
					pst.setLong(5, ids.get(index));
					pst.setString(6, folder.getFilePathSet());
					pst.addBatch();
					
					folder.setId(ids.get(index));
					folders.add(folder);
					
					index++;
				}
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
			if(conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			folders = null;
		}finally{
			close(rs, pst, conn);
		}
		return folders;
	}
	
	Boolean update(FolderBO folder){
		Boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String content = null;
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("select res_text from twap_public_crawl_resource where res_id = ? for update");
			pst.setLong(1, folder.getId());
			rs = pst.executeQuery();
			if(rs.next()){
				//TODO 关键字提示 <font style="color: #ff0000"></font>
				content = folder.getContent();
				content = filterHandle(content);
				CLOB clob = (CLOB)rs.getClob("res_text");
				//Oracle 9i 写法
				Writer w = clob.getCharacterOutputStream();
				//TODO Oracle 10g[10.2.0.1.0]
				//getCharacterOutputStream() 
		        //  Deprecated. This method is deprecated. Use setCharacterStream( 0L ).
				BufferedWriter writer = new BufferedWriter(w);
				BufferedReader reader = new BufferedReader(new StringReader(content));
				int len = 0;
				int off = 0;
				char[] cbuf = new char[1024];
				while((len = reader.read(cbuf))!=-1)
					writer.write(cbuf, off, len);
				writer.close();
				reader.close();
				pst = conn.prepareStatement("update twap_public_crawl_resource set res_text = ? where res_id = ?");
				pst.setClob(1, clob);
				pst.setLong(2, folder.getId());
				pst.execute();
			}
			conn.setAutoCommit(true);
			conn.commit();
			
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			if(conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}finally{
			close(rs, pst, conn);
		}
		return bln;
	}
	
	Boolean updateBatch(List<FolderBO> list){
		Boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("update twap_public_crawl_resource set res_text = ? where res_id = ?");
			pst.clearBatch();
			
			for(FolderBO folder : list){
				
				pst.setString(1, folder.getContent());
				pst.setLong(2, folder.getId());
				
				pst.addBatch();
				
			}
			
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			if(conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}finally{
			close(rs, pst, conn);
		}
		return bln;
	}
	
	//废弃的方法
	Boolean saveBatch(FolderBO folder){
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pstFolder = null;
		PreparedStatement pstResource = null;
		ResultSet rs = null;
		
		String sqlFolder = "insert into TWAP_PUBLIC_FOLDER(FOLDER_ID,FOLDERNAME,FATHER_FOLDER_INDEX,FOLDER_INDEX,SHOW_TYPE,MODIFYTIME,MEMO) values(?,?,?,?,'0',sysdate,?)";
		String sqlResource = "insert into TWAP_PUBLIC_FILE_RESOURCE(PAGES_ID,SPCP_ID,FOLDER_ID,TITLE,STATUS,PRICE,POST_DATE,RES_AUTHOR,AVAIL_DATE,FILE_PATH,RES_SIZE,COPYRIGHT,KEYWORD,BROWSE_COUNT,DOWN_COUNT,RES_DESC,MODIFYTIME) " +
				" values(seq_twap_public_file_resource.nextval,'0',?,?,'0',0,sysdate,'web crawler',sysdate+365*3,?,?,'admin',?,0,0,?,sysdate)";
		String sql = "select seq_twap_public_folder.nextval from dual";
		Long folder_id = null;
		
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			
			//get folder id
			pstFolder = conn.prepareStatement(sql);
			rs = pstFolder.executeQuery();
			if(rs.next()) folder_id = rs.getLong(1);
			rs.close();
			
			//save folder object
			pstFolder = conn.prepareStatement(sqlFolder);
			pstFolder.setLong(1, folder_id);
			pstFolder.setString(2, folder.getTitle().trim());
			pstFolder.setString(3, folder.getFolderId());
			pstFolder.setString(4, folder_id.toString());
			pstFolder.setString(5, folder.getLink()+"|"+folder.getContent());
			pstFolder.executeUpdate();
			
			//save resource object
			List<ResourceBO> resList = folder.getResources();
			pstResource = conn.prepareStatement(sqlResource);
			for(ResourceBO res : resList){
				//FOLDER_ID,TITLE,FILE_PATH,RES_SIZE,KEYWORD,RES_DESC
				pstResource.setLong(1, folder_id);
				pstResource.setString(2, folder.getTitle());
				if(res.getType() == ResourceType.PIC){
					pstResource.setString(3, res.getPath());
					pstResource.setInt(4, 0);
				}else if(res.getType() == ResourceType.WORDS){
					pstResource.setString(3, folder.getLink());
					pstResource.setInt(4, res.getContent().length());
				}
				pstResource.setString(5, folder.getTitle());
				pstResource.setString(6, res.getContent());
				pstResource.addBatch();
			}
			pstResource.executeBatch();
			
			//commit;
			conn.commit();
			bln = true;
		}catch(Exception e){
			log.error(e);
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error(e1);
				}
			}
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e);
				}
			if(pstFolder!=null)
				try {
					pstFolder.close();
				} catch (SQLException e) {
					log.error(e);
				}
			if(pstResource!=null)
				try {
					pstResource.close();
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
		return bln;
	}
	
	@Override
	public Boolean clearCrawlResource() {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			String sql = " delete from twap_public_crawl_resource a where to_char(a.create_time,'yyyymmdd') < to_char(sysdate-3,'yyyymmdd') ";
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			
			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					log.error(e1);
				}
			}
		}finally{
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public Boolean deleteCrawlResource(Long channelId) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			String sql = " delete from twap_public_crawl_resource a where a.channel_id = ? ";
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			
			pst = conn.prepareStatement(sql);
			pst.setLong(1, channelId);
			pst.executeUpdate();
			
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					log.error(e1);
				}
			}
		}finally{
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public synchronized Boolean saveLog(AppLog appLog) {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement("insert into twap_app_log_webcrawler(log_id,log_message,log_channel_id,log_url,log_catch_count) values(seq_twap_app_log_webcrawler.nextval,?,?,?,?)");
			pst.setString(1, appLog.getLogMessage());
			pst.setLong(2, appLog.getLogChannelId());
			pst.setString(3, appLog.getUrl());
			pst.setLong(4, appLog.getCatchCount());
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error(e1);
				}
			}
		}finally{
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public Boolean isAnyTriggerFired() {
		boolean bln = true;//执行中
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement("select count(1) from qrtz_fired_triggers");
			rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)==0) bln = false;//未执行
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public Boolean initQuartzDatabase(String sqlFile) {
		boolean bln = true;//执行中
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			String replacement = "";
			File file = new File(sqlFile);
			FileInputStream fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while ((fis.read(buf)) != -1) {
				sb.append(new String(buf));
				buf = new byte[1024];// 重新生成，避免和上次读取的数据重复
			}
			String content = sb.toString();
			content = content.replaceAll(RegexUtil.REGEX_ENTER, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ENTER_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_TAB, replacement);
			content = content.replaceAll(RegexUtil.REGEX_ESC_SPACE, replacement);
			content = content.substring(0, content.lastIndexOf(";")+1);
			log.debug("Quartz Database SQL:"+content);
			String[] sqls = content.split(";");
			bln = batchExecuteSQLSentence(sqls);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					log.error(e1);
				}
			}
		} finally {
			close(rs, pst, conn);
		}
		
		return bln;
	}
	
	@Override
	public List<String> getFilterKeyWordsList() {
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = JavaOracle.getConn();
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
	
	boolean batchExecuteSQLSentence(String[] sqlScript) {
		boolean bln = true;//执行中
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			Properties property = ConfigPropertyUtil.getConfigProperty("quartz.properties");
			String className = property.getProperty("org.quartz.dataSource.myDS.driver");
			String url = property.getProperty("org.quartz.dataSource.myDS.URL");
			String user = property.getProperty("org.quartz.dataSource.myDS.user");
			String password = property.getProperty("org.quartz.dataSource.myDS.password");
			System.out.println("Quartz Database location :"+url+"|"+user+"|"+password);
			if(sqlScript!=null&&sqlScript.length>0){
				conn = JavaOracle.getConn(className, url, user, password);
				conn.setAutoCommit(false);
				
				pst = conn.prepareStatement("select sysdate from dual");
				for(String sql : sqlScript){
					sql = sql.trim();
					if(sql!=null&&!"".equals(sql)){
						log.info("Batch Execution SQL:"+sql);
						pst.executeQuery(sql);
					}
				}
				
				conn.setAutoCommit(true);
				conn.commit();
				bln = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					log.error(e1);
				}
			}
		} finally {
			close(rs, pst, conn);
		}
		
		return bln;
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
