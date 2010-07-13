/**
 * 
 */
package com.bcinfo.crawl.dao.log.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * @author dongq
 * 
 *         create time : 2010-7-13 上午11:11:25
 */
public final class JettyServer {

	private static final Log log = LogFactory.getLog(JettyServer.class);
	
	private static Server server = null;
	public static final int PORT = 3501;
	
	synchronized static public Server getServer() {
		if(server == null){
			server = new Server(PORT);
		}
		return server;
	}
	
	public static void start() {
		try {
			getServer();
			ContextHandlerCollection contexts = new ContextHandlerCollection();
			server.setHandler(contexts);
			
			Context root = new Context(contexts, "/", Context.SESSIONS);
			root.addServlet(new ServletHolder(IndexServlet.class), "/index.html");
			root.addServlet(new ServletHolder(CrawlerLogServlet.class), "/crawlerLog.html");
			
			//server.join();
			server.start();
			log.info("内置Web服务器启动");
		} catch (Exception e) {
			log.warn("内置Web服务器启动失败");
			e.printStackTrace();
		}
	}
	
	public static void stop() {
		try {
			server.stop();
			log.info("内置Web服务器关闭");
		} catch (Exception e) {
			log.warn("内置Web服务器关闭失败");
			log.warn(e.getMessage());
		}
	}
}
