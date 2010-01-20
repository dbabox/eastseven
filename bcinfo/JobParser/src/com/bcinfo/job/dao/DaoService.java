/**
 * 
 */
package com.bcinfo.job.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bcinfo.job.model.JobBean;

/**
 * @author dongq
 * 
 *         create time : 2009-12-10 ÉÏÎç09:36:17
 */
public class DaoService {

	private static final Logger log = Logger.getLogger(DaoService.class);

	public static String driver = "oracle.jdbc.driver.OracleDriver";
	public static String url = "jdbc:oracle:thin:@127.0.0.1:1521:ZK";
	public static String user = "wap";
	public static String password = "wap";
	public static int batch_size = 10000;

	public DaoService() {
		try {
			String path = System.getProperties().getProperty("user.dir") + "/conf/config.properties";
			Properties property = new Properties();
			property.load(new FileInputStream(path));
			
			driver = property.getProperty("db.driver");
			url = property.getProperty("db.url");
			user = property.getProperty("db.user");
			password = property.getProperty("db.password");
			batch_size = Integer.valueOf(property.getProperty("batch.size"));
			
			log.info("[driver:" + driver + "][url:" + url + "][" + user + "/" + password + "][batch.size:"+batch_size+"]");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	public Boolean saveBatch(List<JobBean> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst =null;
		ResultSet rs = null;
		String sql = "insert into WAP_JOB_INFO(JOB_ID/*1*/,JOB_GSDQ/*2*/,JOB_SSHY/*3*/,JOB_GSMC/*4*/,JOB_LXDH/*5*/,JOB_XXDZ/*6*/,JOB_YB/*7*/,JOB_SJ/*8*/,JOB_LXR/*9*/,JOB_DZYX/*10*/,JOB_GSJJ/*11*/,JOB_ZWMC/*12*/,JOB_ZWMS/*13*/,JOB_ZYYQ/*14*/,JOB_XLYQ/*15*/,JOB_GZJY/*16*/,JOB_XBYQ/*17*/,JOB_SFJZ/*18*/,JOB_YXFW/*19*/,JOB_ZPRS/*20*/,JOB_GZDD/*21*/,JOB_KSRQ/*22*/,JOB_JSRQ/*23*/,JOB_SFTJ/*24*/,JOB_SFRM/*25*/,JOB_TJDY/*26*/,JOB_LOGO/*27*/) values(?/*JOB_ID*/,?/*JOB_GSDQ*/,?/*JOB_SSHY*/,?/*JOB_GSMC*/,?/*JOB_LXDH*/,?/*JOB_XXDZ*/,?/*JOB_YB*/,?/*JOB_SJ*/,?/*JOB_LXR*/,?/*JOB_DZYX*/,?/*JOB_GSJJ*/,?/*JOB_ZWMC*/,?/*JOB_ZWMS*/,?/*JOB_ZYYQ*/,?/*JOB_XLYQ*/,?/*JOB_GZJY*/,?/*JOB_XBYQ*/,?/*JOB_SFJZ*/,?/*JOB_YXFW*/,?/*JOB_ZPRS*/,?/*JOB_GZDD*/,?/*JOB_KSRQ*/,?/*JOB_JSRQ*/,?/*JOB_SFTJ*/,?/*JOB_SFRM*/,?/*JOB_TJDY*/,?/*JOB_LOGO*/)";
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.clearBatch();
			int count = 1;
			for(JobBean job : list){
				pst.setObject(1, job.getJobId());
				pst.setObject(2, job.getJobGsdq());
				pst.setObject(3, job.getJobSshy());
				pst.setObject(4, job.getJobGsmc());
				pst.setObject(5, job.getJobLxdh());
				pst.setObject(6, job.getJobXxdz());
				pst.setObject(7, job.getJobYb());
				pst.setObject(8, job.getJobSj());
				pst.setObject(9, job.getJobLxr());
				pst.setObject(10, job.getJobDzyx());

				pst.setObject(11, job.getJobGsjj());
				pst.setObject(12, job.getJobZwmc());
				pst.setObject(13, job.getJobZwms());
				pst.setObject(14, job.getJobZyyq());
				pst.setObject(15, job.getJobXlyq());
				pst.setObject(16, job.getJobGzjy());
				pst.setObject(17, job.getJobXbyq());
				pst.setObject(18, job.getJobSfjz());
				pst.setObject(19, job.getJobYxfw());
				pst.setObject(20, job.getJobZprs());

				pst.setObject(21, job.getJobGzdd());
				pst.setObject(22, new Date(job.getJobKsrq().getTime()));
				pst.setObject(23, new Date(job.getJobJsrq().getTime()));
				pst.setObject(24, job.getJobSftj());
				pst.setObject(25, job.getJobSfrm());
				pst.setObject(26, job.getJobTjdy());
				pst.setObject(27, job.getJobLogo());
				
//				if(count%batch_size==0){
//					pst.addBatch();
//					count = 1;
//				}else if(list.size() < batch_size){
//					pst.addBatch();
//				}
				pst.addBatch();
				count++;
			}
			int[] batch = pst.executeBatch();
			if(batch!=null){
				log.info(" execute batch :"+batch.length);
			}
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.info("insert batch fail");
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}

	public Boolean updateBatch(List<JobBean> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst =null;
		ResultSet rs = null;
		String sql = "update WAP_JOB_INFO set JOB_GSDQ=?/*2*/,JOB_SSHY=?/*3*/,JOB_GSMC=?/*4*/,JOB_LXDH=?/*5*/,JOB_XXDZ=?/*6*/,JOB_YB=?/*7*/,JOB_SJ=?/*8*/,JOB_LXR=?/*9*/,JOB_DZYX=?/*10*/,JOB_GSJJ=?/*11*/,JOB_ZWMC=?/*12*/,JOB_ZWMS=?/*13*/,JOB_ZYYQ=?/*14*/,JOB_XLYQ=?/*15*/,JOB_GZJY=?/*16*/,JOB_XBYQ=?/*17*/,JOB_SFJZ=?/*18*/,JOB_YXFW=?/*19*/,JOB_ZPRS=?/*20*/,JOB_GZDD=?/*21*/,JOB_KSRQ=?/*22*/,JOB_JSRQ=?/*23*/,JOB_SFTJ=?/*24*/,JOB_SFRM=?/*25*/,JOB_TJDY=?/*26*/,JOB_LOGO=?/*27*/ where JOB_ID = ?";
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.clearBatch();
			int count = 1;
			for(JobBean job : list){
				
				pst.setObject(1, job.getJobGsdq());
				pst.setObject(2, job.getJobSshy());
				pst.setObject(3, job.getJobGsmc());
				pst.setObject(4, job.getJobLxdh());
				pst.setObject(5, job.getJobXxdz());
				pst.setObject(6, job.getJobYb());
				pst.setObject(7, job.getJobSj());
				pst.setObject(8, job.getJobLxr());
				pst.setObject(9, job.getJobDzyx());

				pst.setObject(10, job.getJobGsjj());
				pst.setObject(11, job.getJobZwmc());
				pst.setObject(12, job.getJobZwms());
				pst.setObject(13, job.getJobZyyq());
				pst.setObject(14, job.getJobXlyq());
				pst.setObject(15, job.getJobGzjy());
				pst.setObject(16, job.getJobXbyq());
				pst.setObject(17, job.getJobSfjz());
				pst.setObject(18, job.getJobYxfw());
				pst.setObject(19, job.getJobZprs());

				pst.setObject(20, job.getJobGzdd());
				pst.setObject(21, new Date(job.getJobKsrq().getTime()));
				pst.setObject(22, new Date(job.getJobJsrq().getTime()));
				pst.setObject(23, job.getJobSftj());
				pst.setObject(24, job.getJobSfrm());
				pst.setObject(25, job.getJobTjdy());
				pst.setObject(26, job.getJobLogo());
				
				pst.setObject(27, job.getJobId());

				pst.addBatch();
				count++;
			}
			int[] batch = pst.executeBatch();
			if(batch!=null){
				log.info(" execute batch :"+batch.length);
			}
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.info("update batch fail");
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Boolean deleteBatch(List<JobBean> list){
		boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst =null;
		ResultSet rs = null;
		String sql = "delete WAP_JOB_INFO where JOB_ID = ?";
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.clearBatch();
			int count = 1;
			for(JobBean job : list){
				pst.setObject(1, job.getJobId());
				pst.addBatch();
				count++;
			}
			int[] batch = pst.executeBatch();
			if(batch!=null){
				log.info(" execute batch :"+batch.length);
			}
			conn.setAutoCommit(true);
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			log.info("delete batch fail");
			rollback(conn);
		}finally{
			close(conn, pst, rs);
		}
		
		return bln;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return conn;
	}

	void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pst != null) {
			try {
				pst.close();
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
}
