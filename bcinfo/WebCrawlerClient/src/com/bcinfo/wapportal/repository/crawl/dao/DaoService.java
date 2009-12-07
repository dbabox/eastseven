/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaInternal;
import com.bcinfo.wapportal.repository.crawl.domain.internal.FileLog;

/**
 * @author dongq
 * 
 *         create time : 2009-11-16 上午10:22:55<br>
 */
public class DaoService {

	private static final Logger log = Logger.getLogger(DaoService.class);

	public synchronized List<String> getAllFileLog() {
		List<String> list = new ArrayList<String>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select file_name from internal_file_log";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("file_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(rs, pst, conn);
		}

		return list;
	}

	public synchronized List<String> getAllFileLog(String flag) {
		List<String> list = new ArrayList<String>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select file_name from internal_file_log where flag = ?";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, flag);
			rs = pst.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("file_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			close(rs, pst, conn);
		}

		return list;
	}
	
	/**
	 * 批量保存
	 * 
	 * @param list
	 * @return
	 */
	public synchronized Boolean batchSaveInternalFileLogs(List<FileLog> list) {
		boolean bln = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "insert into internal_file_log(file_name,create_time,flag) values(?,CURDATE(),?)";

		try {
			conn = JavaInternal.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);

			for (FileLog fileLog : list) {
				pst.setString(1, fileLog.getFileName());
				pst.setString(2, fileLog.getFlag());
				pst.addBatch();
			}
			pst.executeBatch();

			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}
		return bln;
	}

	/**
	 * 保存
	 * 
	 * @param fileLog
	 * @return
	 */
	public synchronized Boolean saveInternalFileLog(FileLog fileLog) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "insert into internal_file_log(file_name,create_time,flag) values(?,CURDATE(),?)";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, fileLog.getFileName());
			pst.setString(2, fileLog.getFlag());
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}

	public synchronized Boolean saveInternalFileLog(String fileName, String flag) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "insert into internal_file_log(file_name,create_time,flag) values(?,CURDATE(),?)";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, fileName);
			pst.setString(2, flag);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}

	public synchronized Boolean deleteInternalFileLog() {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "delete from internal_file_log where flag = ? and create_time < ?";

		try {
			
			Date date = new Date(System.currentTimeMillis()-24*60*60*1000);
			
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, "1");
			pst.setDate(2, date);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}
	
	public synchronized Boolean deleteInternalFileLog(String fileName) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "delete from internal_file_log where file_name = ?";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, fileName);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}
	
	public synchronized Boolean deleteInternalFileLog(List<String> list) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "delete from internal_file_log where file_name = ?";

		try {
			if(list!=null&&!list.isEmpty()){
				conn = JavaInternal.getConnection();
				conn.setAutoCommit(false);
				pst = conn.prepareStatement(sql);
				for(String fileName : list){
					pst.setString(1, fileName);
					pst.addBatch();
				}
				pst.executeBatch();
				conn.setAutoCommit(true);
				conn.commit();
				bln = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}

	public synchronized Boolean updateInternalFileLog(String fileName,
			String flag) {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "update internal_file_log set flag = ? where file_name = ?";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, flag);
			pst.setString(2, fileName);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			rollback(conn);
		} finally {
			close(rs, pst, conn);
		}

		return bln;
	}

	/**
	 * 创建internal_file_log表
	 * 
	 * @return
	 */
	public Boolean createInternalFileLogTable() {
		boolean bln = false;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		// 0:失败; 1:处理中
		String sql = " create table internal_file_log(file_name varchar(1024) not null primary key,create_time date not null, flag char(1) not null) ";

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement(sql);
			pst.execute();
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			log.error("创建internal_file_log表出错");
		} finally {
			close(rs, pst, conn);
		}
		return bln;
	}

	/**
	 * 查询指定表是否存在
	 * 
	 * @param tableName
	 * @return true ：存在<br>
	 *         false：不存在<br>
	 */
	public Boolean isTableExist(String tableName) {
		boolean bln = true;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = JavaInternal.getConnection();
			pst = conn.prepareStatement("select 1 from internal_file_log");
			log.info(tableName+" 已存在");
		} catch (Exception e) {
			bln = false;
			log.info(tableName+" 不存在");
			//e.printStackTrace();
			//log.error(e);
			//log.error("查询指定表是否存在出错");
		} finally {
			close(rs, pst, conn);
		}
		return bln;
	}

	void rollback(Connection conn) {
		try {
			if (conn != null)
				conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param rs
	 * @param pst
	 * @param conn
	 */
	void close(ResultSet rs, PreparedStatement pst, Connection conn) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(e);
			}
		if (pst != null)
			try {
				pst.close();
			} catch (SQLException e) {
				log.error(e);
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
	}

	public static void main(String[] args) {
		DaoService dao = new DaoService();
		boolean bln = dao.isTableExist("internal_file_log");
		System.out.println(" table is exist : " + bln);
	}

}
