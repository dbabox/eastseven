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
 *         create time : 2009-10-18 ÉÏÎç11:28:42
 */
public class CrawlListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String method;
	private CrawlListDao dao;
	
	public CrawlListServlet() {
		super();
		dao = new CrawlListDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		method = request.getParameter("method");
		if("init".equals(method)){
			request.setAttribute("crawlList", dao.getAllCrawlList());
			request.getRequestDispatcher("crawl_list.jsp").forward(request, response);
		}
		if("list".equals(method)){
			list(request, response);
		}else if("add".equals(method)){
			add(request, response);
		}
	}

	//
	void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channelId = request.getParameter("channelId");
		String url = request.getParameter("url");
		
		Boolean bln = dao.saveCrawlList(Long.valueOf(channelId), url);
		if(bln)
			request.getRequestDispatcher("/CrawlListServlet?method=init").forward(request, response);
	}
}
