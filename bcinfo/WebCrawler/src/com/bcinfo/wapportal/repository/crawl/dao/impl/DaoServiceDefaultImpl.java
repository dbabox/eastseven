/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceType;
import com.bcinfo.wapportal.repository.crawl.domain.po.CrawlList;

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
	public List<CrawlList> getCrawlLists() {
		List<CrawlList> list = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select a.crawl_id,a.channel_id,a.crawl_url,a.crawl_status,to_char(a.create_time,'yyyy-mm-dd hh24:mi:ss') create_time from twap_public_crawl_list a where a.crawl_status = '1' order by a.channel_id,a.create_time,a.crawl_id";
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
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
	public Boolean saveCrawlResource(List<FolderBO> folders) {
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		//PreparedStatement pstUpdate = null;
		ResultSet rs = null;//res_content,RES_IMG_PATH_SET
		String sql = "insert into twap_public_crawl_resource(channel_id,res_title,res_link,res_img_path_set,res_id,res_text) values(?,?,?,?,?,empty_clob())";
		
		String link = "";
		String title = "";
		String channelId = "";
		String content = "";
		String imgPathSet="";
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
					channelId = folder.getFolderId();
					content = folder.getContent();
					imgPathSet = folder.getImgPathSet();
					pst.setLong(1, Long.valueOf(channelId));
					pst.setString(2, title);
					pst.setString(3, link);
					pst.setString(4, imgPathSet);
					pst.setLong(5, ids.get(count));
					pst.addBatch();
					
					folder.setId(ids.get(count));
					list.add(folder);
					
					count++;
					if(log.isDebugEnabled())
						System.out.println("channel_id:"+channelId+"|title:"+title+"["+title.length()+"]|link:"+link+"["+link.length()+"]|cntSize:"+content.length());
				}
				
				if(log.isDebugEnabled()){
					System.out.println("-------------------insert:"+folders.size());
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
				
				if(log.isDebugEnabled()){
					System.out.println("-------------------update:"+list.size());
				}
				
				conn.commit();
				conn.setAutoCommit(true);
				bln = true;
			}else{
				//oracle 9i 使用方法
				List<FolderBO> list = insertBatch(folders);
				if(list!=null && !list.isEmpty()){
					for(FolderBO folder : list){
						boolean flag = update(folder);
						
						if(log.isDebugEnabled()){
							System.out.println("  "+folder.getId()+" update is "+flag);
						}
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
				pst = conn.prepareStatement("insert into twap_public_crawl_resource(channel_id,res_title,res_link,res_img_path_set,res_text,res_id) values(?,?,?,?,empty_clob(),?)");
				pst.clearBatch();
				folders = new ArrayList<FolderBO>();
				int index = 0;
				for(FolderBO folder : list){
					pst.setLong(1, Long.parseLong(folder.getFolderId()));
					pst.setString(2, folder.getTitle());
					pst.setString(3, folder.getLink());
					pst.setString(4, folder.getImgPathSet());
					pst.setLong(5, ids.get(index));
					pst.addBatch();
					
					folder.setId(ids.get(index));
					folders.add(folder);
					
					if(log.isDebugEnabled()){
						System.out.println(" batch insert data: "+folder.getId()+" | "+folder.getLink()+" | "+folder.getTitle());
					}
					
					index++;
				}
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			System.out.println(" -----batch insert finish----- ");
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
				content = folder.getContent();
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
			
			if(log.isDebugEnabled()){
				System.out.println(" update data: "+folder.getId()+" | "+folder.getLink()+" | "+folder.getTitle());
			}
			
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
				
				if(log.isDebugEnabled()){
					System.out.println(" batch update data: "+folder.getId()+" | "+folder.getLink()+" | "+folder.getTitle());
				}
			}
			
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			
			System.out.println(" -----batch update finish----- ");
			
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
		
		/*
		  FOLDER_ID           NUMBER(11) not null,
		  CHANNEL_ID          NUMBER(11),
		  FOLDERNAME          VARCHAR2(64),
		  FATHER_FOLDER_INDEX VARCHAR2(256),
		  FOLDER_INDEX        VARCHAR2(256),
		  STATUS              VARCHAR2(1) default '1',
		  FOLDER_DESC         VARCHAR2(256),
		  FOLDER_LEVEL        NUMBER(5),
		  FOLDERPATH          VARCHAR2(256),
		  FOLDERURL           VARCHAR2(256),
		  FOLDERPAGECONF      VARCHAR2(256),
		  CONTENTPAGECONF     VARCHAR2(256),
		  CREATE_TIME         DATE default SYSDATE,
		  SHOW_TYPE           VARCHAR2(1) default '1',
		  FOLDER_LOGO         VARCHAR2(256),
		  FOLDER_ICON         VARCHAR2(256),
		  KEYWORD             VARCHAR2(512),
		  PAGE_WORD_SIZE      NUMBER(6),
		  PAGE_IMAGE_SIZE     NUMBER(6),
		  MODIFYTIME          DATE,
		  MEMO                VARCHAR2(256) 
		*/
		String sqlFolder = "insert into TWAP_PUBLIC_FOLDER(FOLDER_ID,FOLDERNAME,FATHER_FOLDER_INDEX,FOLDER_INDEX,SHOW_TYPE,MODIFYTIME,MEMO) values(?,?,?,?,'0',sysdate,?)";
		/*
		  PAGES_ID     NUMBER(15) not null,
		  SPCP_ID      NUMBER(11),
		  FOLDER_ID    NUMBER(11),
		  TITLE        VARCHAR2(128),
		  STATUS       VARCHAR2(1),
		  PRICE        NUMBER(9,3),
		  POST_DATE    DATE,
		  RES_AUTHOR   VARCHAR2(64),
		  AVAIL_DATE   DATE,
		  FILE_PATH    VARCHAR2(256),
		  RES_SIZE     NUMBER(11),
		  COPYRIGHT    VARCHAR2(128),
		  CREATE_TIME  DATE default SYSDATE,
		  KEYWORD      VARCHAR2(512),
		  BROWSE_COUNT NUMBER(11),
		  DOWN_COUNT   NUMBER(11),
		  RES_DESC     VARCHAR2(256),
		  MODIFYTIME   DATE
		*/
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
