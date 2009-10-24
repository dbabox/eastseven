package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcinfo.wapportal.repository.crawl.dao.UserDao;

/**
 * 
 * @author dongq
 * 
 * create time : 2009-10-23 ÏÂÎç01:55:48
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		Long userId = new UserDao().getLoginUserId(userName, password);
		
		if(userId!=null){
			request.getSession().setAttribute("userId", userId);
			request.getRequestDispatcher("channel.jsp").forward(request, response);
		}else{
			response.sendRedirect("login.jsp");
		}
	}

}
