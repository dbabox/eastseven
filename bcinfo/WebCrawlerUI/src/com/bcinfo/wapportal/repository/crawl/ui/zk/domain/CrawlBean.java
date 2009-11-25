package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-11-23 ÏÂÎç04:21:10
 */
public class CrawlBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long crawlId;
	private Long channelId;
	private String channelName;
	private String crawlUrl;
	private String crawlStatus;
	private String createTime;

	public Long getCrawlId() {
		return crawlId;
	}

	public void setCrawlId(Long crawlId) {
		this.crawlId = crawlId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getCrawlUrl() {
		return crawlUrl;
	}

	public void setCrawlUrl(String crawlUrl) {
		this.crawlUrl = crawlUrl;
	}

	public String getCrawlStatus() {
		return crawlStatus;
	}

	public void setCrawlStatus(String crawlStatus) {
		this.crawlStatus = crawlStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Override
	public String toString() {
		return "CrawlList [channelId=" + channelId + ", crawlId=" + crawlId
				+ ", crawlStatus=" + crawlStatus + ", crawlUrl=" + crawlUrl
				+ ", createTime=" + createTime + "]";
	}
}
