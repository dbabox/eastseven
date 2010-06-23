/**
 * 
 */
package com.bcinfo.crawl.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bcinfo.crawl.domain.model.Site;

/**
 * @author dongq
 * 
 *         create time : 2010-6-23 ÉÏÎç09:36:17
 */
public final class SiteUtil {

	private static final Log log = LogFactory.getLog(SiteUtil.class);
	
	public List<Site> getSites(String configPath) {
		List<Site> sites = new ArrayList<Site>();
		try {
			sites.addAll(getSites(new FileInputStream(configPath)));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return sites;
	}
	
	@SuppressWarnings("unchecked")
	public List<Site> getSites(InputStream input) throws Exception {
		List<Site> sites = new ArrayList<Site>();
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(input);
		Element root = doc.getRootElement();
		List siteList = root.getChildren("site");
		sites = new ArrayList<Site>();
		for(Iterator iter = siteList.iterator(); iter.hasNext(); ) {
			Site site = new Site();
			Element siteElement = (Element)iter.next();
			if(StringUtils.isNotEmpty(siteElement.getChild("name").getText()))
				site.setName(siteElement.getChild("name").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("url").getText()))
			    site.setUrl(siteElement.getChild("url").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("charset").getText()))
			    site.setCharset(siteElement.getChild("charset").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("channelId").getText()))
				site.setChannelId(Long.parseLong(siteElement.getChild("channelId").getText()));
			if(StringUtils.isNotEmpty(siteElement.getChild("channelName").getText()))
				site.setChannelName(siteElement.getChild("channelName").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("pageSuffix").getText()))
				site.setPageSuffix(siteElement.getChild("pageSuffix").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("pageSelector").getText()))
				site.setPageSelector(siteElement.getChild("pageSelector").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("contentSelector").getText()))
				site.setContentSelector(siteElement.getChild("contentSelector").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("deployTimeSelector").getText()))
				site.setDeployTimeSelector(siteElement.getChild("deployTimeSelector").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("datePattern").getText()))
				site.setDatePattern(siteElement.getChild("datePattern").getText());
			if(StringUtils.isNotEmpty(siteElement.getChild("realTime").getText()))
				site.setRealTime(Boolean.valueOf(siteElement.getChild("realTime").getText()));
			if(StringUtils.isNotEmpty(siteElement.getChild("frequency").getText()))
				site.setFrequency(Long.parseLong(siteElement.getChild("frequency").getText()));
			if(StringUtils.isNotEmpty(siteElement.getChild("debug").getText()))
				site.setDebug(Boolean.valueOf(siteElement.getChild("debug").getText()));
			sites.add(site);
		}
		return sites;
	}
	
	public static void main(String[] args) throws Exception {
		List<Site> sites = new SiteUtil().getSites(SiteUtil.class.getResourceAsStream("sites.xml"));
		for(Site site : sites) {
			System.out.println(site.getPageSelector());
		}
	}
}
