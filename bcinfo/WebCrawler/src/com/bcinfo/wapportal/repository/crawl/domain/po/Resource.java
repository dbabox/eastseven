/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.po;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-10-13 ÏÂÎç01:41:32
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = -2182886211030156705L;

	private Long spcpId;
	private Long downCount;
	private Double price;
	private String filePath;
	private String modifyTime;
	private Long folderId;
	private String keyWord;
	private String createTime;
	private String postTime;
	private String availTime;
	private Long browseCount;
	private String copyRight;
	private String status;
	private String resAuthor;
	private Long resSize;
	private String title;
	private String resDesc;
	private Long pagesId;

	public Long getSpcpId() {
		return spcpId;
	}

	public void setSpcpId(Long spcpId) {
		this.spcpId = spcpId;
	}

	public Long getDownCount() {
		return downCount;
	}

	public void setDownCount(Long downCount) {
		this.downCount = downCount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getAvailTime() {
		return availTime;
	}

	public void setAvailTime(String availTime) {
		this.availTime = availTime;
	}

	public Long getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(Long browseCount) {
		this.browseCount = browseCount;
	}

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResAuthor() {
		return resAuthor;
	}

	public void setResAuthor(String resAuthor) {
		this.resAuthor = resAuthor;
	}

	public Long getResSize() {
		return resSize;
	}

	public void setResSize(Long resSize) {
		this.resSize = resSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public Long getPagesId() {
		return pagesId;
	}

	public void setPagesId(Long pagesId) {
		this.pagesId = pagesId;
	}

	@Override
	public String toString() {
		return "FileResource [availTime=" + availTime + ", browseCount="
				+ browseCount + ", copyRight=" + copyRight + ", createTime="
				+ createTime + ", downCount=" + downCount + ", filePath="
				+ filePath + ", folderId=" + folderId + ", keyWord=" + keyWord
				+ ", modifyTime=" + modifyTime + ", pagesId=" + pagesId
				+ ", postTime=" + postTime + ", price=" + price
				+ ", resAuthor=" + resAuthor + ", resDesc=" + resDesc
				+ ", resSize=" + resSize + ", spcpId=" + spcpId + ", status="
				+ status + ", title=" + title + "]";
	}

}
