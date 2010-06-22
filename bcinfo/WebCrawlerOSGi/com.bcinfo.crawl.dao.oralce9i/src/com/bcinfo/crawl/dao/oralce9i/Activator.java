package com.bcinfo.crawl.dao.oralce9i;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.bcinfo.crawl.dao.service.WebCrawlerDao;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	private ServiceRegistration sr = null;
	
	final String name = WebCrawlerDao.class.getName();
	
	public void start(BundleContext context) throws Exception {
		sr = context.registerService(name, new WebCrawlerDaoWithOracle9i(), null);
		log.info("数据库操作服务[" + name + "]发布");
	}

	public void stop(BundleContext context) throws Exception {
		if(sr != null) {
			sr.unregister();
			log.info("数据库操作服务[" + name + "]注销");
		}
	}

}
