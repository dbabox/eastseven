package org.dongq.analytics.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dongq.analytics.model.Responder;
import org.dongq.analytics.service.QuestionnairePaperService;
import org.dongq.analytics.service.QuestionnairePaperServiceImpl;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Log logger = LogFactory.getLog(InitServlet.class);
	
	private final String METHOD = "m";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("初始化WEB");
		ServletContext context = config.getServletContext();
		String name = config.getInitParameter("config");
		String path = "/WEB-INF/classes/" + name;
		logger.info(path);
		
		try {
			URL url = context.getResource(path);
			logger.info(url);
			Configuration file = new PropertiesConfiguration(url);
			if(file.isEmpty()) {
				logger.error("url is empty");
			} else {
				logger.debug("before config is " + file.getString("db.path"));
				String dbPath = file.getString("db.path");
				dbPath = dbPath.replaceFirst("\\{webapp.root\\}/", config.getServletContext().getRealPath("/"));
				file.setProperty("db.path", dbPath);
				logger.debug("after  config is " + file.getString("db.path"));
				
				int start = new DbServer(file).getServer().start();
				logger.info("HsqlDb Server is starting..." + start);
				
				DbHelper.driver = file.getString("db.classDriver");
				DbHelper.url = file.getString("db.url");
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String method = req.getParameter(METHOD);
		logger.debug(method);
		if("login".equalsIgnoreCase(method)) {
			login(req, resp);
		}
	}
	
	void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String no = req.getParameter("no");
		String pwd = req.getParameter("pwd");
		String path = "paper.jsp";

		QuestionnairePaperService service = new QuestionnairePaperServiceImpl();
		Responder responder = service.login(no, pwd);
		if(responder != null && StringUtils.isNotBlank(responder.getNo())) {
			boolean answered = service.hasAnswered(responder.getId());
			if(answered) {
				resp.sendRedirect("login.jsp?msg=hasanswered");
			} else {
				req.setAttribute("v", responder.getVersion());
				req.setAttribute("id", responder.getId());
				req.setAttribute("name", responder.getName());
				req.getRequestDispatcher(path).forward(req, resp);
			}
		} else {
			resp.sendRedirect("login.jsp?msg=loginfail");
		}
		
	}
	
}
