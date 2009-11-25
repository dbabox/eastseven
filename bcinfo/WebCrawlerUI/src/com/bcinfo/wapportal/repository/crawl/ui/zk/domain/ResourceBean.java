package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 */
public class ResourceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long resId;
	private Long channelId;
	private String channelName;// TODO ÈßÓà
	private String title;
	private String link;
	private String content;
	private String text;
	private String imgPathSet;
	private String status;
	private String createTime;

	private String pics;// ·ÇÊý¾Ý¿â×Ö¶Î

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImgPathSet() {
		return imgPathSet;
	}

	public void setImgPathSet(String imgPathSet) {
		this.imgPathSet = imgPathSet;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Override
	public String toString() {
		return "CrawlResource [channelId=" + channelId + ", content=" + content
				+ ", createTime=" + createTime + ", imgPathSet=" + imgPathSet
				+ ", link=" + link + ", resId=" + resId + ", status=" + status
				+ ", text=" + text + ", title=" + title + "]";
	}
}
