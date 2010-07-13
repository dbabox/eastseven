/**
 * 
 */
package com.bcinfo.crawl.dao.log.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bcinfo.crawl.dao.log.service.CrawlerLogMonitor;
import com.bcinfo.crawl.dao.log.service.impl.CrawlerLogMonitorImpl;

/**
 * @author dongq
 * 
 *         create time : 2010-7-13 ÉÏÎç11:31:42
 */
public class CrawlerLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(CrawlerLogServlet.class);
	private CrawlerLogMonitor monitor = null;
	
	public CrawlerLogServlet() {
		monitor = new CrawlerLogMonitorImpl();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		log.info("request parameter method is " + method);
		if("list".equalsIgnoreCase(method)) {
			list(req, resp);
		} else if("del".equalsIgnoreCase(method)) {
			del(req, resp);
		} else {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("<strong>404</strong>");
			resp.getWriter().write("<br />" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
	
	void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String channelIdString = req.getParameter("channelId");
		long channelId = -1;
		if(StringUtils.isNotEmpty(channelIdString)) {
			channelId = Long.parseLong(channelIdString);
		}
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(channelId == -1) list = monitor.getAllCrawlerLogs();
		else list = monitor.getCrawlerLogs(channelId);
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = resp.getWriter();
		String html = "";
		if(!list.isEmpty()) {
			for(Map<String, String> crawlerLog : list) {
				html += "<li>";
				html += crawlerLog.get("channelId") + " ";
				html += crawlerLog.get("date") + " ";
				html += crawlerLog.get("url");
				html += "</li>";
			}
		}
		writer.print("<br /><a href='index.html'>back</a>");
		writer.print(html);
		writer.print("<br /><a href='index.html'>back</a>");
		if(writer != null) writer.close();
	}
	
	void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String channelIdString = req.getParameter("channelId");
		long channelId = -1;
		if(StringUtils.isNotEmpty(channelIdString)) {
			channelId = Long.parseLong(channelIdString);
		}
		boolean bln = false;
		if(channelId == -1) {
			List<String> channelIds = monitor.getChannelIds();
			if(!channelIds.isEmpty()) {
				for(String _channelId : channelIds) {
					bln &= monitor.clearCrawlerLogs(Long.parseLong(_channelId));
				}
			}
		} else {
			bln = monitor.clearCrawlerLogs(channelId);
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = resp.getWriter();
		writer.print("<br />clear channelId " + channelId + " " + bln);
		writer.print("<br /><a href='index.html'>back</a>");
		if(writer != null) writer.close();
	}
}
