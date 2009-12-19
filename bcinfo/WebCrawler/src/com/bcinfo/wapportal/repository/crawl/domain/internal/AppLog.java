/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.internal;

/**
 * @author dongq
 * 
 *         create time : 2009-12-10 ÏÂÎç11:21:41
 */
public class AppLog {

	private Long logId;
	private String logMessage;
	private Long logChannelId;
	private String url;
	private Long catchCount;
	private String createTime;

	public AppLog(String logMessage, Long logChannelId, String url, Long catchCount) {
		super();
		this.logMessage = logMessage;
		this.logChannelId = logChannelId;
		this.url = url;
		this.catchCount = catchCount;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public Long getLogChannelId() {
		return logChannelId;
	}

	public void setLogChannelId(Long logChannelId) {
		this.logChannelId = logChannelId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getCatchCount() {
		return catchCount;
	}

	public void setCatchCount(Long catchCount) {
		this.catchCount = catchCount;
	}

	@Override
	public String toString() {
		return "AppLog [catchCount=" + catchCount + ", createTime="
				+ createTime + ", logChannelId=" + logChannelId + ", logId="
				+ logId + ", logMessage=" + logMessage + ", url=" + url + "]";
	}

}
