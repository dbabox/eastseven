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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.DaoService;
import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.Folder;
import com.bcinfo.wapportal.repository.crawl.domain.Resource;

/**
 * @author dongq
 * 
 *         create time : 2009-9-9 ����03:40:57<br>
 *         ���ݿ����<br>
 */
public final class OperationDB {

	private static final Logger log = Logger.getLogger(OperationDB.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

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
			System.out.println(" û��ȡ�������ļ���ֵ ");
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean patchSave(List folders) throws Exception {
		boolean isSuccess = false;
		if (folders == null) throw new Exception("���ݼ�Ϊ��");
		DaoService dao = new DaoService();
		try {
			for (int i = 0; i < folders.size(); i++) {
				Folder folder = (Folder) folders.get(i);
				/*
				boolean bln = isExsit(folder);
				log.info(folder.getTitle()+" is exsit :"+bln+"|operation:"+folder.getOperation());
				if(String.valueOf(Folder.UPDATE).equals(folder.getOperation())){
					//TODO 050001��������,һ������Ŀֻ����һ����¼
					bln = update(folder);
					log.info("������Ŀ["+folder.getId()+"]["+folder.getTitle()+"]"+bln);
				}else if(String.valueOf(Folder.INSERT).equals(folder.getOperation())){
					if(bln) continue;//�����Ѿ����ڵļ�¼
				}
				*/
				boolean bln = save(folder);
				log.info("�����Ŀ["+folder.getId()+"]["+folder.getTitle()+"]"+bln);
				
				String fileName = folder.getResFileName();
				if (bln) {
					if (dir != null) {
						File file = new File(dir + fileName);
						System.out.println("�ļ���" + dir + fileName);
						if (file.exists()) {
							log.info("�ļ���" + dir + fileName + " ɾ����" + file.delete());
						} else {
							log.info("�ļ���" + dir + fileName + " ������");
						}
					}
					dao.deleteInternalFileLog(fileName);
					log.info(folder.getTitle() + " ���ɹ� ");
				} else{
					dao.updateInternalFileLog(fileName, "0");
					log.info(folder.getTitle() + " ���ʧ�� ");
				}
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			log.error("��������ʧ��");
		}

		return isSuccess;
	}

	@Deprecated
	public boolean isExsit(Folder folder) throws Exception{
		boolean bln = false;//Ĭ���ǲ����ڵ�
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(1) from wap_page_folder where wap_folder_id = ? and folder_name = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, folder.getId());
			pst.setString(2, folder.getTitle());
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
	
	//�������ռ����ֻ������������
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
				log.info("����ռ������Ŀ["+folder.getId()+"]���ݸ������");
			}else{
				log.info("����ռ������Ŀ["+folder.getId()+"]���ݸ���ʧ��");
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
			//Date date = new Date();
			String wapFolderId = folder.getId();
			String folderId = wapFolderId + getFolderId(wapFolderId);// 12λ
			String sqlFolder = "insert into wap_page_folder_spcp(folder_id,wap_folder_id,folder_name,folder_index,folder_status,folder_desc,folder_level,create_time) ";
			sqlFolder += " values(?,?,?,0,0,?,3,sysdate) ";
			pstFolder = conn.prepareStatement(sqlFolder);
			pstFolder.setString(1, folderId);
			pstFolder.setString(2, wapFolderId);
			pstFolder.setString(3, folder.getTitle());
			pstFolder.setString(4, folder.getTitle());
			//pstFolder.setString(5, sdf.format(date));
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
			sqlResource += " values(?,1,?,?,?,'0','0',?,?,?,sysdate,?,0,0,'web crawler',to_char(sysdate,'yy/mm/dd hh24:mi:ss'),'web crawler') ";

			String sqlReFolderRes = "insert into wap_re_folder_res(folder_id,res_id,order_index) values(?,?,sq_wap_re_folder_res.nextval)";
			pstResource = conn.prepareStatement(sqlResource);
			List resourceList = folder.getResources();
			long[] ids = getResId(resourceList.size());

			for (int i = resourceList.size() - 1; i >= 0; i--) {
				// TODO ��ʱ�䵹������
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
				pstResource.setString(5, path);
				pstResource.setLong(6, resource.getResourceContent().length());
				pstResource.setString(7, folder.getTitle());
				//date = new Date();
				//pstResource.setString(8, sdf.format(date));
				pstResource.setString(8, resource.getResourceContent());
				// Thread.sleep(5000);
				pstResource.addBatch();
			}
			pstResource.executeBatch();

			pstReFolderRes = conn.prepareStatement(sqlReFolderRes);
			for (int j = resourceList.size() - 1; j >= 0; j--) {
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

	// 6λ��ˮ���ַ���
	public synchronized String getFolderId(String id) {
		String folderId = null;
		Connection conn = JavaOracle.getConn();
		try {
			// �ж��Ƿ���9λ��
			int length = getFolderIdLength(id);
			if (length == 0) {
				folderId = "000001";
			} else if (length == 9) {
				folderId = "000001";
			} else if (length == 12) {
				String sql = "select lpad(substr(max(folder_id),LENGTH(max(folder_id))-LENGTH(?)+1,LENGTH(max(folder_id)))+1,6,'0') ";//"from wap_page_folder where wap_folder_id=? and length(folder_id)=12";
				sql += " from( ";
				sql += " select a.folder_id from wap_page_folder a where a.wap_folder_id = ? and length(a.folder_id)=12 ";
				sql += " union ";
				sql += " select b.folder_id from wap_page_folder_spcp b where b.wap_folder_id = ? and length(b.folder_id)=12 ";
				sql += " ) c ";
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(1, id);
				pst.setString(2, id);
				pst.setString(3, id);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					folderId = rs.getString(1);
					// TODO ��Ŀȥ����Ŀ����λ����Ŀ�����ø���Ŀ��û�м�¼����Ĭ�ϴ�000001��ʼ
					if (folderId == null) {
						folderId = "000001";
					}
				}
				rs.close();
				pst.close();
				conn.close();
			}
			log.info("         6λ��ˮ���ַ���:" + id + "|FID:" + id + "|ID:" + folderId);

		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e);
		}
		return folderId;
	}

	public int getFolderIdLength(String wapFolderId) {
		int length = 0;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";//"select nvl(max(length(folder_id)),0) from wap_page_folder where wap_folder_id = ?";
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
			for (int i = length - 1; i >= 0; i--) {
				rs = ps.executeQuery();
				if (rs.next())
					primaryKey[i] = rs.getLong(1);
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
	 * ������ҳ�ϵ�ͼƬ�����ط�������
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
			log.info("ͼƬ������:" + destImgUrl + subDir + fileName);
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
				filePath += "pa_file_"+sdf.format(new Date(System.currentTimeMillis()))+"."+fileSuffix;
				//if(!file.exists()) file.createNewFile();
				file = new File(filePath);
				os = new FileOutputStream(file);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];

				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				log.info("�ļ����غ�����"+filePath);
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
	//�ض���
	//http://www.moxiu.com/down.html?rid=5968872&file=theme/sisdd/f4/bbe/f4bbeccf/moxiu1259849728.sis
	public static void main(String[] args) {
		try{
			URL url = new URL("http://www.izhuti.com/download.php?id=46639");
			System.out.println("content:"+url.getContent());
			System.out.println("default port:"+url.getDefaultPort());
			System.out.println("file:"+url.getFile());
			System.out.println("host:"+url.getHost());
			System.out.println("path:"+url.getPath());
			System.out.println("protocol:"+url.getProtocol());
			System.out.println("query:"+url.getQuery());
			System.out.println("ref:"+url.getRef());
			System.out.println("userInfo:"+url.getUserInfo());
			System.out.println(" ---------------- URL Connection -------------------- ");
			URLConnection conn = url.openConnection();
			System.out.println("content type:"+conn.getContentType());
			System.out.println("content:"+conn.getContent());
			Map<String, List<String>> map = conn.getHeaderFields();
			if(map!=null&&!map.isEmpty()){
				Set<String> keySet = map.keySet();
				for(String key : keySet){
					System.out.println(key+":"+map.get(key));
				}
			}
			System.out.println(" ---------------- HTTP URL Connection -------------------- ");
			//HttpURLConnection httpConn = (HttpURLConnection)conn.getContent();
			//HttpURLConnection
			//InputStream in = url.openStream();
			//System.out.println(" inputstream : "+in);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
