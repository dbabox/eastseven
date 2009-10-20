/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.po;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-13 ÏÂÎç01:49:38
 */
public class Folder implements Serializable {

	private static final long serialVersionUID = 5016259879088727854L;

	private Long channelId;
	private String modifyTime;
	private Long folderId;
	private String keyWord;
	private String contentPageConf;
	private String createTime;
	private Long pageImageSize;
	private String memo;
	private String showType;
	private Long pageWordSize;
	private String folderLogo;
	private String folderURL;
	private String folderName;
	private String folderIcon;
	private String folderDesc;
	private String Status;
	private String folderIndex;
	private Long folderLevel;
	private String folderPath;
	private String folderPageConf;
	private String FatherFolderIndex;

	private Integer resType;// 3:pic,8:words

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getContentPageConf() {
		return contentPageConf;
	}

	public void setContentPageConf(String contentPageConf) {
		this.contentPageConf = contentPageConf;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getPageImageSize() {
		return pageImageSize;
	}

	public void setPageImageSize(Long pageImageSize) {
		this.pageImageSize = pageImageSize;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getFolderLogo() {
		return folderLogo;
	}

	public void setFolderLogo(String folderLogo) {
		this.folderLogo = folderLogo;
	}

	public String getFolderURL() {
		return folderURL;
	}

	public void setFolderURL(String folderURL) {
		this.folderURL = folderURL;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderIcon() {
		return folderIcon;
	}

	public void setFolderIcon(String folderIcon) {
		this.folderIcon = folderIcon;
	}

	public String getFolderDesc() {
		return folderDesc;
	}

	public void setFolderDesc(String folderDesc) {
		this.folderDesc = folderDesc;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(String folderIndex) {
		this.folderIndex = folderIndex;
	}

	public Long getFolderLevel() {
		return folderLevel;
	}

	public void setFolderLevel(Long folderLevel) {
		this.folderLevel = folderLevel;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getFolderPageConf() {
		return folderPageConf;
	}

	public void setFolderPageConf(String folderPageConf) {
		this.folderPageConf = folderPageConf;
	}

	public String getFatherFolderIndex() {
		return FatherFolderIndex;
	}

	public void setFatherFolderIndex(String fatherFolderIndex) {
		FatherFolderIndex = fatherFolderIndex;
	}

	public Integer getResType() {
		return resType;
	}

	public void setResType(Integer resType) {
		this.resType = resType;
	}

	@Override
	public String toString() {
		return "Folder [FatherFolderIndex=" + FatherFolderIndex + ", Status="
				+ Status + ", channelId=" + channelId + ", contentPageConf="
				+ contentPageConf + ", createTime=" + createTime
				+ ", folderDesc=" + folderDesc + ", folderIcon=" + folderIcon
				+ ", folderId=" + folderId + ", folderIndex=" + folderIndex
				+ ", folderLevel=" + folderLevel + ", folderLogo=" + folderLogo
				+ ", folderName=" + folderName + ", folderPageConf="
				+ folderPageConf + ", folderPath=" + folderPath
				+ ", folderURL=" + folderURL + ", keyWord=" + keyWord
				+ ", memo=" + memo + ", modifyTime=" + modifyTime
				+ ", pageImageSize=" + pageImageSize + ", pageWordSize="
				+ pageWordSize + ", resType=" + resType + ", showType="
				+ showType + "]";
	}

}
