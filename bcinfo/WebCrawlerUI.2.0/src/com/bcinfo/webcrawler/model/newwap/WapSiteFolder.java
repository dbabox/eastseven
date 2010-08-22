package com.bcinfo.webcrawler.model.newwap;

import java.sql.Timestamp;

public class WapSiteFolder {

	public static final String TABLE_NAME = "WAP_SITE_FOLDER";
	public static final String FOLDER_ID = "FOLDER_ID";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String FOLDER_DESC = "FOLDER_DESC";
	public static final String FOLDER_ICON = "FOLDER_ICON";
	public static final String FOLDER_NAME = "FOLDER_NAME";
	public static final String FOLDER_PATH = "FOLDER_PATH";
	public static final String KEYWORD = "KEYWORD";
	public static final String IS_SHOWSOURCE = "IS_SHOWSOURCE";
	public static final String MODIFY_TIME = "MODIFY_TIME";
	public static final String PAGE_IMAGE_SIZE = "PAGE_IMAGE_SIZE";
	public static final String PAGE_WORD_SIZE = "PAGE_WORD_SIZE";
	public static final String PAGE_LIST_NUMBER = "PAGE_LIST_NUMBER";
	public static final String STATUS = "STATUS";
	public static final String FOLDER_LEVEL = "FOLDER_LEVEL";
	public static final String PARENT_FOLDER = "PARENT_FOLDER";
	public static final String WAPSITE_ID = "WAPSITE_ID";
	public static final String CHANNEL_ID = "CHANNEL_ID";
	public static final String F_PAGE = "F_PAGE";
	public static final String FOLDER_PAGE_ID = "FOLDER_PAGE_ID";
	public static final String RES_PAGE_ID = "RES_PAGE_ID";
	public static final String USER_ID = "USER_ID";
	public static final String CHANNELS_ID = "CHANNELS_ID";
	public static final String SITE_PAGE_ID = "SITE_PAGE_ID";

	private Long folderId;
	private Timestamp createTime;
	private String folderDesc;
	private String folderIcon;
	private String folderName;
	private String folderPath;
	private String keyword;
	private String isShowsource;
	private Timestamp modifyTime;
	private Long pageImageSize;
	private Long pageWordSize;
	private Long pageListNumber;
	private String status;
	private Long folderLevel;
	private Long parentFolder;
	private Long wapsiteId;
	private Long channelId;
	private Long fPage;
	private Long folderPageId;
	private Long resPageId;
	private Long userId;
	private Long channelsId;
	private Long sitePageId;

	public Long getFolderId() {
		return this.folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFolderDesc() {
		return this.folderDesc;
	}

	public void setFolderDesc(String folderDesc) {
		this.folderDesc = folderDesc;
	}

	public String getFolderIcon() {
		return this.folderIcon;
	}

	public void setFolderIcon(String folderIcon) {
		this.folderIcon = folderIcon;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderPath() {
		return this.folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getIsShowsource() {
		return this.isShowsource;
	}

	public void setIsShowsource(String isShowsource) {
		this.isShowsource = isShowsource;
	}

	public Timestamp getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getPageImageSize() {
		return this.pageImageSize;
	}

	public void setPageImageSize(Long pageImageSize) {
		this.pageImageSize = pageImageSize;
	}

	public Long getPageWordSize() {
		return this.pageWordSize;
	}

	public void setPageWordSize(Long pageWordSize) {
		this.pageWordSize = pageWordSize;
	}

	public Long getPageListNumber() {
		return this.pageListNumber;
	}

	public void setPageListNumber(Long pageListNumber) {
		this.pageListNumber = pageListNumber;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getFolderLevel() {
		return this.folderLevel;
	}

	public void setFolderLevel(Long folderLevel) {
		this.folderLevel = folderLevel;
	}

	public Long getParentFolder() {
		return this.parentFolder;
	}

	public void setParentFolder(Long parentFolder) {
		this.parentFolder = parentFolder;
	}

	public Long getWapsiteId() {
		return this.wapsiteId;
	}

	public void setWapsiteId(Long wapsiteId) {
		this.wapsiteId = wapsiteId;
	}

	public Long getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getFPage() {
		return this.fPage;
	}

	public void setFPage(Long fPage) {
		this.fPage = fPage;
	}

	public Long getFolderPageId() {
		return this.folderPageId;
	}

	public void setFolderPageId(Long folderPageId) {
		this.folderPageId = folderPageId;
	}

	public Long getResPageId() {
		return this.resPageId;
	}

	public void setResPageId(Long resPageId) {
		this.resPageId = resPageId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getChannelsId() {
		return this.channelsId;
	}

	public void setChannelsId(Long channelsId) {
		this.channelsId = channelsId;
	}

	public Long getSitePageId() {
		return this.sitePageId;
	}

	public void setSitePageId(Long sitePageId) {
		this.sitePageId = sitePageId;
	}

}
