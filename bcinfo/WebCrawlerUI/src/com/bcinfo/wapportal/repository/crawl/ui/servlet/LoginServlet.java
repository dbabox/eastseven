package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.http.WebManager;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.UserDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * 
 * @author dongq
 * 
 * create time : 2009-10-23 ÏÂÎç01:55:48
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(LoginServlet.class);
       
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		
		Session zkSession = WebManager.getSession(this.getServletContext(), request);
		System.out.println(zkSession);
		
		UserBean user = new UserDao().getUser(userName);
		
		if(user.getUserId()!=null){
			
			System.out.println(user);
			log.info("ÓÃ»§"+userName+"µÇÂ¼");
			zkSession.setAttribute("user", user);
			response.sendRedirect("index.zul");
		}else{
			response.sendRedirect("pages/login/login.zul");
		}
	}

}
