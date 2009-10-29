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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

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

	public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Properties property;
	String dir = null;
	public OperationDB() {
		property = new ConfigPropertyUtil().getConfigProperty();
		//String dir = null;
		if(property!=null){
			String os = System.getenv("OS");
			System.out.println("OS:"+os);
			if(os!=null&&!"".equals(os)&&!"null".equals(os)){
				dir = property.getProperty("resource.dir.windows");
				System.out.println("WINNT:"+dir);
			}else{
				dir = property.getProperty("resource.dir.linux");
				System.out.println("LINUX:"+dir);
			}
		}else{
			System.out.println(" 没有取到配置文件的值 ");
		}
	}
	
	public boolean patchSave(List folders) throws Exception {
		boolean isSuccess = false;
		if (folders == null) throw new Exception("数据集为空");

		try {
			for (int i = 0; i < folders.size(); i++) {
				Folder folder = (Folder) folders.get(i);
				boolean bln = save(folder);
				
				if(bln){
					
					//dir = property.getProperty("resource.dir");
					if(dir!=null){
						File file = new File(dir+folder.getResFileName());
						System.out.println("文件："+dir+folder.getResFileName());
						if(file.exists()){
							System.out.println("文件："+dir+folder.getResFileName()+" 删除："+file.delete());
						}else{
							System.out.println("文件："+dir+folder.getResFileName()+" 不存在");
						}
					}
					System.out.println(sdf.format(new Date())+" : "+folder.getTitle()+" 入库成功 ");
				}else
					System.out.println(sdf.format(new Date())+" : "+folder.getTitle()+" 入库失败 ");
				/*
				try {
					Thread.sleep(1000 * 3);// 等3秒再插下一条
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				*/
			}
			isSuccess = true;
		} catch (Exception e) {

		}

		return isSuccess;
	}

	public boolean save(Folder folder) throws Exception {
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
			Date date = new Date();
			String wapFolderId = folder.getId();
			String folderId = wapFolderId + getFolderId(wapFolderId);//12位
			String sqlFolder = "insert into wap_page_folder(folder_id,wap_folder_id,folder_name,folder_index,folder_status,folder_desc,folder_level,create_time) ";
			sqlFolder += " values(?,?,?,0,0,?,3,to_date(?,'yyyy-mm-dd hh24:mi:ss')) ";
			pstFolder = conn.prepareStatement(sqlFolder);
			pstFolder.setString(1, folderId);
			pstFolder.setString(2, wapFolderId);
			pstFolder.setString(3, folder.getTitle());
			pstFolder.setString(4, folder.getTitle());
			pstFolder.setString(5, sdf.format(date));
			pstFolder.executeUpdate();
			pstFolder.clearParameters();
			pstFolder.close();
			
			System.out.println("");
			System.out.println(wapFolderId+"|"+folderId+"|"+folder.getTitle());
			System.out.println("-----------------------------------------------------------------------------------");
			// 2.generate resource
			// 3.related 1 and 2
			String sqlResource = "insert into wap_resource" + "(res_id,spcp_id,res_type_id,firstname,passname,check_flag,res_status,store_filepath,res_size,res_desc,CREATE_TIME,res_content,DOWN_COUNT,CLICK_SUM,RES_AUTHOR,CORP_NAME,COPYRIGHT) ";
			sqlResource += " values(?,1,?,?,?,'0','1',?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,0,0,'web crwaler','web crwaler','web crwaler') ";
			/*
			  *RES_ID         INTEGER not null,
			  *SPCP_ID        INTEGER,
			  *RES_TYPE_ID    INTEGER,
			  SPCP_CODE      VARCHAR2(20),
			  *FIRSTNAME      VARCHAR2(120),
			  *PASSNAME       VARCHAR2(120),
			  *CHECK_FLAG     CHAR(1) default '1',
			  PRICE          NUMBER(9,3),
			  DOWN_COUNT     INTEGER,
			  CLICK_SUM      INTEGER,
			  POST_DATE      DATE default sysdate,
			  CHECK_DATE     DATE default sysdate,
			  RES_AUTHOR     VARCHAR2(30),
			  AVAIL_DATE     DATE default sysdate+1000,
			  *RES_STATUS     CHAR(1) default '1',
			  *RES_CONTENT    VARCHAR2(4000),
			  *STORE_FILEPATH VARCHAR2(255),
			  *RES_SIZE       NUMBER(10),
			  CORP_NAME      VARCHAR2(90),
			  COPYRIGHT      VARCHAR2(120),
			  *CREATE_TIME    DATE default sysdate,
			  SEND_FLAG      CHAR(1),
			  *RES_DESC       VARCHAR2(400),
			  EXTERNAL1      VARCHAR2(300),
			  EXTERNAL2      VARCHAR2(300),
			  EXTERNAL3      VARCHAR2(300),
			  DOWN_INTEGRAL  INTEGER default 10,
			  CP_EMP_ID      NUMBER(5),
			  CP_ID          NUMBER(5) default 1 
			*/
			
			String sqlReFolderRes = "insert into wap_re_folder_res(folder_id,res_id,order_index) values(?,?,sq_wap_re_folder_res.nextval)";
			pstResource = conn.prepareStatement(sqlResource);
			List resourceList = folder.getResources();
			long[] ids = getResId(resourceList.size());
			
			for (int i = resourceList.size()-1; i >=0  ; i--) {
				//TODO 按时间倒序排列
				long resId = ids[i];
				
				System.out.println(" ****** wap_resource.res_id:"+resId+" used "+isUsed(resId));
				
				Resource resource = (Resource) resourceList.get(i);
				String path = "";
				if(resource.getResourceType() == ResourceType.PIC){
					path = resource.getResourcePath();
				}
				
				pstResource.setLong(1, resId);
				pstResource.setLong(2, resource.getResourceType());
				pstResource.setString(3, folder.getTitle());
				pstResource.setString(4, folder.getTitle());
				pstResource.setString(5, path);
				pstResource.setLong(6, resource.getResourceContent().length());
				pstResource.setString(7, folder.getTitle());
				date = new Date();
				pstResource.setString(8, sdf.format(date));
				pstResource.setString(9, resource.getResourceContent());
				//Thread.sleep(5000);
				pstResource.addBatch();
			}
			pstResource.executeBatch();

			pstReFolderRes = conn.prepareStatement(sqlReFolderRes);
			for (int j = resourceList.size()-1; j >= 0 ; j--) {
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

			conn.commit();
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
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

	// 6位流水号字符串
	public synchronized String getFolderId(String id) {
		String folderId = null;
		Connection conn = JavaOracle.getConn();
		try {
			//判断是否是9位的
			int length = getFolderIdLength(id);
			if(length == 0){
				folderId = "000001";
			}else if(length == 9){
				folderId = "000001";
			}else if(length == 12){
				String sql = "select lpad(substr(max(folder_id),LENGTH(max(folder_id))-LENGTH(?)+1,LENGTH(max(folder_id)))+1,6,'0') from wap_page_folder where wap_folder_id=? and length(folder_id)=12";
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(1, id);
				pst.setString(2, id);
				ResultSet rs = pst.executeQuery();
				if (rs.next()){
					folderId = rs.getString(1);
					//TODO 栏目去父栏目下六位子栏目，若该父栏目下没有记录，则默认从000001开始
					if(folderId == null){
						folderId = "000001";
					}
				}
				rs.close();
				pst.close();
				conn.close();
			}
			System.out.println("         6位流水号字符串:"+id+"|FID:"+id+"|ID:"+folderId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return folderId;
	}

	public int getFolderIdLength(String wapFolderId){
		int length = 0;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select nvl(max(length(folder_id)),0) from wap_page_folder where wap_folder_id = ?";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, wapFolderId);
			rs = pst.executeQuery();
			if(rs.next()) length = rs.getInt(1);
			
		}catch (Exception e) {
			e.printStackTrace();
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
	
	public boolean isUsed(long id){
		boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement("select count(res_id) from wap_resource where res_id = ?");
			pst.setLong(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				bln = (rs.getInt(1)>0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
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
			if (rs.next()) id = rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
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
			for (int i = length -1; i >=0 ; i--) {
				rs = ps.executeQuery();
				if (rs.next()) primaryKey[i] = rs.getLong(1);
				rs.close();
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		OutputStream os = null;

		GregorianCalendar gcDate = new GregorianCalendar();
		int year = gcDate.get(GregorianCalendar.YEAR);
		int month = gcDate.get(GregorianCalendar.MONTH);
		int day = gcDate.get(GregorianCalendar.DAY_OF_MONTH);
		subDir = year + "/" + (month + 1) + "/" + day + "/";
		
		File f = new File(destImgUrl + subDir);
		if (!f.exists()) f.mkdirs();
		
		try {
			os = new FileOutputStream(destImgUrl + subDir + fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];

			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			path = "/upload/"+subDir;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public String destImgUrl = "/usr/local/jboss-3.2.7/server/default/deploy/spcpnew.war/upload/";
	
	public String subDir = "";

}
