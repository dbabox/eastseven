package com.justinmobile.tsm.endpoint.webservice.dto.mocam;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.justinmobile.tsm.endpoint.webservice.NameSpace;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppComment", namespace = NameSpace.CM)
public class AppComment {

	@XmlElement(namespace = NameSpace.CM)
	private String userName;

	@XmlElement(namespace = NameSpace.CM)
	private String commentTime;

	@XmlElement(namespace = NameSpace.CM)
	private Integer starGrade;
	
	@XmlElement(namespace = NameSpace.CM)
	private String commentContent;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public Integer getStarGrade() {
		return starGrade;
	}

	public void setStarGrade(Integer starGrade) {
		this.starGrade = starGrade;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

}
