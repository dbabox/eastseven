/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.dao.util.OracleUtil;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.PageFolderBean;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.ResourceBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.Dao;

/**
 * @author dongq
 * 
 *         create time : 2009-12-23 ÏÂÎç03:40:23<br>
 */
public class SpcpDao extends Dao {

	public List<ResourceBean> getFolderResource(String folderId) {
		List<ResourceBean> list = new ArrayList<ResourceBean>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select a.res_id,a.res_type_id,a.firstname,a.passname,nvl(a.res_author,'') res_author,a.res_status,a.res_content,a.store_filepath,a.res_size,to_char(a.create_time,'yyyy/mm/dd hh24:mi:ss') create_time,a.res_desc from wap_resource a where exists(select 1 from wap_re_folder_res b where a.res_id=b.res_id and b.folder_id=?) order by a.res_id asc");
			pst.setString(1, folderId);
			rs = pst.executeQuery();
			ResourceBean bean = null;
			while(rs.next()){
				bean = new ResourceBean();
				bean.setResId(rs.getLong("res_id"));
				bean.setResTypeId(rs.getLong("res_type_id"));
				bean.setFirstName(rs.getString("firstname"));
				bean.setPassName(rs.getString("passname"));
				bean.setResAuthor(rs.getString("res_author"));
				bean.setResStatus(rs.getString("res_status"));
				bean.setResContent(rs.getString("res_content"));
				bean.setStoreFilePath(rs.getString("store_filepath"));
				bean.setResSize(rs.getLong("res_size"));
				bean.setCreateTime(rs.getString("create_time"));
				bean.setResDesc(rs.getString("res_desc"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public List<PageFolderBean> getPageFolderList(String folderId){
		List<PageFolderBean> list = new ArrayList<PageFolderBean>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = OracleUtil.getConnection();
			if(folderId!=null&&!"".equals(folderId)){
				//wap_page_folder where wap_folder_id = ? order by folder_id asc
				pst = conn.prepareStatement("select folder_id,nvl(wap_folder_id,'') wap_folder_id,folder_name,folder_desc,folder_status,to_char(create_time,'yyyy/mm/dd hh24:mi:ss') create_time from (select a.folder_id,a.wap_folder_id,a.folder_name,a.folder_desc,a.folder_status,a.create_time from wap_page_folder a where a.wap_folder_id=? union all select b.folder_id,b.wap_folder_id,b.folder_name,b.folder_desc,b.folder_status,b.create_time from wap_page_folder_spcp b where b.wap_folder_id=?) order by folder_id desc");
				pst.setString(1, folderId);
				pst.setString(2, folderId);
			}else{
				pst = conn.prepareStatement("select folder_id,nvl(wap_folder_id,'') wap_folder_id,folder_name,folder_desc,folder_status,to_char(create_time,'yyyy/mm/dd hh24:mi:ss') create_time from wap_page_folder where wap_folder_id is null order by folder_id asc");
			}
			rs = pst.executeQuery();
			PageFolderBean bean = null;
			while(rs.next()){
				bean = new PageFolderBean();
				bean.setFolderId(rs.getString("folder_id"));
				bean.setWapFolderId(rs.getString("wap_folder_id"));
				bean.setFolderName(rs.getString("folder_name"));
				bean.setFolderDesc(rs.getString("folder_desc"));
				bean.setFolderStatus(rs.getString("folder_status"));
				bean.setCreateTime(rs.getString("create_time"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst, rs);
		}
		
		return list;
	}
	
	public boolean isLeaf(String folderId) {
		boolean isLeaf = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = OracleUtil.getConnection();
			pst = conn.prepareStatement("select count(folder_id) from (select a.folder_id from wap_page_folder a where a.wap_folder_id=? union all select b.folder_id from wap_page_folder_spcp b where b.wap_folder_id=?)");
			pst.setString(1, folderId);
			pst.setString(2, folderId);
			rs = pst.executeQuery();
			if(rs.next()){
				int count = rs.getInt(1);
				if(count==0) isLeaf = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
		return isLeaf;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
