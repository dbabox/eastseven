/**
 * 
 */
package com.bcinfo.webcrawler.service;

import java.util.List;

import com.bcinfo.webcrawler.model.Folder;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午03:46:35
 */
public interface FolderService {

	public Folder initFolderTree() throws Exception;
	
	public List<Folder> getTopFolders(Object root);
	
	public Folder getChild(Folder parent, int index);
	
	public int getChildCount(Folder parent);
	
	public boolean isLeaf(Folder folder);
}
