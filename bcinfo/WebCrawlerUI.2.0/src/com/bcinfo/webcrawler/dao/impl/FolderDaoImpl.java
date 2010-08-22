/**
 * 
 */
package com.bcinfo.webcrawler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bcinfo.webcrawler.dao.FolderDao;
import com.bcinfo.webcrawler.model.Folder;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午03:47:58
 */
public class FolderDaoImpl implements FolderDao {

	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Folder getRoot() throws Exception {
		final String sql = "select * from wap_site_folder where parent_folder is null";
		return (Folder)jdbcTemplate.queryForObject(sql, new FolderRowMapper());
	}
	
	@SuppressWarnings("unchecked")
	public List<Folder> getAllFolderWithOrder() throws Exception {
		final String sql = "select level,a.folder_id,a.parent_folder from wap_site_folder a where a.parent_folder is not null start with a.folder_id = -1 connect by prior a.folder_id = a.parent_folder";
		return jdbcTemplate.query(sql, new OrderFolderRowMapper());
	}
	
	@SuppressWarnings("unchecked")
	public List<Folder> getFolders(Long parent) throws Exception {
		final String sql = "select * from " + Folder.TABLE_NAME + " where " + Folder.PARENT_FOLDER + " = ? order by " + Folder.FOLDER_ID;
		return jdbcTemplate.query(sql, new Object[]{parent}, new FolderRowMapper());
	}

	public Long getChildCount(Long parent) throws Exception {
		if(parent == null) {
			final String sql = "select count(1) from " + Folder.TABLE_NAME;
			return jdbcTemplate.queryForLong(sql);
		} else {
			final String sql = "select count(1) from " + Folder.TABLE_NAME + " where " + Folder.PARENT_FOLDER + " = ? order by " + Folder.FOLDER_ID;
			return jdbcTemplate.queryForLong(sql, new Object[]{parent});
		}
	}

	public Boolean hasChild(Long parent) throws Exception {
		boolean bln = false;
		
		if(getChildCount(parent) > 0) bln = true;
		
		return bln;
	}
	
	public int getDepth() throws Exception {
		return jdbcTemplate.queryForInt("select max(level) from wap_site_folder a start with a.folder_id = -1 connect by prior a.folder_id = a.parent_folder");
	}
	
	class FolderRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int row) throws SQLException {
			Folder folder = new Folder();
			
			folder.setFolderId(rs.getLong(Folder.FOLDER_ID));
			folder.setParentFolder(rs.getLong(Folder.PARENT_FOLDER));
			folder.setFolderName(rs.getString(Folder.FOLDER_NAME));
			folder.setStatus(rs.getString(Folder.STATUS));
			folder.setFolderLevel(rs.getLong(Folder.FOLDER_LEVEL));
			
			return folder;
		}
	}
	
	class OrderFolderRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int row) throws SQLException {
			Folder folder = new Folder();
			
			folder.setFolderId(rs.getLong(Folder.FOLDER_ID));
			folder.setParentFolder(rs.getLong(Folder.PARENT_FOLDER));
			folder.setFolderName(rs.getString(Folder.FOLDER_NAME));
			folder.setStatus(rs.getString(Folder.STATUS));
			folder.setFolderLevel(rs.getLong("level"));
			
			return folder;
		}
	}
}
