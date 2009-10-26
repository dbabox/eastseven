package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcinfo.wapportal.repository.crawl.dao.CrawlResourceDao;
import com.bcinfo.wapportal.repository.crawl.domain.CrawlResource;
import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;
import com.bcinfo.wapportal.repository.crawl.service.impl.CrawlResourceServiceDefaultImpl;

/**
 * 
 * @author dongq
 * 
 * create time : 2009-10-18 下午05:01:23
 */
public class CrawlResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String method;
	private CrawlResourceDao dao;
	private CrawlResourceService service;
	
	public CrawlResourceServlet() {
		super();
		dao = new CrawlResourceDao();
		service = new CrawlResourceServiceDefaultImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		method = request.getParameter("method");

		if("list".equals(method)){
			list(request, response);
		}else if("detail".equals(method)){
			System.out.println("test ajax:"+request.getParameter("resId"));
			detail(request, response);
		}else if("update".equals(method)){
			update(request, response);
		}else if("send".equals(method)){
			System.out.println("频道ID:"+request.getParameter("channelId"));
			System.out.println("参数：   "+request.getParameter("resIds"));
			send(request, response);
		}else if("edit".equals(method)){
			edit(request, response);
		}
	}

	void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String channelId = request.getParameter("channelId");
		String status = request.getParameter("resStatus");
		String title = request.getParameter("title");
		String pageSize = request.getParameter("pageSize");
		String pageNo = (request.getParameter("pageNo")==null)?"1":request.getParameter("pageNo");
		
		//String pageOffset = (request.getParameter("pager.offset")==null)?"0":request.getParameter("pager.offset");
		
		int start = 0;
		int end = 1;
		
		if(Integer.parseInt(pageNo) <= 1){
			start = 1;
			end = Integer.parseInt(pageSize);
		}else{
			start = (Integer.parseInt(pageNo)-1)* Integer.parseInt(pageSize);
			end = start + Integer.parseInt(pageSize);
		}
		
		System.out.println("channelId:"+channelId+" | start:"+start+" | end:"+end+" | pageNo:"+pageNo);
		
		Long channel_id = null;
		if(!"".equals(channelId)) channel_id = Long.valueOf(channelId);
		
		List<CrawlResource> list = dao.getAllCrawlResources(channel_id,title,status,start,end);

		request.setAttribute("channelId", channelId);
		request.setAttribute("title", title);
		request.setAttribute("status", status);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("crawlResourceList", list);
		request.getRequestDispatcher("crawl_resource_list.jsp").forward(request, response);
	}
	
	void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resId = request.getParameter("resId");
		
		CrawlResource resource = dao.getCrawlResourceDetail(Long.parseLong(resId));
		
		String cnt = resource.getContent();
		String status = "审核";
		if("0".equals(resource.getStatus())) status="未"+status;
		else if("1".equals(resource.getStatus())) status="已"+status;
		
		String json = "{ ";
		       json+=" title:\""+resource.getTitle()+"\",";
		       json+=" content:\""+cnt+"\",";
		       json+=" link:\""+resource.getLink()+"\",";
		       json+=" status:\""+status+"\"";
		       json+=" }";
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
		out.print(json);
		System.out.println(json);
		out.close();
	}
	
	void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String channelId = request.getParameter("channelId");
		String[] checkStatus = request.getParameterValues("checkStatus");
		String status = "";
		for(int i=0;i<checkStatus.length;i++)
			status += checkStatus[i]+",";
		boolean bln = dao.updateCrawlResourceStatus(checkStatus);
		if(bln){
			System.out.println("审核通过:"+status);
		}
		//返回第一页
		request.setAttribute("channelId", channelId);
		request.setAttribute("title", "");
		request.getRequestDispatcher("crawl_resource_list.jsp").forward(request, response);
	}
	
	void send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long userId = (Long)request.getSession().getAttribute("userId");
		String channelId = request.getParameter("channelId");
		String str = request.getParameter("resIds");
		str = str.substring(0, str.lastIndexOf(","));
		String[] ids = str.split(",");
		
		boolean bln = service.sendResource(userId, channelId, ids);
		
		PrintWriter out = response.getWriter();
		out.print("{ msg:\""+bln+"\" }");
		out.close();
	}
	
	void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resId = request.getParameter("resId");
		System.out.println(resId);
		CrawlResource resource = dao.getCrawlResourceDetail(Long.parseLong(resId));
		request.setAttribute("resource", resource);
		request.getRequestDispatcher("crawl_resource_edit.jsp").forward(request, response);
	}
}
