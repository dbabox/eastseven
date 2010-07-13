/**
 * 
 */
package com.bcinfo.crawl.dao.log.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.log.service.CrawlerLogMonitor;
import com.bcinfo.crawl.dao.log.service.impl.CrawlerLogMonitorImpl;

/**
 * @author dongq
 * 
 *         create time : 2010-5-14 ÏÂÎç02:36:24
 */
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(IndexServlet.class);
	private CrawlerLogMonitor monitor = null;
	
	public IndexServlet() {
		monitor = new CrawlerLogMonitorImpl();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("http index");
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().write("<strong>Ê×Ò³</strong>");
		resp.getWriter().write("<br />" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		resp.getWriter().write("<br />");
		resp.getWriter().write("<br /><a href='crawlerLog.html?method=list&channelId='>all</a>  ");
		resp.getWriter().write("  <a href='crawlerLog.html?method=del&channelId='>delete</a>");
		List<String> channelIds = monitor.getChannelIds();
		if(!channelIds.isEmpty()) {
			for(String channelId : channelIds) {
				String link = "<br />";
				link += "<a href='crawlerLog.html?method=list&channelId="+channelId+"'>"+channelId+"</a>  ";
				link += "  <a href='crawlerLog.html?method=del&channelId="+channelId+"'>delete</a>";
				resp.getWriter().write(link);
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
