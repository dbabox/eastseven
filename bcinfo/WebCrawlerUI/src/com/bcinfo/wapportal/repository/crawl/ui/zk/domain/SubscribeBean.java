package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-11-24 ÉÏÎç10:45:34
 */
public class SubscribeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long mappingId;
	private Long channelId;
	private String channelName;
	private String localCode;
	private String localFolderId;
	private Long userId;
	private String createTime;

	private String operation;

	public Long getMappingId() {
		return mappingId;
	}

	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
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

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getLocalFolderId() {
		return localFolderId;
	}

	public void setLocalFolderId(String localFolderId) {
		this.localFolderId = localFolderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "SubscribeBean [channelId=" + channelId + ", channelName="
				+ channelName + ", createTime=" + createTime + ", localCode="
				+ localCode + ", localFolderId=" + localFolderId
				+ ", mappingId=" + mappingId + ", operation=" + operation
				+ ", userId=" + userId + "]";
	}

}
