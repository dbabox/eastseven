/**
 * 
 */
package com.bcinfo.webcrawler.service.impl;

import java.util.List;

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

	private Folder root = null;

	public List<Folder> getTopFolders(Object root) {
		try {
			return folderDao.getFolders((Long)root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Folder initFolderTree() throws Exception {
		root = folderDao.getRoot();
		List<Folder> all = folderDao.getFolders(-1L);
		// int levels = folderDao.getDepth();

		for (Folder folder : all) {
			addFolder(folder);
		}
		/*
		 * for(long index = 1; index < 11; index ++) { Folder folder = new
		 * Folder(); folder.setFolderId(index);
		 * folder.setFolderName("频道-"+String.valueOf(index)); long limited =
		 * Math.round(index); for(long i = 1; i < limited; i++) { Folder f = new
		 * Folder(); f.setFolderId(i); f.setFolderName("频道-" +
		 * String.valueOf(index) + "-" + i); f.setParentFolder(index);
		 * folder.addChild(f); } folder.setParentFolder(0L);
		 * root.addChild(folder); }
		 */
		return root;
	}

	private void addFolder(Folder folder) throws Exception {
		if (root.equals(folder))
			return;
		if (folderDao.hasChild(folder.getFolderId())) {
			List<Folder> children = folderDao.getFolders(folder.getFolderId());
			folder.addChild(children);
			for (Folder child : children) {
				addFolder(child);
			}
		} else {
			root.addChild(folder);
		}
	}

	public Folder getChild(Folder parent, int index) {
		try {
			return folderDao.getFolders(parent.getFolderId()).get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getChildCount(Folder parent) {
		
		try {
			return folderDao.getChildCount(parent.getFolderId()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public boolean isLeaf(Folder folder) {
		try {
			return !folderDao.hasChild(folder.getFolderId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
