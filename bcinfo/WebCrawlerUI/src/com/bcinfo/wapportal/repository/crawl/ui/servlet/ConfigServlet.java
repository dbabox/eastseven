package com.bcinfo.wapportal.repository.crawl.ui.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.job.AutoSendJob;

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
		
		//����������
		try{
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//ҵ��Job
			JobDetail job = new JobDetail("autoSendJob", Scheduler.DEFAULT_GROUP, AutoSendJob.class);
			long repeatInterval = 60 * 60 * 1000L;//TODO 60����һ��
			Trigger trigger = new SimpleTrigger("singleTrigger", Scheduler.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);
			scheduler.scheduleJob(job, trigger);
			
			//scheduler.start();
			System.out.println("����������");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("��������������");
		}
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
