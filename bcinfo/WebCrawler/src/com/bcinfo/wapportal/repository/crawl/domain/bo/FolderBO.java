/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.bo;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午11:02:48<br>
 *         栏目信息的业务对象
 */
public class FolderBO implements Serializable {

	private static final long serialVersionUID = -6434622155462613619L;

	private String folderId;// channel_id
	private String title;
	private String link;
	private String content;
	private String imgPathSet;
	private List<ResourceBO> resources;

	public FolderBO() {

	}

	public FolderBO(String folderId, String title, String link, String content, String imgPathSet) {
		super();
		this.folderId = folderId;
		this.title = title;
		this.link = link;
		this.content = content;
		this.imgPathSet = imgPathSet;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
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

	public List<ResourceBO> getResources() {
		return resources;
	}

	public void setResources(List<ResourceBO> resources) {
		this.resources = resources;
	}

	public String getImgPathSet() {
		return imgPathSet;
	}

	public void setImgPathSet(String imgPathSet) {
		this.imgPathSet = imgPathSet;
	}

	@Override
	public String toString() {
		return "FolderBO [content=" + content + ", folderId=" + folderId
				+ ", imgPathSet=" + imgPathSet + ", link=" + link
				+ ", resources=" + resources + ", title=" + title + "]";
	}

}
