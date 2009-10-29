/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-9-8 上午10:46:49<br>
 *         资源对象<br>
 *         SQL> desc WAP_RESOURCE;<br>
 *         Name Null? Type<br>
 *         ----------------------------- -------- ----------------------------<br>
 *         RES_ID NOT NULL NUMBER(38)<br>
 *         SPCP_ID NUMBER(38)<br>
 *         RES_TYPE_ID NUMBER(38)<br>
 *         SPCP_CODE VARCHAR2(20)<br>
 *         FIRSTNAME VARCHAR2(120)<br>
 *         PASSNAME VARCHAR2(120)<br>
 *         CHECK_FLAG CHAR(1)<br>
 *         PRICE NUMBER(9,3)<br>
 *         DOWN_COUNT NUMBER(38)<br>
 *         CLICK_SUM NUMBER(38)<br>
 *         POST_DATE DATE<br>
 *         CHECK_DATE DATE<br>
 *         RES_AUTHOR VARCHAR2(30)<br>
 *         AVAIL_DATE DATE<br>
 *         RES_STATUS CHAR(1)<br>
 *         RES_CONTENT VARCHAR2(4000)<br>
 *         STORE_FILEPATH VARCHAR2(255)<br>
 *         RES_SIZE NUMBER(10)<br>
 *         CORP_NAME VARCHAR2(90)<br>
 *         COPYRIGHT VARCHAR2(120)<br>
 *         CREATE_TIME DATE<br>
 *         SEND_FLAG CHAR(1)<br>
 *         RES_DESC VARCHAR2(400)<br>
 *         EXTERNAL1 VARCHAR2(300)<br>
 *         EXTERNAL2 VARCHAR2(300)<br>
 *         EXTERNAL3 VARCHAR2(300)<br>
 *         DOWN_INTEGRAL NUMBER(38)<br>
 *         CP_EMP_ID NUMBER(5)<br>
 *         CP_ID NUMBER(5)<br>
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Folder folder;
	private String resourceContent;// 若是图片，则保持img标签
	private int resourceType;// 3:pic,8:words
	private String resourcePath;// 图片http链接

	private String firstName;// 资源名称
	private String resAuthor = "bcinfo";// 所属作者
	private String availDate = "2099-12-30";// 有效期
	private int downIntegral = 0;// 下载送分
	private String corpName = "bcinfo";// 公司名称
	private String copyRight = "bcinfo";// 版权
	private String resDesc;// 描述
	private String resContent;// 内容
	private int spcpId = 1;
	private String checkFlag = "0";
	private String resStatus = "1";

	public Resource() {
		super();
	}

	public Resource(int id, Folder folder, String resourceContent,
			int resourceType, String resourcePath) {
		super();
		this.id = id;
		this.folder = folder;
		this.resourceContent = resourceContent;
		this.resourceType = resourceType;
		this.resourcePath = resourcePath;
		this.resDesc = folder.getTitle();
		this.resContent = resourceContent;
	}

	public Resource(int id, Folder folder, String resourceContent,
			int resourceType) {
		super();
		this.id = id;
		this.folder = folder;
		this.resourceContent = resourceContent;
		this.resourceType = resourceType;
		this.resDesc = folder.getTitle();
		this.resContent = resourceContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public String getResourceContent() {
		return resourceContent;
	}

	public void setResourceContent(String resourceContent) {
		this.resourceContent = resourceContent;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public int getDownIntegral() {
		return downIntegral;
	}

	public void setDownIntegral(int downIntegral) {
		this.downIntegral = downIntegral;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getResAuthor() {
		return resAuthor;
	}

	public String getAvailDate() {
		return availDate;
	}

	public String getCorpName() {
		return corpName;
	}

	public String getCopyRight() {
		return copyRight;
	}

	public String getResDesc() {
		return resDesc;
	}

	public String getResContent() {
		return resContent;
	}

	public int getSpcpId() {
		return spcpId;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public String getResStatus() {
		return resStatus;
	}

	@Override
	public String toString() {
		return "Resource [availDate=" + availDate + ", checkFlag=" + checkFlag
				+ ", copyRight=" + copyRight + ", corpName=" + corpName
				+ ", downIntegral=" + downIntegral + ", firstName=" + firstName
				+ ", folder=" + folder + ", id=" + id + ", resAuthor="
				+ resAuthor + ", resContent=" + resContent + ", resDesc="
				+ resDesc + ", resStatus=" + resStatus + ", resourceContent="
				+ resourceContent + ", resourcePath=" + resourcePath
				+ ", resourceType=" + resourceType + ", spcpId=" + spcpId + "]";
	}

}
