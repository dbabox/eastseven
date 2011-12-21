package com.justinmobile.tsm.endpoint.webservice.dto.mocam;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.justinmobile.tsm.endpoint.webservice.NameSpace;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResLoginOrRegister", namespace = NameSpace.CM)
public class ResLoginOrRegister extends ResExecAPDU {
	
	@XmlElement(name = "MOCAMVersion", namespace = NameSpace.CM)
	private String mocamVersion;
	
	@XmlElement(name = "URL", namespace = NameSpace.CM)
	private String url;

	public String getMocamVersion() {
		return mocamVersion;
	}

	public void setMocamVersion(String mocamVersion) {
		this.mocamVersion = mocamVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
