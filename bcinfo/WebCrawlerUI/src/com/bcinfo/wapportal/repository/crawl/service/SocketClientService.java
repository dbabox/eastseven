/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.FtpConfigDao;

/**
 * @author dongq
 * 
 *         create time : 2010-4-21 上午09:09:55<br>
 */
public final class SocketClientService {

	private static final Logger log = Logger.getLogger(SocketClientService.class);

	public static Socket getSocket(String host, int port) throws Exception {
		log.debug("取得Socket对象");
		return new Socket(host, port);
	}
	
	public boolean send(File file, String localCode) {
		FtpConfigDao dao = new FtpConfigDao();
		Map<String, String> ftp = dao.getFtpConfig(localCode);
		return send(file, ftp.get("ftp_host"), 10000);
	}
	
	public boolean send(File file, String host, int port) {
		boolean isSuccess = false;
		Socket client = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			
			client = SocketClientService.getSocket(host, port);
			log.info("连接建立...");
			in = client.getInputStream();
			out = client.getOutputStream();
			
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[1024];
			int size;
			while((size = reader.read(buffer))!=-1) {
				out.write(buffer, 0, size);
			}
			
			reader.close();
			in.close();
			out.close();
			log.info(file.getName()+" 发送完毕");
			isSuccess = true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return isSuccess;
	}
}
