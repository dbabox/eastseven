/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.domain;

/**
 * @author dongq
 * 
 *         create time : 2009-12-23 ÏÂÎç03:34:48<br>
 */
public class PageFolderBean {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private String folderId;
	private String wapFolderId;
	private String folderName;
	private Long folderIndex;
	private String folderStatus;
	private String folderDesc;
	private Long folderLevel;
	private String folderUrl;
	private String createTime;
	private String folderLogo;
	private String folderIcon;
	private String showType;
	private Long pageWordSize;
	private Long visitTime;

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getWapFolderId() {
		return wapFolderId;
	}

	public void setWapFolderId(String wapFolderId) {
		this.wapFolderId = wapFolderId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Long getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Long folderIndex) {
		this.folderIndex = folderIndex;
	}

	public String getFolderStatus() {
		return folderStatus;
	}

	public void setFolderStatus(String folderStatus) {
		this.folderStatus = folderStatus;
	}

	public String getFolderDesc() {
		return folderDesc;
	}

	public void setFolderDesc(String folderDesc) {
		this.folderDesc = folderDesc;
	}

	public Long getFolderLevel() {
		return folderLevel;
	}

	public void setFolderLevel(Long folderLevel) {
		this.folderLevel = folderLevel;
	}

	public String getFolderUrl() {
		return folderUrl;
	}

	public void setFolderUrl(String folderUrl) {
		this.folderUrl = folderUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFolderLogo() {
		return folderLogo;
	}

	public void setFolderLogo(String folderLogo) {
		this.folderLogo = folderLogo;
	}

	public String getFolderIcon() {
		return folderIcon;
	}

	public void setFolderIcon(String folderIcon) {
		this.folderIcon = folderIcon;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Long getPageWordSize() {
		return pageWordSize;
	}

	public void setPageWordSize(Long pageWordSize) {
		this.pageWordSize = pageWordSize;
	}

	public Long getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Long visitTime) {
		this.visitTime = visitTime;
	}

	@Override
	public String toString() {
		return "PageFolderBean [createTime=" + createTime + ", folderDesc="
				+ folderDesc + ", folderIcon=" + folderIcon + ", folderId="
				+ folderId + ", folderIndex=" + folderIndex + ", folderLevel="
				+ folderLevel + ", folderLogo=" + folderLogo + ", folderName="
				+ folderName + ", folderStatus=" + folderStatus
				+ ", folderUrl=" + folderUrl + ", pageWordSize=" + pageWordSize
				+ ", showType=" + showType + ", visitTime=" + visitTime
				+ ", wapFolderId=" + wapFolderId + "]";
	}

}
