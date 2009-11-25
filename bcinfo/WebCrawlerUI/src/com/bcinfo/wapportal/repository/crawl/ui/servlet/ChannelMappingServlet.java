package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao;
import com.bcinfo.wapportal.repository.crawl.dao.UserDao;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-20 上午11:45:22
 */
public class ChannelMappingServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ChannelMappingServlet.class);
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
		Long userId = (Long)request.getSession().getAttribute("userId");
		String localCode = new UserDao().getUserIdLocal(userId); 
		String localChannelId = request.getParameter("localChannelId");
		String channelId = request.getParameter("channelId");
		String type = request.getParameter("type");
		
		if("add".equals(type)){
			if(!"".equals(channelId)&&!"".equals(localChannelId)){
				boolean bln = dao.save(userId, localCode, localChannelId, channelId);
				log.info("用户["+userId+"]订购："+localCode+"|"+localChannelId+"|"+channelId+"|"+bln);
			}
			response.sendRedirect("channel_mapping.jsp");
		}else if("del".equals(type)){
			String[] mappingId = request.getParameterValues("mappingId");
			boolean bln = dao.deleteBatch(userId, mappingId);
			log.info("用户["+userId+"]退订:"+bln);
			response.sendRedirect("channel_mapping.jsp");
		}else if("".equals(type)){
			
		}
		
		
	}

	
}
