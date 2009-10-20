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
	
	public CrawlResourceServlet() {
		super();
		dao = new CrawlResourceDao();
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
		}
	}

	void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String channelId = request.getParameter("channelId");
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
			end = start + Integer.parseInt(pageSize) - 1;
		}
		
		System.out.println("channelId:"+channelId+" | start:"+start+" | end:"+end+" | pageNo:"+pageNo);
		
		Long channel_id = null;
		if(!"".equals(channelId)) channel_id = Long.valueOf(channelId);
		
		List<CrawlResource> list = dao.getAllCrawlResources(channel_id,title,start,end);

		request.setAttribute("channelId", channelId);
		request.setAttribute("title", title);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("crawlResourceList", list);
		request.getRequestDispatcher("crawl_resource_list.jsp").forward(request, response);
	}
	
	void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resId = request.getParameter("resId");
		
		CrawlResource resource = dao.getCrawlResourceDetail(Long.parseLong(resId));
		
		String src = resource.getContent();
		//src = "<img src=E:/usr/local/jboss-3.2.7/server/default/deploy/spcpnew.war/upload/2009/10/19/Img267504345.jpg>";
		//src = "<img alt=意甲-桑普客场1-1平拉齐奥国米2分优势独居榜首 src=http://i1.sinaimg.cn/ty/g/2009-10-18/U350P6T12D4642992F1286DT20091019030614.jpg border=1 vspace=4 hspace=8>";
		
		String json = "{ ";
		       json+=" title:\""+resource.getTitle()+"\",";
		       json+=" content:\""+src+"\",";
		       json+=" link:\""+resource.getLink()+"\",";
		       json+=" status:\""+resource.getStatus()+"\"";
		       json+=" }";
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
		out.print(json);
		System.out.println(json);
		out.close();
	}
}
