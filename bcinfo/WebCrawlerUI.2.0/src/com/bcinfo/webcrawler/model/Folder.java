package com.bcinfo.webcrawler.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.bcinfo.webcrawler.model.newwap.WapSiteFolder;

/**
 * 
 * @author dongq
 * 
 *         create time : 2010-8-20 下午04:11:05
 */
public class Folder extends WapSiteFolder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<Folder> children = new HashSet<Folder>();

	public Set<Folder> getChildren() {
		return children;
	}

	public void addChild(Folder child) {
		this.children.add(child);
	}

	public void addChild(Collection<Folder> children) {
		this.children.addAll(children);
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		Folder folder = (Folder) obj;
		return this.getFolderId().longValue() == folder.getFolderId().longValue();
	}

	@Override
	public String toString() {
		return "ChannelBean [" +
				"getFolderId()=" + getFolderId() + 
				", getFolderName()=" + getFolderName() + 
				", getStatus()=" + getStatus() + 
				", getFolderLevel()=" + getFolderLevel() + 
				", getParentFolder()=" + getParentFolder() + "]";
	}

}
