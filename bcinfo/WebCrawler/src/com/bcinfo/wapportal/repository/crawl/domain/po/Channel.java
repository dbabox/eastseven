/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.po;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ÉÏÎç10:13:56
 */
public class Channel implements Serializable {

	private static final long serialVersionUID = -2082136969721378418L;

	private Long channelId;
	private String createTime;
	private String channelName;
	private String channelPath;
	private String channelIndex;

	public Channel() {
		
	}
	
	public Channel(String channelIndex) {
		this.channelIndex = channelIndex;
	}
	
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
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

	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}

	public String getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(String channelIndex) {
		this.channelIndex = channelIndex;
	}

	@Override
	public String toString() {
		return "Channel [channelId=" + channelId + ", channelIndex="
				+ channelIndex + ", channelName=" + channelName
				+ ", channelPath=" + channelPath + ", createTime=" + createTime
				+ "]";
	}

}
