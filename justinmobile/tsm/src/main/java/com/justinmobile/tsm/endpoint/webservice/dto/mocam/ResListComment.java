package com.justinmobile.tsm.endpoint.webservice.dto.mocam;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.justinmobile.tsm.endpoint.webservice.NameSpace;
import com.justinmobile.tsm.endpoint.webservice.dto.Status;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResListComment", namespace = NameSpace.CM)
public class ResListComment {
	
	@XmlElement(namespace = NameSpace.CM)
	private String commandID;

	@XmlElement(namespace = NameSpace.CM)
	private Status status;

	@XmlElement(namespace = NameSpace.CM)
	private Integer nextPageNumber;
	
	@XmlElement(namespace = NameSpace.CM)
	private Integer totalPage;
	
	@XmlElement(namespace = NameSpace.CM)
	private AppCommentList appCommentList;

	public String getCommandID() {
		return commandID;
	}

	public void setCommandID(String commandID) {
		this.commandID = commandID;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getNextPageNumber() {
		return nextPageNumber;
	}

	public void setNextPageNumber(Integer nextPageNumber) {
		this.nextPageNumber = nextPageNumber;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public AppCommentList getAppCommentList() {
		return appCommentList;
	}

	public void setAppCommentList(AppCommentList appCommentList) {
		this.appCommentList = appCommentList;
	}

}
