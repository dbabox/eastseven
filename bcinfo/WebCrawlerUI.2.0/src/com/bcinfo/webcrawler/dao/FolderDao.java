/**
 * 
 */
package com.bcinfo.webcrawler.dao;

import java.util.List;

import com.bcinfo.webcrawler.model.Folder;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午03:05:08
 */
public interface FolderDao {

	public List<Folder> getFolders(Long parent) throws Exception;
	
	public Long getChildCount(Long parent) throws Exception;
	
}
