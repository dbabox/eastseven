/**
 * 
 */
package com.bcinfo.crawl.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dongq
 * 
 *         create time : 2010-5-17 ÏÂÎç09:42:13
 */
public class Channel implements Serializable {

	private static final long serialVersionUID = 6951782061988550579L;

	private Long id;
	private String name;
	private Channel parent;
	private Set<Channel> child = new HashSet<Channel>();

	public Channel() {
	}

	public Channel(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Channel getParent() {
		return parent;
	}

	public void setParent(Channel parent) {
		this.parent = parent;
	}

	public Set<Channel> getChild() {
		return child;
	}

	public void setChild(Set<Channel> child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return "Channel [id=" + id + ", name=" + name + "]";
	}

}
