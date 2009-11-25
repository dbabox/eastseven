package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2009-11-23 ÏÂÎç06:16:11
 */
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;
	private String password;
	private String localCode;
	private String userStatus;
	private String createTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "UserBean [createTime=" + createTime + ", localCode="
				+ localCode + ", password=" + password + ", userId=" + userId
				+ ", userName=" + userName + ", userStatus=" + userStatus + "]";
	}

}
