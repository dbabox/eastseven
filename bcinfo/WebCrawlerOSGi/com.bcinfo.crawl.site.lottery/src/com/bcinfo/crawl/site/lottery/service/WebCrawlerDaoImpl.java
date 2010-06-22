/**
 * 
 */
package com.bcinfo.crawl.site.lottery.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Resource;
import com.bcinfo.crawl.domain.model.ResourceType;
import com.bcinfo.crawl.site.lottery.database.DatabaseConnection;
import com.bcinfo.crawl.site.lottery.util.Configuration;

/**
 * @author dongq
 * 
 *         create time : 2010-5-14 上午10:07:26
 */
public class WebCrawlerDaoImpl implements WebCrawlerDao {

	private static final Log log = LogFactory.getLog(WebCrawlerDaoImpl.class);
	
	@Override
	public String getSystemDate(String pattern) {
		String date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		final String sql = "select sysdate from dual";
		
		try {
			conn = DatabaseConnection.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if(rs.next()) {
				date = sdf.format(new Date(rs.getDate(1).getTime()));
			}
			
			rs.close();
			pst.close();
			conn.close();
			
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			
		}
		return date;
	}
	
	@Override
	public Boolean save(Resource resource, Connection conn) {
		boolean bln = false;
		
		try {
			bln = save(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bln;
	}
	
	@Override
	public Boolean saveBatch(Resource[] resources, Connection conn) {
		return null;
	}
	
	@Override
	public Boolean isExist(Resource resource) {
		return null;
	}
	
	private static final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat MM = new SimpleDateFormat("MM");
	private static final SimpleDateFormat dd = new SimpleDateFormat("dd");
	private static final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	private String absoluteHtmlFilePath = "";
	private String absoluteImageFilePath = "";
	
	public WebCrawlerDaoImpl() {
		this.absoluteHtmlFilePath = Configuration.HTML_FILE_PATH;
		this.absoluteImageFilePath = Configuration.IMAGE_FILE_PATH;
		log.info("loading html file path :" + this.absoluteHtmlFilePath);
		log.info("loading image file path :" + this.absoluteImageFilePath);
	}
	
	public synchronized boolean save(Resource resource) {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			if(resource == null) throw new Exception("resource object is null...");
			
			conn = DatabaseConnection.getConnection();
			if(conn == null) return false;
			conn.setAutoCommit(false);
			
			if(isExist(resource, conn)) {
				log.warn("资源：" + resource.getTitle() + "已经存在");
				conn.setAutoCommit(true);
				conn.close();
				return false;
			}
			
			//if there were image files, 
			//download and replace the attribute src's value to the download path 
			//of the image target in the content
			List<Map<String, String>> imagePaths = null;
			if(resource.getImgPath() != null && resource.getImgPath().length() > 0){
				imagePaths = downloadImageFiles(resource);
			}
			
			//generation html file and get the relative path of the html file
			Map<String, String> fileInfo = generationHTML(resource);
			
			//insert resource information into the WAP_SITE_FOLDER_RESOURCE
			resource.setId(getFolderResourceId(conn));
			addFolderResource(fileInfo, resource, conn);
			if(resource.getStatus().equals(Resource.AGREE)) {
				//add Sync queue
				addFolderResourceToSyncQueue(resource, conn);
			}
			
			//insert image information into the WAP_SITE_FOLDER_RESOURCE
			if(imagePaths != null && !imagePaths.isEmpty()){
				addFileResource(imagePaths, resource, conn, ResourceType.PIC);
			}
			
			conn.setAutoCommit(true);
			conn.commit();
			conn.close();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if(conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(pst != null)
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return success;
	}
	
	/**
	 * 取得资源ID
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public Long getFolderResourceId(Connection conn) throws Exception {
		long id = 0;
		
		final String sql = "SELECT S_WAP_SITE_FOLDER_RESOURCE.NEXTVAL folderResourceId FROM DUAL";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) id = rs.getLong("folderResourceId");
		
		return id;
	}
	
	public Long getUserId(String userName, Connection conn) throws Exception {
		long userId = 0;
		
		final String sql = "SELECT USER_ID FROM APF_USER WHERE USERNAME = ?";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, userName);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) userId = rs.getLong("user_id");
		
		return userId;
	}
	
	/**
	 * 添加资源内容信息到对应的表中
	 * @param htmlFilePath 生成的HTML页面的相对路径
	 * @param resource 资源对象
	 * @param conn 数据库连接
	 * @throws Exception
	 */
	public void addFolderResource(Map<String, String> fileInfo, Resource resource, Connection conn) throws Exception {
		
		final String sql = "INSERT INTO WAP_SITE_FOLDER_RESOURCE " +
				"( FOLDER_RESOURCE_ID,FOLDER_ID,WAPSITE_ID,PASSNAME,SUBTITLE,POST_DATE,CHECK_DATE,RES_AUTHOR,AVAIL_DATE,FILE_PATH,RES_SIZE,KEYWORD,RES_DESC,BROWSE_COUNT,MODIFY_TIME,STATUS,TEMPLATE_ID,ISTURNTOPAGE,FILE_NAME,USER_ID,COMPONENT_XML_PATH,RES_ORDER,/*RES_ISOPEN,*/RESOURCE_LEVEL_ID,PARENT_RESOURCE ) " + "VALUES" +
				"( ?/* 0.FOLDER_RESOURCE_ID */," +
				"?/* 1.FOLDER_ID */," +
				"?/* 2.WAPSITE_ID */," +
				"?/* 3.PASSNAME */," +
				"?/* 4.SUBTITLE */," +
				"?/* 5.POST_DATE */," +
				"?/* 6.CHECK_DATE */," +
				"?/* 7.RES_AUTHOR */," +
				"?/* 8.AVAIL_DATE */," +
				"?/* 9.FILE_PATH */," +
				"?/* 10.RES_SIZE */," +
				"?/* 11.KEYWORD */," +
				"?/* 12.RES_DESC */," +
				"?/* 13.BROWSE_COUNT */," +
				"?/* 14.MODIFY_TIME */," +
				"?/* 15.STATUS */," +
				"to_number(?)/* 16.TEMPLATE_ID */," +
				"'0'/* 17.ISTURNTOPAGE */," +
				"?/* 18.FILE_NAME */," +
				"?/* 19.USER_ID */," +
				"null/* 20.COMPONENT_XML_PATH */," +
				"999999999/* 21.RES_ORDER */," +
				"1/* 22.RESOURCE_LEVEL_ID */," +
				"-1/* 23.PARENT_RESOURCE */)";
		PreparedStatement pst = conn.prepareStatement(sql);
		Timestamp current = new Timestamp(System.currentTimeMillis());
		Timestamp avail = new Timestamp(System.currentTimeMillis() + 50 * 365 * 60 * 60 * 60 * 1000);//50 years
		Long userId = getUserId(resource.getUserName(), conn);
		
		pst.setLong(     1, resource.getId());                            //FOLDER_RESOURCE_ID
		pst.setLong(     2, resource.getChannel().getId());               //FOLDER_ID
		pst.setLong(     3, 641);                                         //WAPSITE_ID 暂时写死
		pst.setString(   4, resource.getTitle());                         //PASSNAME
		pst.setString(   5, "");                                          //SUBTITLE
		pst.setTimestamp(6, current);                                     //POST_DATE
		pst.setTimestamp(7, current);                                     //CHECK_DATE
		pst.setString(   8, "WebCrawler");                                //RES_AUTHOR
		pst.setTimestamp(9, avail);                                       //AVAIL_DATE
		pst.setString(   10, fileInfo.get("filePath"));                   //FILE_PATH
		pst.setLong(     11, Long.parseLong(fileInfo.get("fileSize")));   //RES_SIZE
		pst.setString(   12, "");                                         //KEYWORD
		pst.setString(   13, "");                                         //RES_DESC
		pst.setLong(     14, 0);                                          //BROWSE_COUNT
		pst.setTimestamp(15, current);                                    //MODIFY_TIME
		
		pst.setString(   16, resource.getStatus());                          //STATUS 已审核
		
		pst.setString(   17, ResourceType.WORDS);                            //TEMPLATE_ID
		pst.setString(   18, fileInfo.get("fileName"));                      //FILE_NAME
		pst.setLong(     19, userId);                                          //USER_ID
		
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * 添加资源图片或文件信息到对应的表中
	 * @param fileList 下载的文件或图片的相关信息
	 * @param resource 资源对象
	 * @param conn 数据库连接
	 * @throws Exception
	 */
	public void addFileResource(List<Map<String, String>> fileList, Resource resource, Connection conn, String resType)  throws Exception {
		String sql = " INSERT INTO WAP_SITE_FILE_RESOURCE ( " +
				"FILE_RESOURCE_ID,FOLDER_ID,WAPSITE_ID,PASSNAME,SUBTITLE,POST_DATE,CHECK_DATE,RES_AUTHOR,AVAIL_DATE,FILE_PATH,RES_SIZE,KEYWORD,RES_DESC,BROWSE_COUNT,MODIFY_TIME,STATUS,RES_TYPE ) " +
				"VALUES( " +
				"S_WAP_SITE_FILE_RESOURCE.NEXTVAL/* 1.FILE_RESOURCE_ID */," +
				"?/* 2.FOLDER_ID */," +
				"?/* 3.WAPSITE_ID */," +
				"?/* 4.PASSNAME */," +
				"?/* 5.SUBTITLE */," +
				"?/* 6.POST_DATE */," +
				"?/* 7.CHECK_DATE */," +
				"?/* 8.RES_AUTHOR */," +
				"?/* 9.AVAIL_DATE */," +
				"?/* 10.FILE_PATH */," +
				"?/* 11.RES_SIZE */," +
				"?/* 12.KEYWORD */," +
				"?/* 13.RES_DESC */," +
				"?/* 14.BROWSE_COUNT */," +
				"?/* 15.MODIFY_TIME */," +
				"?/* 16.STATUS */," +
				"?/* 17.RES_TYPE */ ) ";
		PreparedStatement pst = conn.prepareStatement(sql);
		Timestamp current = new Timestamp(System.currentTimeMillis());
		Timestamp avail = new Timestamp(System.currentTimeMillis() + 50 * 365 * 60 * 60 * 60 * 1000);//50 years
		for(Map<String, String> fileInfo : fileList) {
			log.debug("pic info : " + fileInfo);
			pst.setLong(1, resource.getChannel().getId());               //FOLDER_ID
			pst.setLong(2, 641);                                         //WAPSITE_ID 暂时写死
			pst.setString(3, resource.getTitle());                       //PASSNAME
			pst.setString(4, fileInfo.get("fileName"));                  //SUBTITLE
			pst.setTimestamp(5, current);                                //POST_DATE
			pst.setTimestamp(6, current);                                //CHECK_DATE
			pst.setString(7, "WebCrawler");                              //RES_AUTHOR
			pst.setTimestamp(8, avail);                                  //AVAIL_DATE
			pst.setString(9, fileInfo.get("filePath"));                  //FILE_PATH
			pst.setLong(10, Long.parseLong(fileInfo.get("fileSize")));   //RES_SIZE
			pst.setString(11, "");                                       //KEYWORD
			pst.setString(12, resource.getLink());                       //RES_DESC
			pst.setLong(13, 0);                                          //BROWSE_COUNT
			pst.setTimestamp(14, current);                               //MODIFY_TIME
			pst.setString(15, "0");                                      //STATUS 未审核
			pst.setString(16, resType);                                  //RES_TYPE
			pst.addBatch();
		}
		int[] batch = pst.executeBatch();
		conn.commit();
		log.debug("batch insert pic resource : " + batch.length);
		
	}

	/**
	 * 添加资源到同步队列中
	 * @param resource
	 * @param conn
	 * @throws Exception
	 */
	public void addFolderResourceToSyncQueue(Resource resource, Connection conn) throws Exception {
		
		final String sql = "INSERT INTO TWAP_PROVISION_QUEUE (QUEUE_ID, WAPPORTAL_ID, PROCESS_TYPE, FOLDER_ID, OTHER_ID) VALUES (SEQ_TWAP_PROVISION_QUEUE.NEXTVAL,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.clearBatch();
		for(Integer portalId : Configuration.CAI_PIAO_WAP_PORTAL_IDS) {
			pst.setInt(   1, portalId);
			pst.setString(2, "3");
			pst.setLong(  3, resource.getChannel().getId());
			pst.setLong(  4, resource.getId());
			pst.addBatch();
		}
		int[] batch = pst.executeBatch();
		conn.commit();
		log.info(resource.getTitle() + " 添加资源到同步队列中:" + batch.length);
	}
	
	/**
	 * 生成HTML文件<br>
	 * 存放位置<br>
	 * @param resource
	 * @return 生成文件的相关信息，<br>
	 *         fileName<br>
	 *         filePath<br>
	 *         fileSize<br>
	 */
	public Map<String, String> generationHTML(Resource resource) {

		Map<String, String> fileInfo = new HashMap<String, String>();
		String relativeFilePath = "";
		OutputStream output = null;
		
		try {
			
			if(resource == null) throw new Exception("resource object is null...");

			Date date = new Date();
			String fileName = "P" + yyyyMMddHHmmss.format(date) + RandomStringUtils.random(5, false, true) + ".html";
			fileInfo.put("fileName", fileName);
			File folder = new File(this.absoluteHtmlFilePath + yyyyMM.format(date) + "/");
			if(!folder.exists()) folder.mkdirs();
			relativeFilePath = "/admin/uploadHTML/" + yyyyMM.format(date) + "/" + fileName;
			fileInfo.put("filePath", relativeFilePath);
			File file = new File(this.absoluteHtmlFilePath + relativeFilePath);
			output = new FileOutputStream(file);
			IOUtils.write(resource.getContent(), output);
			String size = FileUtils.byteCountToDisplaySize(resource.getContent().getBytes().length).replaceAll("KB", "").trim();
			if(size.contains("bytes")) size = "1";
			fileInfo.put("fileSize", size);
			
			log.debug(fileInfo);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			
		} finally {
			if(output != null) IOUtils.closeQuietly(output);
		}
		
		return fileInfo;
	}
	
	/**
	 * 图片下载，图片路径本地化
	 * @param resource
	 * @return
	 */
	public List<Map<String, String>> downloadImageFiles(Resource resource) {
		List<Map<String, String>> relativeFilePaths = new ArrayList<Map<String, String>>();
		
		try {
			
			if(resource == null) throw new Exception("resource object is null...");
			
			if(resource.getImgPath().isEmpty()) return relativeFilePaths;
			
			String[] images = resource.getImgPath().split(",");
			if(images != null && images.length > 0){
				String content = resource.getContent();
				for(String imageSrc : images){
					if(imageSrc.length() == 0) continue;
					
					Map<String, String> fileInfo = new HashMap<String, String>();
					Date date = new Date();
					String relativeFilePath = "";
					String fileName = "P" + yyyyMMddHHmmss.format(date) + RandomStringUtils.random(5, false, true) + imageSrc.substring(imageSrc.lastIndexOf("."));
					fileInfo.put("fileName", fileName);
					relativeFilePath = "/admin/upload/images/" + yyyy.format(date) + "/" + MM.format(date) + "/" + dd.format(date) + "/";
					File folder = new File(this.absoluteHtmlFilePath + relativeFilePath);
					if(!folder.exists()) folder.mkdirs();
					fileInfo.put("filePath", relativeFilePath);
					File file = new File(this.absoluteImageFilePath + relativeFilePath + fileName);
					relativeFilePath = relativeFilePath + fileName;
					OutputStream output = new FileOutputStream(file);
					InputStream input = null;
					try {
						input = new URL(imageSrc).openStream();
					} catch (ConnectException e) {
						e.printStackTrace();
						content = content.replace(imageSrc, "<img src='' alt='下载失败' />");
						continue;
					}
					if(input == null) {
						log.warn(imageSrc + " is invalid url");
						continue;
					}
					
					int bytesRead = 0;
					byte[] buffer = new byte[8192];

					while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
					if(input != null) input.close();
					if(output != null) output.close();
					
					String size = FileUtils.byteCountToDisplaySize(FileUtils.readFileToByteArray(file).length).replaceAll("KB", "").trim();
					if(size.contains("bytes")) size = "1";
					else if(size.contains("MB")) size = String.valueOf(Long.parseLong(size)*1024);
					else if(size.contains("GB")) size = String.valueOf(Long.parseLong(size)*1024*1024);
					fileInfo.put("fileSize", size);
					
					content = content.replaceAll(imageSrc, relativeFilePath);
					log.info("\n download file["+size+"] " + imageSrc + " to " + this.absoluteImageFilePath + relativeFilePath + "\n");
					relativeFilePaths.add(fileInfo);
				}
				log.debug(content);
				resource.setContent(content);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return relativeFilePaths;
	}
	
	/**
	 * 该资源是否已经存在
	 * @param resource
	 * @param conn
	 * @return true-存在；false-不存在；
	 * @throws Exception
	 */
	private boolean isExist(Resource resource, Connection conn) throws Exception {
		boolean bln = true;
		
		final String sql = "SELECT COUNT(1) RES_COUNT FROM WAP_SITE_FOLDER_RESOURCE WHERE PASSNAME = ? AND FOLDER_ID = ? AND STATUS = '1'";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, resource.getTitle().trim());
		pst.setLong(2, resource.getChannel().getId());
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			if(rs.getInt("RES_COUNT") == 0) bln = false;
		}
		rs.close();
		pst.close();
		
		return bln;
	}

	@Override
	public Boolean saveBatch(Object[] resources, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}
}
