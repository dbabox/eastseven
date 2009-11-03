/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.CLOB;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;

/**
 * @author dongq
 * 
 *         create time : 2009-10-28 ����09:29:08
 */
public class DBTest {

	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			conn = JavaOracle.getConn();
			DatabaseMetaData metaData = conn.getMetaData();
			//System.out.println(" DatabaseMajorVersion  : "+metaData.getDatabaseMajorVersion());
			//System.out.println(" DatabaseMinorVersion  : "+metaData.getDatabaseMinorVersion());
			System.out.println(" DatabaseProductName   : "+metaData.getDatabaseProductName());
			System.out.println(" DatabaseProductVersion: "+metaData.getDatabaseProductVersion());
			System.out.println(" DriverMajorVersion    : "+metaData.getDriverMajorVersion());
			System.out.println(" DriverMinorVersion    : "+metaData.getDriverMinorVersion());
			System.out.println(" DriverName            : "+metaData.getDriverName());
			System.out.println(" DriverVersion         : "+metaData.getDriverVersion());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	void test(){
		String sql = "insert into twap_public_crawl_resource(res_id,res_text) values(seq_twap_public_crawl_resource.nextval,?)";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cnt = "�л����񹲺͹�������������";
		try {

			conn = JavaOracle.getConn();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("select res_text from twap_public_crawl_resource where res_id = 12941 for update");
			//12941
			rs = pst.executeQuery();
			if(rs.next()){
				CLOB clob = (CLOB)rs.getClob("res_text");
				BufferedWriter out = new BufferedWriter(clob.getCharacterOutputStream());
				BufferedReader in = new BufferedReader(new StringReader(cnt));
				int count = 0;
				char[] cbuf = new char[1024];
				while((count = in.read(cbuf))!=-1){
					out.write(cbuf, 0, count);
				}
				in.close();
				out.close();
				pst = conn.prepareStatement("update twap_public_crawl_resource set res_text = ? where res_id = 12941");
				pst.setClob(1, clob);
				pst.execute();
			}

			conn.commit();
			System.out.println("clob restore ok...");
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pst != null)
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}