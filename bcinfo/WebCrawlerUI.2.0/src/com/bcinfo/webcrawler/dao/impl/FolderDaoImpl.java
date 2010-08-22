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
	
	@SuppressWarnings("unchecked")
	public List<Folder> getFolders(Long parent) throws Exception {
		if(parent == null) {
			final String sql = "select * from " + Folder.TABLE_NAME;
			return jdbcTemplate.query(sql, new FolderRowMapper());
		} else {
			final String sql = "select * from " + Folder.TABLE_NAME + " where " + Folder.PARENT_FOLDER + " = ? order by " + Folder.FOLDER_ID;
			return jdbcTemplate.query(sql, new Object[]{parent}, new FolderRowMapper());
		}
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
}
