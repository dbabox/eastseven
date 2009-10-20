package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-20 ÉÏÎç11:45:22
 */
public class ChannelMappingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ChannelMappingDao dao;
	
	public ChannelMappingServlet() {
		super();
		dao = new ChannelMappingDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String localCode = request.getParameter("localCode");
		String localChannelId = request.getParameter("localChannelId");
		String channelId = request.getParameter("channelId");
		
		if(!"".equals(channelId)&&!"".equals(localChannelId)){
			boolean bln = dao.save(localCode, localChannelId, channelId);
			System.out.println(localCode+"|"+localChannelId+"|"+channelId+"|"+bln);
		}
		
		response.sendRedirect("channel_mapping.jsp");
	}

}
