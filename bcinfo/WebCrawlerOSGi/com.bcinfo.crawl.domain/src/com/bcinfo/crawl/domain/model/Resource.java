/**
 * 
 */
package com.bcinfo.crawl.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dongq
 * 
 *         create time : 2010-5-11 下午03:54:52<br />
 *         资源对象基类
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String AGREE = "1";
	public static final String DISAGREE = "0";
	public static final String OLD_IMAGE_SRC = "oldImageSrc";
	public static final String NEW_IMAGE_SRC = "newImageSrc";

	private Long id;
	private String userName = "admin";
	private String link;
	private String title;
	private String charset;
	private String content;
	private Channel channel;
	private String status = DISAGREE;// 1-已审;0-未审
	private String imgPath = "";
	private List<String> images = new ArrayList<String>();

	private Site site;

	public Resource() {
	}

	public Resource(String link, String title, String charset) {
		super();
		this.link = link;
		this.title = title;
		this.charset = charset;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isEmpty() {
		return (title == null && "".equals(title))
				|| (content == null && "".equals(content));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Resource other = (Resource) obj;
		return this.link.equalsIgnoreCase(other.getLink())
				&& this.title.equalsIgnoreCase(other.getTitle());
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		String str = "Resource [";
		str += "title=" + title;
		str += ", content=" + content;
		str += ", channel=" + channel;
		str += ", charset=" + charset;
		str += ", link=" + link;
		str += "]";
		return str;
	}

}
