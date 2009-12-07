/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongq
 * 
 *         create time : 2009-9-8 上午10:46:24<br>
 *         栏目对象<br>
 */
public class Folder implements Serializable {

	private static final long serialVersionUID = 1L;
	public static int INSERT = 1;
	public static int UPDATE = 2;

	private String id;
	private String url;
	private String title;
	private String content;

	private String resFileName;
	private String operation;

	private List resources;

	public Folder() {
		super();
	}

	public Folder(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}

	public Folder(String url, String title, String content) {
		super();
		this.url = url;
		this.title = title;
		this.content = content;
	}

	public Folder(String id, String url, String title, String content) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List getResources() {
		return resources;
	}

	public void setResources(List resources) {
		this.resources = resources;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Folder [id=" + id + ", operation=" + operation
				+ ", resFileName=" + resFileName + ", title=" + title
				+ ", url=" + url + "]";
	}

}
