package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 */
public class ChannelBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long channelId;
	private Long channelPid;
	private String channelName;
	private String channelPath;
	private String channelIndex;//index的值暂时用来标识节点关系:0-非叶子节点;1-叶子节点
	private String createTime;

	public ChannelBean() {
		// TODO Auto-generated constructor stub
	}

	public ChannelBean(Long channelId, Long channelPid, String channelName,
			String channelPath, String channelIndex, String createTime) {
		super();
		this.channelId = channelId;
		this.channelPid = channelPid;
		this.channelName = channelName;
		this.channelPath = channelPath;
		this.channelIndex = channelIndex;
		this.createTime = createTime;
	}

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
