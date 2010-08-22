/**
 * 
 */
package com.bcinfo.webcrawler.service.impl;

import com.bcinfo.webcrawler.dao.FolderDao;
import com.bcinfo.webcrawler.model.Folder;
import com.bcinfo.webcrawler.service.FolderService;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午04:02:17
 */
public class FolderServiceImpl implements FolderService {

	private FolderDao folderDao;
	
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
	public Folder initFolderTree() throws Exception {
		Folder root = new Folder();
		root.setFolderId(0L);
		root.setFolderName("root");
		for(long index = 1; index < 11; index ++) {
			Folder folder = new Folder();
			folder.setFolderId(index);
			folder.setFolderName("频道-"+String.valueOf(index));
			long limited = Math.round(index);
			for(long i = 1; i < limited; i++) {
				Folder f = new Folder();
				f.setFolderId(i);
				f.setFolderName("频道-" + String.valueOf(index) + "-" + i);
				f.setParentFolder(index);
				folder.addChild(f);
			}
			folder.setParentFolder(0L);
			root.addChild(folder);
		}
		
		return root;
	}

}
