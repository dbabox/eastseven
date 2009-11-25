package com.bcinfo.wapportal.repository.crawl.ui.zk.domain;

import java.io.Serializable;

/**
 * @author dongq
 * 
 */
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long oid;
	private Long pid;
	private String label;
	private String link;
	private Long level;

	public MenuBean(Long oid, Long pid, String label, String link, Long level) {
		super();
		this.oid = oid;
		this.pid = pid;
		this.label = label;
		this.link = link;
		this.level = level;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "MenuBean [label=" + label + ", level=" + level + ", link="
				+ link + ", oid=" + oid + ", pid=" + pid + "]";
	}

}
