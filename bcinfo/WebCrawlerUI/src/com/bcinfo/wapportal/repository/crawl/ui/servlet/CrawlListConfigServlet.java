package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcinfo.wapportal.repository.crawl.dao.CrawlListDao;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-23 ÏÂÎç04:25:27
 */
public class CrawlListConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CrawlListDao dao;
	public CrawlListConfigServlet() {
		super();
		dao = new CrawlListDao();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String[] ids = request.getParameterValues("crawlId");
		boolean bln = false;
		if("add".equals(type)){
			String channelId = request.getParameter("channelId");
			String crawlUrl = request.getParameter("crawlUrl");
			bln = dao.saveCrawlList(Long.parseLong(channelId), crawlUrl);
		}else if("start".equals(type)){
			bln = dao.updateBatch(ids, "1");
		}else if("stop".equals(type)){
			bln = dao.updateBatch(ids, "0");
		}
		System.out.println(type+"²Ù×÷£º"+bln);
		response.sendRedirect("crawl_list.jsp");
	}

}
