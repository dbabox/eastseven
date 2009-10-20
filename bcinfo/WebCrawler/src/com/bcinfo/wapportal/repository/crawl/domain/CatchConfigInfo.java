/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-9-25 ÏÂÎç03:16:27<br>
 */
public class CatchConfigInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String folderId;
	private String url;
	private boolean isUse;
	private boolean fetchImage;
	private String fetchType;// Ò³Ãæ£»RSS
	private String webSite;
	private boolean isNew;
	private String logFilePath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}

	public boolean getFetchImage() {
		return fetchImage;
	}

	public void setFetchImage(boolean fetchImage) {
		this.fetchImage = fetchImage;
	}

	public String getFetchType() {
		return fetchType;
	}

	public void setFetchType(String fetchType) {
		this.fetchType = fetchType;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getLogFilePath() {
		return logFilePath;
	}
	
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public String toString() {
		return "CatchConfigInfo [fetchImage=" + fetchImage + ", fetchType="
				+ fetchType + ", folderId=" + folderId + ", id=" + id
				+ ", isNew=" + isNew + ", isUse=" + isUse + ", logFilePath="
				+ logFilePath + ", url=" + url + ", webSite=" + webSite + "]";
	}
	
}
