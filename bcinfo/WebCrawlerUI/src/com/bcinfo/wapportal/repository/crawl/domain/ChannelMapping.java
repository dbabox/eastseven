/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 上午11:32:26<br>
 *         中心频道与地方栏目对于关系表<br>
 *         2009-11-17 添加MAPPING_FLAG字段，用以控制是否自动FTP到目标SPCP后台<br>
 *         0--非定时;1--定时<br>
 */
public class ChannelMapping implements Serializable {

	private static final long serialVersionUID = 6872095441836904734L;

	private Long mappingId;
	private String mappingFlag;
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

	public String getMappingFlag() {
		return mappingFlag;
	}

	public void setMappingFlag(String mappingFlag) {
		this.mappingFlag = mappingFlag;
	}

	@Override
	public String toString() {
		return "ChannelMapping [channelId=" + channelId + ", createTime="
				+ createTime + ", localChannelId=" + localChannelId
				+ ", localCode=" + localCode + ", mappingFlag=" + mappingFlag
				+ ", mappingId=" + mappingId + "]";
	}

}
