package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelDao;
import com.bcinfo.wapportal.repository.crawl.domain.Channel;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-23 下午01:17:25
 */
public class ChannelManagementServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ChannelManagementServlet.class);
	
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
			list(request, response);
		}else if("add".equals(method)){
			add(request, response);
		}
	}

	protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channelId = request.getParameter("channelId");
		
		List<Channel> channelList = dao.getChannels(Long.parseLong(channelId));
		request.setAttribute("channelList", channelList);
		request.setAttribute("channelId", channelId);
		request.getRequestDispatcher("channel_management_main.jsp").forward(request, response);
	}
	
	protected void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channelId = request.getParameter("channelId");
		String channelName = request.getParameter("channelName");
		if(channelId ==null || "".equals(channelId) || "null".equals(channelId)) channelId = "0";
		boolean bln = dao.saveChannel(Long.parseLong(channelId), channelName);
		log.info("添加频道["+channelName+"]"+(bln?"成功":"失败"));
		request.setAttribute("channelList", dao.getChannels(Long.parseLong(channelId)));
		request.setAttribute("channelId", channelId);
		request.getRequestDispatcher("channel_management_main.jsp").forward(request, response);
	}
}
