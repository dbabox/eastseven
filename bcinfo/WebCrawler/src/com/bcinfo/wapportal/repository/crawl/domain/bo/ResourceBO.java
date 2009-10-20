/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.bo;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 上午11:04:12<br>
 *         资源的业务对象
 */
public class ResourceBO implements Serializable {

	private static final long serialVersionUID = -5220536453060099522L;

	private int id;
	private FolderBO folder;
	private String content;
	private String path;
	private int type;

	public ResourceBO() {

	}

	public ResourceBO(int id, FolderBO folder, String content, int type) {
		super();
		this.id = id;
		this.folder = folder;
		this.content = content;
		this.type = type;
	}

	public ResourceBO(int id, FolderBO folder, String content, String path,
			int type) {
		super();
		this.id = id;
		this.folder = folder;
		this.content = content;
		this.path = path;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FolderBO getFolder() {
		return folder;
	}

	public void setFolder(FolderBO folder) {
		this.folder = folder;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ResourceBO [content=" + content + ", folder=" + folder
				+ ", id=" + id + ", path=" + path + ", type=" + type + "]";
	}

}
