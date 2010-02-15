/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.Folder;
import com.bcinfo.wapportal.repository.crawl.domain.Resource;

/**
 * @author dongq
 * 
 *         create time : 2009-9-9 下午03:40:57<br>
 *         数据库操作<br>
 */
public final class OperationDB {

	private static final Logger log = Logger.getLogger(OperationDB.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String NEED_CHECK = "wap_page_folder_spcp";
	public static final String NOT_NEED_CHECK = "wap_page_folder";

	public static final String DEFAULT_FOLDER_TYPE = "1";//只有栏目的格式
	public static final String MULT_FOLDER_TYPE = "2";//图片资源和栏目混排格式
	
	Properties property;
	String dir = null;

	public OperationDB() {
		property = new ConfigPropertyUtil().getConfigProperty();
		// String dir = null;
		if (property != null) {
			String os = System.getenv("OS");
			System.out.println("OS:" + os);
			if (os != null && !"".equals(os) && !"null".equals(os)) {
				dir = property.getProperty("resource.dir.windows");
				System.out.println("WINNT:" + dir);
			} else {
				dir = property.getProperty("resource.dir.linux");
				System.out.println("LINUX:" + dir);
			}
		} else {
			System.out.println(" 没有取到配置文件的值 ");
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean patchSave(List folders) throws Exception {
		boolean isSuccess = false;
		if (folders == null) throw new Exception("数据集为空");
		DaoService dao = new DaoService();
		try {
			for (int i = 0; i < folders.size(); i++) {
				Folder folder = (Folder) folders.get(i);
				boolean bln = isExsit(folder);
				if(bln) continue;
				/*
				log.info(folder.getTitle()+" is exsit :"+bln+"|operation:"+folder.getOperation());
				if(String.valueOf(Folder.UPDATE).equals(folder.getOperation())){
					//TODO 050001单独处理,一个子栏目只保存一条记录
					bln = update(folder);
					log.info("更新栏目["+folder.getId()+"]["+folder.getTitle()+"]"+bln);
				}else if(String.valueOf(Folder.INSERT).equals(folder.getOperation())){
					if(bln) continue;//跳过已经存在的记录
				}
				*/
				if(DEFAULT_FOLDER_TYPE.equals(folder.getSendType())){
					bln = save(folder);
				}else if(MULT_FOLDER_TYPE.equals(folder.getSendType())){
					bln = saveAsOtherType(folder);
				}
				log.info("添加栏目["+folder.getId()+"]["+folder.getTitle()+"]"+bln);
				
				String fileName = folder.getResFileName();
				if (bln) {
					if (dir != null) {
						File file = new File(dir + fileName);
						System.out.println("文件：" + dir + fileName);
						if (file.exists()) {
							log.info("文件：" + dir + fileName + " 删除：" + file.delete());
						} else {
							log.info("文件：" + dir + fileName + " 不存在");
						}
					}
					dao.deleteInternalFileLog(fileName);
					log.info(folder.getTitle() + " 入库成功 ");
				} else{
					dao.updateInternalFileLog(fileName, "0");
					log.info(folder.getTitle() + " 入库失败 ");
				}
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			log.error("批量保存失败");
		}

		return isSuccess;
	}

	public boolean isExsit(Folder folder) throws Exception{
		boolean bln = false;//默认是不存在的
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		//select count(1) from wap_page_folder where wap_folder_id = ? and folder_name = ?
		String sql = " select count(c.folder_id) from( ";
		sql += " select a.folder_id from wap_page_folder a where a.wap_folder_id = ? and a.folder_name = ? ";
		sql += " union select b.folder_id from wap_page_folder_spcp b where b.wap_folder_id = ? and b.folder_name = ? ";
		sql += " ) c ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, folder.getId());
			pst.setString(2, folder.getTitle());
			pst.setString(3, folder.getId());
			pst.setString(4, folder.getTitle());
			rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getInt(1) > 0) bln = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}finally{
			if(rs != null) rs.close();
			if(pst != null) pst.close();
			if(conn != null) conn.close();
		}
		return bln;
	}
	
	//针对星座占卜，只更新文字内容
	//由于CCTV的缘故，停止针对星座占卜的自动更新
	@Deprecated
	public boolean update(Folder folder) throws Exception {
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " update wap_resource a set a.res_content = ? where a.res_type_id = ? and a.res_id in (select b.res_id from wap_re_folder_res b where b.folder_id = ?)";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, folder.getContent());
			pst.setInt(2, ResourceType.WORDS);
			pst.setString(3, folder.getId());
			if(pst.executeUpdate() == 1){
				conn.commit();
				bln = true;
				log.info("星座占卜子栏目["+folder.getId()+"]内容更新完成");
			}else{
				log.info("星座占卜子栏目["+folder.getId()+"]内容更新失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			if(conn != null) conn.rollback();
		}finally{
			if(rs != null) rs.close();
			if(pst != null) pst.close();
			if(conn != null) conn.close();
		}
		
		return bln;
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveAsOtherType(Folder folder) throws Exception {
		boolean isSuccess = false;
		
		Connection conn = null;
		PreparedStatement pstFolder = null;
		PreparedStatement pstResource = null;
		PreparedStatement pstReFolderRes = null;
		
		try {
			// 1.generate folder
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			String wapFolderId = folder.getId();
			long resIdForPic = 0;
			String folderId = wapFolderId + getFolderId(wapFolderId);// 12位
			//TODO 无审核直接发布
			String table = OperationDB.NEED_CHECK;
			String folder_status = "0";
			if("10".equals(folder.getOperation())){
				table = OperationDB.NOT_NEED_CHECK;
				folder_status = "1";
			}
			String sqlFolder = "insert into "+table+"(folder_id,wap_folder_id,folder_name,folder_index,folder_status,folder_desc,folder_level,create_time,PAGE_WORD_SIZE,VISIT_TIME) ";
			sqlFolder += " values(?,?,?,0,?,?,3,sysdate,255,0) ";
			pstFolder = conn.prepareStatement(sqlFolder);
			pstFolder.setString(1, folderId);
			pstFolder.setString(2, wapFolderId);
			pstFolder.setString(3, folder.getTitle());
			pstFolder.setString(4, folder_status);
			pstFolder.setString(5, folder.getTitle());
			pstFolder.executeUpdate();
			pstFolder.clearParameters();
			pstFolder.close();

			log.info("");
			log.info(wapFolderId + "|" + folderId + "|" + folder.getTitle());
			log.info("-----------------------------------------------------------------------------------");
			// 2.generate resource
			// 3.related 1 and 2
			String sqlResource = "insert into wap_resource"
					+ "(res_id,spcp_id,res_type_id,firstname,passname,check_flag,res_status,store_filepath,res_size,res_desc,CREATE_TIME,res_content,DOWN_COUNT,CLICK_SUM,RES_AUTHOR,CORP_NAME,COPYRIGHT) ";
			sqlResource += " values(?,1,?,?,?,'0',?,?,?,?,?,?,0,0,'web crawler',to_char(sysdate,'yy/mm/dd hh24:mi:ss'),'web crawler') ";

			String sqlReFolderRes = "insert into wap_re_folder_res(folder_id,res_id,order_index) values(?,?,0)";
			pstResource = conn.prepareStatement(sqlResource);
			List resourceList = folder.getResources();
			long[] ids = getResId(resourceList.size());

			//int i = resourceList.size() - 1; i >= 0; i--
			//int i = 0; i < resourceList.size(); i++
			for (int i = 0; i < resourceList.size(); i++) {
				// TODO 按时间倒序排列
				long resId = ids[i];

				log.info(" ****** wap_resource.res_id:" + resId + " used " + isUsed(resId));

				Resource resource = (Resource) resourceList.get(i);
				String path = "";
				if (resource.getResourceType() == ResourceType.PIC) {
					path = resource.getResourcePath();
				}else if(resource.getResourceType() == ResourceType.SOFTWARE){
					path = resource.getResourcePath();
				}

				pstResource.setLong(1, resId);
				pstResource.setLong(2, resource.getResourceType());
				if(resource.getResourceType()==ResourceType.PIC){
					resIdForPic = resId;
				}
				pstResource.setString(3, folder.getTitle());
				pstResource.setString(4, folder.getTitle());
				pstResource.setString(5, folder_status);
				System.out.println(" ******************************** "+resId+"|"+folder_status);
				pstResource.setString(6, path);
				pstResource.setLong(7, resource.getResourceContent().length());
				pstResource.setString(8, "");
				//批量操作时，时间都是一样的，下面的赋值是没有意义的
				//pstResource.setDate(9, new java.sql.Date(System.currentTimeMillis()+i*65432));
				//时间戳可以
				pstResource.setTimestamp(9, new Timestamp(System.currentTimeMillis()-i*1000));
				pstResource.setString(10, resource.getResourceContent());
				pstResource.addBatch();
			}
			pstResource.executeBatch();

			pstReFolderRes = conn.prepareStatement(sqlReFolderRes);
			for (int j = resourceList.size() - 1; j >= 0 ; j--) {
				long resId = ids[j];
				pstReFolderRes.setString(1, folderId);
				pstReFolderRes.setLong(2, resId);
				pstReFolderRes.addBatch();
			}
			if(resIdForPic!=0){
				pstReFolderRes.setString(1, wapFolderId);
				pstReFolderRes.setLong(2, resIdForPic);
				pstReFolderRes.addBatch();
			}
			pstReFolderRes.executeBatch();

			pstResource.clearBatch();
			pstResource.close();

			pstReFolderRes.clearBatch();
			pstReFolderRes.close();

			conn.commit();
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			if (conn != null)
				conn.rollback();
		} finally {
			if (pstFolder != null)
				pstFolder.close();
			if (pstResource != null)
				pstResource.close();
			if (pstReFolderRes != null)
				pstReFolderRes.close();
			if (conn != null)
				conn.close();
		}
		
		return isSuccess;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized boolean save(Folder folder) throws Exception {
		boolean isSuccess = false;
		if (folder == null) throw new Exception(" folder is null... ");
		Connection conn = null;
		PreparedStatement pstFolder = null;
		PreparedStatement pstResource = null;
		PreparedStatement pstReFolderRes = null;
		try {
			// 1.generate folder
			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			String wapFolderId = folder.getId();
			String folderId = wapFolderId + getFolderId(wapFolderId);// wapFolderId.len+6位
			//TODO 无审核直接发布
			String table = OperationDB.NEED_CHECK;
			String folder_status = "0";
			if("10".equals(folder.getOperation())){
				table = OperationDB.NOT_NEED_CHECK;
				folder_status = "1";
			}
			String sqlFolder = "insert into "+table+"(folder_id,wap_folder_id,folder_name,folder_index,folder_status,folder_desc,folder_level,create_time,PAGE_WORD_SIZE,VISIT_TIME) ";
			sqlFolder += " values(?,?,?,0,?,?,3,sysdate,255,0) ";
			pstFolder = conn.prepareStatement(sqlFolder);
			pstFolder.setString(1, folderId);
			pstFolder.setString(2, wapFolderId);
			pstFolder.setString(3, folder.getTitle());
			pstFolder.setString(4, folder_status);
			pstFolder.setString(5, folder.getTitle());
			pstFolder.executeUpdate();
			pstFolder.clearParameters();
			pstFolder.close();
			
			//conn.commit();
			
			log.info("");
			log.info(wapFolderId + "|" + folderId + "|" + folder.getTitle());
			log.info("-----------------------------------------------------------------------------------");
			// 2.generate resource
			// 3.related 1 and 2
			String sqlResource = "insert into wap_resource"
					+ "(res_id,spcp_id,res_type_id,firstname,passname,check_flag,res_status,store_filepath,res_size,res_desc,CREATE_TIME,res_content,DOWN_COUNT,CLICK_SUM,RES_AUTHOR,CORP_NAME,COPYRIGHT) ";
			sqlResource += " values(?,1,?,?,?,'0',?,?,?,?,?,?,0,0,'web crawler',to_char(sysdate,'yy/mm/dd hh24:mi:ss'),'web crawler') ";

			String sqlReFolderRes = "insert into wap_re_folder_res(folder_id,res_id,order_index) values(?,?,0)";
			pstResource = conn.prepareStatement(sqlResource);
			List resourceList = folder.getResources();
			long[] ids = getResId(resourceList.size());
			
			//int i = resourceList.size() - 1; i >= 0; i--
			//int i = 0; i < resourceList.size(); i++
			for (int i = 0; i < resourceList.size(); i++) {
				// TODO 按时间倒序排列
				long resId = ids[i];

				log.info(" ****** wap_resource.res_id:" + resId + " used " + isUsed(resId));

				Resource resource = (Resource) resourceList.get(i);
				String path = "";
				if (resource.getResourceType() == ResourceType.PIC) {
					path = resource.getResourcePath();
				}else if(resource.getResourceType() == ResourceType.SOFTWARE){
					path = resource.getResourcePath();
				}

				pstResource.setLong(1, resId);
				pstResource.setLong(2, resource.getResourceType());
				pstResource.setString(3, folder.getTitle());
				pstResource.setString(4, folder.getTitle());
				pstResource.setString(5, folder_status);
				System.out.println(" ******************************** "+resId+"|"+folder_status);
				pstResource.setString(6, path);
				pstResource.setLong(7, resource.getResourceContent().length());
				pstResource.setString(8, "");
				//批量操作时，时间都是一样的，下面的赋值是没有意义的
				//pstResource.setDate(9, new java.sql.Date(System.currentTimeMillis()+i*65432));
				//时间戳可以
				pstResource.setTimestamp(9, new Timestamp(System.currentTimeMillis()-i*1000));
				pstResource.setString(10, resource.getResourceContent());
				pstResource.addBatch();
			}
			pstResource.executeBatch();

			pstReFolderRes = conn.prepareStatement(sqlReFolderRes);
			for (int j = resourceList.size() - 1; j >= 0 ; j--) {
				long resId = ids[j];

				pstReFolderRes.setString(1, folderId);
				pstReFolderRes.setLong(2, resId);
				pstReFolderRes.addBatch();
			}
			pstReFolderRes.executeBatch();

			pstResource.clearBatch();
			pstResource.close();

			pstReFolderRes.clearBatch();
			pstReFolderRes.close();

			conn.setAutoCommit(true);
			conn.commit();
			isSuccess = true;
			Thread.sleep(3*1000);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			if (conn != null)
				conn.rollback();
		} finally {
			if (pstFolder != null)
				pstFolder.close();
			if (pstResource != null)
				pstResource.close();
			if (pstReFolderRes != null)
				pstReFolderRes.close();
			if (conn != null)
				conn.close();
		}
		return isSuccess;
	}

	// 9位流水号字符串
	public synchronized String getFolderId(String id) {
		String folderId = "";
		String sql = "select seq_commons_folder.nextval from dual";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()){
				long val = rs.getLong(1);
				folderId = String.valueOf(val);
				int len = folderId.length();
				String zero = "";
				for(int i=0;i<8-len;i++) zero += "0";
				folderId = "9" + zero + val;
			}
			log.info("新流水号："+folderId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return folderId;
	}

	public int getFolderIdLength(String wapFolderId) {
		int length = 0;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		sql += " select nvl(max(length(c.folder_id)),0) ";
		sql += " from( ";
		sql += " select a.folder_id from wap_page_folder a where a.wap_folder_id = ? ";
		sql += " union ";
		sql += " select b.folder_id from wap_page_folder_spcp b where b.wap_folder_id = ? ";
		sql += " ) c ";
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, wapFolderId);
			pst.setString(2, wapFolderId);
			rs = pst.executeQuery();
			if (rs.next())
				length = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return length;
	}

	public boolean isUsed(String folderId) {
		boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		sql += " select count(c.folder_id) ";
		sql += " from( ";
		sql += " select a.folder_id from wap_page_folder a where a.wap_folder_id = '"+folderId+"' ";
		sql += " union ";
		sql += " select b.folder_id from wap_page_folder_spcp b where b.wap_folder_id = '"+folderId+"' ";
		sql += " ) c ";
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				bln = (rs.getInt(1) > 0);
				log.info(sql+":"+bln);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return bln;
	}
	
	public boolean isUsed(long id) {
		boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement("select count(res_id) from wap_resource where res_id = ?");
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				bln = (rs.getInt(1) > 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return bln;
	}

	public long getId(String sequence) {
		long id = 0;
		String sql = "select " + sequence + ".nextval from dual";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next())
				id = rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return id;
	}

	public long[] getResId(int length) {
		String sqSql = "select sq_WAP_RESOURCE.nextval from dual";
		Connection conn = JavaOracle.getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long[] primaryKey = new long[length];
		try {
			ps = conn.prepareStatement(sqSql);
			//int i = length - 1; i >= 0; i--
			//int i=0; i<length;i++
			for (int i = length - 1; i >= 0; i--) {
				rs = ps.executeQuery();
				if (rs.next()) primaryKey[i] = rs.getLong(1);
				rs.close();
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return primaryKey;
	}

	/**
	 * 保存网页上的图片到本地服务器上
	 * 
	 */
	public String writeFile(String strUrl, String fileName) {
		String path = null;
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e2) {
		}
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e1) {
		}
		
		if(is == null){
			return null;
		}
		
		OutputStream os = null;

		GregorianCalendar gcDate = new GregorianCalendar();
		int year = gcDate.get(GregorianCalendar.YEAR);
		int month = gcDate.get(GregorianCalendar.MONTH);
		int day = gcDate.get(GregorianCalendar.DAY_OF_MONTH);
		subDir = year + "/" + (month + 1) + "/" + day + "/";
		destImgUrl = new ConfigPropertyUtil().getConfigProperty().getProperty("img.dir");
		File f = new File(destImgUrl + subDir);
		if (!f.exists())
			f.mkdirs();

		try {
			os = new FileOutputStream(destImgUrl + subDir + fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];

			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			path = "/upload/" + subDir;
			log.info("图片保存在:" + destImgUrl + subDir + fileName);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
		
		try{
			if(is!=null) is.close();
			if(os!=null) os.close();
		}catch(Exception e){
		}
		
		return path;
	}

	public String downloadFile(String link) {
		String filePath = null;
		InputStream is = null;
		OutputStream os = null;
		sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		destImgUrl = new ConfigPropertyUtil().getConfigProperty().getProperty("img.dir");
		try{
			String fileSuffix = link.substring(link.lastIndexOf(".")+1);
			URL url = new URL(link);
			URLConnection http = url.openConnection();
			http.addRequestProperty("Referer", link);
			is = http.getInputStream();
			if(is!=null){
				GregorianCalendar gcDate = new GregorianCalendar();
				int year = gcDate.get(GregorianCalendar.YEAR);
				int month = gcDate.get(GregorianCalendar.MONTH);
				int day = gcDate.get(GregorianCalendar.DAY_OF_MONTH);
				subDir = year + "/" + (month + 1) + "/" + day + "/";
				filePath = destImgUrl + subDir + "/flying/other/";
				File file = new File(filePath);
				if(!file.exists()) file.mkdir();
				String fileName = "pa_file_"+sdf.format(new Date(System.currentTimeMillis()))+"."+fileSuffix;
				filePath += fileName;
				//if(!file.exists()) file.createNewFile();
				file = new File(filePath);
				os = new FileOutputStream(file);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];

				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				log.info("文件下载后存放于"+filePath);
				filePath = "http://218.205.231.122/wap/upload/" + subDir + "/flying/other/"+fileName;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}finally{
			try{
				if(is!=null) is.close();
				if(os!=null) os.close();
			}catch(Exception e){
			}
		}
		return filePath;
	}
	
	public String destImgUrl = "";

	public String subDir = "";

	//http://www.izhuti.com/download.php?id=46639
	//重定向
	//http://www.moxiu.com/down.html?rid=5968872&file=theme/sisdd/f4/bbe/f4bbeccf/moxiu1259849728.sis
	public static void main(String[] args) {
		try{
			//http://www.moxiu.com/down.html?rid=6137935&file=theme/sisdd/ce/a1f/cea1f1d6/moxiu1261670130.sis
			//http://www.moxiu.com/themes/2/2009/1224/6137935.shtml
			System.out.println(new OperationDB().downloadFile("http://www.moxiu.com/down.html?rid=6137935&file=theme/sisdd/ce/a1f/cea1f1d6/moxiu1261670130.sis"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
