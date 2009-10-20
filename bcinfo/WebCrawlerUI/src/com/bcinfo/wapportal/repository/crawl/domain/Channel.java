/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-18 ÉÏÎç11:03:02<br>
 *         create table TWAP_PUBLIC_CHANNEL (<br>
 *         CHANNEL_ID NUMERIC(11) not null,<br>
 *         CHANNEL_PID NUMERIC(11),<br>
 *         CHANNEL_NAME VARCHAR2(64),<br>
 *         CHANNEL_PATH VARCHAR2(256),<br>
 *         CHANNEL_INDEX VARCHAR2(64),<br>
 *         CREATE_TIME DATE default SYSDATE,<br>
 *         constraint PK_TWAP_PUBLIC_CHANNEL primary key (CHANNEL_ID) );<br>
 */
public class Channel implements Serializable {

	private static final long serialVersionUID = -2082136969721378418L;

	private Long channelId;
	private Long channelPid;
	private String channelName;
	private String channelPath;
	private String channelIndex;
	private String createTime;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getChannelPid() {
		return channelPid;
	}

	public void setChannelPid(Long channelPid) {
		this.channelPid = channelPid;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Channel [channelId=" + channelId + ", channelIndex="
				+ channelIndex + ", channelName=" + channelName
				+ ", channelPath=" + channelPath + ", channelPid=" + channelPid
				+ ", createTime=" + createTime + "]";
	}

}
