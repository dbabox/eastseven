package com.bcinfo.crawl.dao;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	//private static final Log log = LogFactory.getLog(Activator.class);
	//private ServiceRegistration sr = null;
	
	public void start(BundleContext context) throws Exception {
		/*
		sr = context.registerService(WebCrawlerDao.class.getName(), new WebCrawlerDaoImpl(), null);
		log.info(WebCrawlerDao.class.getName() + "发布");
		sr = context.registerService(WebCrawlerDaoXE.class.getName(), new WebCrawlerDaoImplXe(), null);
		log.info(WebCrawlerDaoXE.class.getName() + "发布");
		*/
	}

	public void stop(BundleContext context) throws Exception {
		/*
		if(sr != null) {
			sr.unregister();
			log.info(WebCrawlerDao.class.getName() + "注销");
			log.info(WebCrawlerDaoXE.class.getName() + "注销");
		}
		*/
	}

}
