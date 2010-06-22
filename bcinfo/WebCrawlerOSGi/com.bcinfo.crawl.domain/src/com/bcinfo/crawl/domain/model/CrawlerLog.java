/**
 * 
 */
package com.bcinfo.crawl.domain.model;

/**
 * @author dongq
 * 
 *         create time : 2010-6-1 ÉÏÎç10:38:36
 */
public final class CrawlerLog {

	private Long channelId;
	private String url;

	public CrawlerLog(Long channelId, String url) {
		super();
		this.channelId = channelId;
		this.url = url;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		CrawlerLog log = (CrawlerLog)obj;
		boolean bln = this.url.equalsIgnoreCase(log.getUrl()) && this.channelId.longValue() == log.getChannelId().longValue();
		return bln;
	}

}
