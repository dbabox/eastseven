package com.bcinfo.crawl.dao.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.bcinfo.crawl.dao.log.database.DatabaseConnection;
import com.bcinfo.crawl.dao.log.service.CrawlerDataAccessService;
import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.log.service.impl.CrawlerDataAccessServiceImpl;
import com.bcinfo.crawl.dao.log.service.impl.CrawlerLogServiceImpl;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	private ServiceRegistration sr = null;
	
	public void start(BundleContext context) throws Exception {
		init();
		sr = context.registerService(CrawlerLogService.class.getName(), new CrawlerLogServiceImpl(), null);
		log.info(CrawlerLogService.class.getName() + "发布");
		context.registerService(CrawlerDataAccessService.class.getName(), new CrawlerDataAccessServiceImpl(), null);
		log.info(CrawlerDataAccessService.class.getName() + "发布");
	}

	public void stop(BundleContext context) throws Exception {
		if(sr != null) {
			sr.unregister();
			log.info(CrawlerLogService.class.getName() + "注销");
			log.info(CrawlerDataAccessService.class.getName() + "注销");
		}
	}

	private void init() {
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement pst = null;
		String sql = "SELECT * FROM CRAWLER_LOG";
		boolean isExist = false;
		try {
			pst = conn.prepareStatement(sql);
			log.info("查询抓取日志表:" + pst.executeQuery());
			isExist = true;
		} catch (Exception e) {
			log.info("抓取日志数据库不存在");
		}
		
		if(!isExist) {
			sql = "";
			Scanner scanner = new Scanner(getClass().getResourceAsStream("table.sql")).useDelimiter("\r");
			while(scanner.hasNext()) {
				sql += scanner.next();
			}
			String[] sqls = sql.split(";");
			try {
				for(String _sql : sqls) {
					log.info(_sql);
					pst = conn.prepareStatement(_sql);
					log.info("创建抓取日志数据库及相关表:" + pst.executeUpdate());
					conn.commit();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			log.info("抓取日志数据库已存在");
		}
		
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
