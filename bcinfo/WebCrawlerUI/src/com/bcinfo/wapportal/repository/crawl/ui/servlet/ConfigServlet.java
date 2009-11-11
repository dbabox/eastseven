package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-10-27 ����09:51:18
 */
public final class ConfigServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public ConfigServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String configPath =  config.getInitParameter("configFile");
		if(configPath!=null || "".equals(configPath)){
			InputStream in;
			try {
				//in = new BufferedInputStream (new FileInputStream(configPath));
				in = config.getServletContext().getResourceAsStream(configPath);
				Properties property = new Properties();
				property = new Properties();
				property.load(in);
				in.close();
				ConfigPropertyUtil.property = property;
				System.out.println("�����ļ�["+configPath+"]���سɹ�");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println("�����ļ�["+configPath+"]����ʧ��");
		}
		
		configPath = config.getInitParameter("logFile");
		if(configPath!=null || "".equals(configPath)){
			InputStream in;
			try {
				//in = new BufferedInputStream (new FileInputStream(configPath));
				in = config.getServletContext().getResourceAsStream(configPath);
				Properties property = new Properties();
				property = new Properties();
				property.load(in);
				in.close();
				PropertyConfigurator.configure(property);
				System.out.println("��־�ļ�["+configPath+"]���سɹ�");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("��־�ļ�["+configPath+"]����ʧ��");
		}
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
