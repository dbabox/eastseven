/**
 * 
 */
package com.bcinfo.webcrawler.ui.componet;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;

import com.bcinfo.webcrawler.model.Folder;
import com.bcinfo.webcrawler.service.FolderService;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午05:13:18
 */
public class FolderTreeModel extends AbstractTreeModel {

	private static final long serialVersionUID = 1L;
	
	private FolderService folderService;
	private List<Folder> folders = new ArrayList<Folder>();
	
	public FolderTreeModel(Object root) {
		super(root);
		folderService = (FolderService)SpringUtil.getBean("folderService");
		folders = folderService.getTopFolders(root);
	}

	public Object getChild(Object parent, int index) {
		Folder child = null;
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			child = folderService.getChild(folder, index);
		} else {
			child = folders.get(index);
		}
		return child;
	}

	public int getChildCount(Object parent) {
		int count = 0;
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			count = folderService.getChildCount(folder);
		} else {
			count = folders.size();
		}
		return count;
	}

	public boolean isLeaf(Object parent) {
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			return folderService.isLeaf(folder);
		}
		return true;
	}

	
}
