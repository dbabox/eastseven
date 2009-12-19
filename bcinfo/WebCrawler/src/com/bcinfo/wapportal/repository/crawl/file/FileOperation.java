/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.bcinfo.wapportal.repository.crawl.dao.util.JavaOracle;
import com.bcinfo.wapportal.repository.crawl.domain.CatchConfigInfo;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午09:46:40
 */
public final class FileOperation {

	private static Logger log = Logger.getLogger(FileOperation.class);
	
	public static String log_dir = "E:/bcinfo/内容抓取/logs/";
	
	public final static String catch_list = "E:/bcinfo/内容抓取/catch_list.xls";
	
	String logPath = null;
	
	final static String sheet_name = "sohu";//list
	
	public FileOperation() {
		String os = System.getenv("OS");
		if(os!=null && os.toLowerCase().contains("windows")){
			log_dir = "E:/bcinfo/内容抓取/logs/";
		}else{
			log_dir = "/usr/local/oracle/apache-tomcat-6.0.18/webcrawler/log/";
		}
	}
	
	public Boolean contains(String link){
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " select count(link_log) from twap_public_crawl_link_log where link_log = ? ";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, link);
			rs = pst.executeQuery();
			
			if(rs.next()){
				int count = rs.getInt(1);
				bln = (count>0)?true:false;
			}
		}catch(Exception e){
			e.printStackTrace();
			//报错还是让他爬
			bln = false;
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bln;
	}
	
	public List<String> readLog(String folderId) {
		List<String> logList = null;
		logPath = getLogPath(folderId);
		try{
			File file = new File(logPath);
			if (!file.exists()){
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = null;
			temp = br.readLine();
			logList = new ArrayList<String>();
			while (temp != null) {
				temp = br.readLine();
				logList.add(temp);
			}
			br.close();
		}catch(Exception e){
			//System.out.println("读取"+logPath+"失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return logList;
	}
	
	public List<String> removeDuplicateLinks(String folderId, List<String> links) {
		List<String> urlList = null;
		logPath = getLogPath(folderId);
		try{
			List<String> logList = readLog(logPath);
			if(logList == null) return links;
			urlList = new ArrayList<String>();
			if(links != null && !links.isEmpty()){
				for(String url : links){
					if(!logList.contains(url)) urlList.add(url);
				}
			}
		}catch(Exception e){
			//System.out.println("剔除"+logPath+"中的重复链接失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return urlList;
	}
	
	public Boolean writeLog(String link) {
		Boolean bln = false;
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = " insert into twap_public_crawl_link_log(link_log) values(?)";
		
		try{
			conn = JavaOracle.getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, link);
			pst.executeUpdate();
			conn.commit();
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			//报错无需回滚,因为link_log字段是主键
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bln;
	}
	
	public Boolean writeLog(String folderId, String link) {
		Boolean bln = false;
		FileOutputStream out = null;
		logPath = getLogPath(folderId);
		try {
			File file = new File(logPath);
			out = new FileOutputStream(file, true);
			link += "\n";
			out.write(link.getBytes());
			out.close();
		} catch (Exception e) {
			//System.out.println("向"+logPath+"写入"+link+"失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return bln;
	}
	
	private String getLogPath(String folderId){
		try{
			return log_dir + folderId + ".txt";
		}catch(Exception e){
			//System.out.println(" 获取 "+ folderId + ".txt日志文件失败");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return null;
	}
	
	public static List<CatchConfigInfo> loadCatchListFile() {
		List<CatchConfigInfo> list = null;
		try {
			HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(catch_list));
			HSSFSheet sheet = workBook.getSheet(sheet_name);
			int rowNum = sheet.getLastRowNum();
			if(rowNum <= 1) rowNum += 1;
			list = new ArrayList<CatchConfigInfo>(rowNum - 1);
			for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
				CatchConfigInfo info = new CatchConfigInfo();
				HSSFRow row = sheet.getRow(rowIndex);
				int cellNum = row.getLastCellNum();
				for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) {
					HSSFCell cell = row.getCell(cellIndex);
					if(cell == null) continue;
					switch (cellIndex) {
					case 3:
						info.setFolderId(cell.getStringCellValue().replaceAll("F", ""));
						break;
					case 4:
						info.setUrl(cell.getStringCellValue().trim());
						break;
					case 5:
						info.setUse("是".equals(cell.getStringCellValue().trim()));
						break;
					case 6:
						info.setFetchImage("是".equals(cell.getStringCellValue().trim()));
						break;
					case 7:
						info.setFetchType(cell.getStringCellValue().trim());
						break;
					case 8:
						info.setWebSite(cell.getStringCellValue().trim());
						break;
					case 9:
						info.setNew("是".equals(cell.getStringCellValue().trim()));
						break;
					case 10:
						info.setLogFilePath(cell.getCellType() == Cell.CELL_TYPE_STRING ? cell.getStringCellValue().trim() : null);
						break;
					default:
						break;
					}
				}
				list.add(info);
			}
		} catch (Exception e) {
			//System.out.println(" 配置文件读取失败 ");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return list;
	}
	
	public static List<CatchConfigInfo> getUsableCatchConfigInfoList(){
		List<CatchConfigInfo> usableList = null;
		try{
			List<CatchConfigInfo> list = loadCatchListFile();
			if(list != null && !list.isEmpty()){
				usableList = new ArrayList<CatchConfigInfo>();
				for(int index=0;index<list.size();index++){
					CatchConfigInfo info = (CatchConfigInfo)list.get(index);
					if(info.isUse()) usableList.add(info);
				}
			}
		}catch(Exception e){
			//System.out.println(" 筛选可用配置信息失败 ");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return usableList;
	}
	
	public static List<CatchConfigInfo> getUsableCatchConfigInfoList(List<CatchConfigInfo> list){
		List<CatchConfigInfo> usableList = null;
		try{
			if(list != null && !list.isEmpty()){
				usableList = new ArrayList<CatchConfigInfo>();
				for(int index=0;index<list.size();index++){
					CatchConfigInfo info = (CatchConfigInfo)list.get(index);
					if(info.isUse()) usableList.add(info);
				}
			}
		}catch(Exception e){
			//System.out.println(" 筛选可用配置信息失败 ");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		return usableList;
	}
	
	public static void printList(List<CatchConfigInfo> list){
		if(list != null && !list.isEmpty()){
			int oldCount = 0;
			int newCount = 0;
			for(int index=0;index<list.size();index++){
				CatchConfigInfo info = (CatchConfigInfo)list.get(index);
				if(info.isNew()){
					newCount++;
				}else{
					oldCount++;
				}
				//System.out.println(info);
				
			}
			//System.out.println("old:"+oldCount+"|new:"+newCount);
		}else{
			//System.out.println(" 没有要打印的数据 ");
		}
	}
	
	/**
	 * 保存网页上的图片到本地服务器上
	 * 
	 */
	@Deprecated
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
			path = "/upload/"+subDir+fileName;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}catch(NullPointerException e){
			//System.out.println("下载 "+destImgUrl + subDir + fileName+" 文件不存在");
		}
		return path;
	}

	public String destImgUrl = "/usr/local/jboss-3.2.7/server/default/deploy/spcpnew.war/upload/";
	
	public String subDir = "";
	
}
