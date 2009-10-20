/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 上午11:32:26<br>
 *         中心频道与地方栏目对于关系表
 */
public class ChannelMapping implements Serializable {

	private static final long serialVersionUID = 6872095441836904734L;

	private Long mappingId;
	private Long channelId;
	private String localCode;
	private String localChannelId;
	private String createTime;

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

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getLocalChannelId() {
		return localChannelId;
	}

	public void setLocalChannelId(String localChannelId) {
		this.localChannelId = localChannelId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ChannelMapping [channelId=" + channelId + ", createTime="
				+ createTime + ", localChannelId=" + localChannelId
				+ ", localCode=" + localCode + ", mappingId=" + mappingId + "]";
	}

}
