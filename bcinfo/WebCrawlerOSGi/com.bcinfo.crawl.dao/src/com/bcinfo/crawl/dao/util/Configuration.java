/**
 * 
 */
package com.bcinfo.crawl.dao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author dongq
 * 
 *         create time : 2010-4-10 ÉÏÎç10:58:38
 */
public final class Configuration {

	private static Properties config = new Properties();
	
	private static Integer[] DEFAULT = {3,4,5};
	
	static {
		String name = System.getProperty("user.dir") + "/conf/com.bcinfo.crawl.dao.properties";
		try {
			config.load(new FileInputStream(name));
			String[] values = config.getProperty("cai.piao.wapportal_id").split(",");
			DEFAULT = new Integer[values.length];
			for(int index=0; index<values.length; index++) {
				DEFAULT[index] = Integer.parseInt(values[index]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String HTML_FILE_PATH = config.getProperty("html.file.path");
	
	public static final String IMAGE_FILE_PATH = config.getProperty("image.file.path");
	
	public static final String XML_REMOTE_DIR = config.getProperty("xml.remote.dir");
	
	public static final long PARSE_JOB_PERIOD = Long.parseLong(config.getProperty("parse.job.period"));
	
	public static final long SCAN_PERIOD = Long.parseLong(config.getProperty("scan.xml.remote.dir"));
	
	public static final String SRC_HOST_ID = config.getProperty("wap.ftp.queue.src.host.id");
	
	public static final String SRC_ADDR = config.getProperty("wap.ftp.queue.src.addr");
	
	public static final String RES_HOST_ID = config.getProperty("wap.ftp.queue.res.host.id");
	
	public static final String RES_ADDR = config.getProperty("wap.ftp.queue.res.addr");
	
	public static final String STATUS = config.getProperty("wap.ftp.queue.status");
	
	public static final int SERVER_PORT = Integer.parseInt(config.getProperty("socket.server.port"));
	
	public static final String SERVER_ADDRESS = config.getProperty("socket.server.address");
	
	public static final int SERVER_POOL = Integer.parseInt(config.getProperty("socket.server.pool.size"));
	
	public static final Integer[] CAI_PIAO_WAP_PORTAL_IDS = DEFAULT;
}
