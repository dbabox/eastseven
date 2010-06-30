/**
 * 
 */
package com.bcinfo.crawl.domain.model;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2010-5-17 下午09:41:24
 */
public class Site implements Serializable {

	private static final long serialVersionUID = 4172117899604842806L;

	public static final String SINA = "新浪";
	public static final String SOHU = "搜狐";
	public static final String QQ = "腾讯";
	public static final String XIN_HUA_NET = "新华网";
	public static final String SD_INFO = "齐鲁热线";

	private String name;
	private String url;
	private String charset = "UTF-8";
	private Long channelId;
	private String channelName;
	private String pageSuffix = "shtml";
	private String pageSelector;
	private String contentSelector;
	private String deployTimeSelector;
	private String datePattern;
	private boolean realTime = true;
	private String imageAddress = "";
	private long frequency = 60 * 60 * 1000;
	private boolean debug = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getPageSuffix() {
		return pageSuffix;
	}

	public void setPageSuffix(String pageSuffix) {
		this.pageSuffix = pageSuffix;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}

	public String getPageSelector() {
		return pageSelector.trim();
	}

	public void setPageSelector(String pageSelector) {
		this.pageSelector = pageSelector;
	}

	public String getContentSelector() {
		return contentSelector;
	}

	public void setContentSelector(String contentSelector) {
		this.contentSelector = contentSelector;
	}

	public String getDeployTimeSelector() {
		return deployTimeSelector;
	}

	public void setDeployTimeSelector(String deployTimeSelector) {
		this.deployTimeSelector = deployTimeSelector;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}

	@Override
	public String toString() {
		return "Site [channelId=" + channelId + ", channelName=" + channelName
				+ ", charset=" + charset + ", contentSelector="
				+ contentSelector + ", datePattern=" + datePattern + ", debug="
				+ debug + ", deployTimeSelector=" + deployTimeSelector
				+ ", frequency=" + frequency + ", name=" + name
				+ ", pageSelector=" + pageSelector + ", pageSuffix="
				+ pageSuffix + ", realTime=" + realTime + ", url=" + url + "]";
	}

}
