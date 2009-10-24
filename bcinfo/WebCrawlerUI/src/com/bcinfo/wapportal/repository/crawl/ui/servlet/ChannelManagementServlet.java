package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelDao;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-23 ÏÂÎç01:17:25
 */
public class ChannelManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String method;
	private ChannelDao dao;
	
	public ChannelManagementServlet() {
		super();
		dao = new ChannelDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		method = request.getParameter("method");
		
		if("list".equals(method)){
			
		}else if("add".equals(method)){
			add(request, response);
		}
	}

	protected void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channelPid = request.getParameter("channelPid");
		String channelName = request.getParameter("channelName");
		
		boolean bln = dao.saveChannel(Long.parseLong(channelPid), channelName);
		
		request.getRequestDispatcher("channel_management.jsp").forward(request, response);
	}
}
