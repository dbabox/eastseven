/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-18 ÉÏÎç11:04:05<br>
 *         create table TWAP_PUBLIC_CRAWL_LIST (<br>
 *         CRAWL_ID NUMERIC(11) not null,<br>
 *         CHANNEL_ID NUMERIC(11) not null,<br>
 *         CRAWL_URL VARCHAR2(256) not null,<br>
 *         CRAWL_STATUS CHAR(1) default '1',<br>
 *         CREATE_TIME DATE default SYSDATE,<br>
 *         constraint PK_TWAP_PUBLIC_CRAWL_LIST primary key (CRAWL_ID)<br>
 *         );<br>
 */
public class CrawlList implements Serializable {

	private static final long serialVersionUID = -6729950694761957579L;

	private Long crawlId;
	private Long channelId;
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

	@Override
	public String toString() {
		return "CrawlList [channelId=" + channelId + ", crawlId=" + crawlId
				+ ", crawlStatus=" + crawlStatus + ", crawlUrl=" + crawlUrl
				+ ", createTime=" + createTime + "]";
	}

}
