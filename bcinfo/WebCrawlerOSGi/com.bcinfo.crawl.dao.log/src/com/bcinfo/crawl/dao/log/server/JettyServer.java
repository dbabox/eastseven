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
 *         create time : 2010-7-13 ����11:11:25
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
			log.info("����Web����������");
		} catch (Exception e) {
			log.warn("����Web����������ʧ��");
			e.printStackTrace();
		}
	}
	
	public static void stop() {
		try {
			server.stop();
			log.info("����Web�������ر�");
		} catch (Exception e) {
			log.warn("����Web�������ر�ʧ��");
			log.warn(e.getMessage());
		}
	}
}
