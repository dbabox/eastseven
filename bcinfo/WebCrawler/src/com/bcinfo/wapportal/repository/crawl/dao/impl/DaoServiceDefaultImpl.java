/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		ResultSet rs = null;//res_content,RES_IMG_PATH_SET
		String sql = "insert into twap_public_crawl_resource(res_id,channel_id,res_title,res_link,res_img_path_set) values(seq_twap_public_crawl_resource.nextval,?,?,?,?)";
		
		String link = "";
		String title = "";
		String channelId = "";
		String content = "";
		String imgPathSet="";
		try{
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			for(FolderBO folder : folders){
				link = folder.getLink();
				title = folder.getTitle();
				channelId = folder.getFolderId();
				content = folder.getContent();
				imgPathSet = folder.getImgPathSet();
				log.debug("channel_id:"+channelId+"|title:"+title+"["+title.length()+"]|link:"+link+"["+link.length()+"]|cntSize:"+content.length());
				pst.setLong(1, Long.valueOf(channelId));
				pst.setString(2, title);
				pst.setString(3, link);
				pst.setString(4, imgPathSet);
				pst.addBatch();
				//log.info(link+" | "+title+" | 保存成功");
			}
			pst.executeBatch();
			pst.clearBatch();
			
			sql = "update twap_public_crawl_resource set res_text = ? where channel_id = ? and res_title = ? and res_link = ?";
			pst = conn.prepareStatement(sql);
			for(FolderBO folder : folders){
				pst.setString(1, folder.getContent());
				pst.setLong(2, Long.valueOf(folder.getFolderId()));
				pst.setString(3, folder.getTitle());
				pst.setString(4, folder.getLink());
				pst.addBatch();
			}
			pst.executeBatch();
			pst.clearBatch();
			
			pst.close();
			conn.commit();
			bln = true;
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
