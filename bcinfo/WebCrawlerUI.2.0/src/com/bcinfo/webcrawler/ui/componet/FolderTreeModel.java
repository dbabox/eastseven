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
		try {
			folders.addAll(folderService.initFolderTree().getChildren());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getChild(Object parent, int index) {
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			return folder.getChildren().iterator().next();
		} else {
			return folders.get(index);
		}
	}

	public int getChildCount(Object parent) {
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			return folder.getChildren().size();
		} else {
			return folders.size();
		}
	}

	public boolean isLeaf(Object parent) {
		if(parent instanceof Folder) {
			Folder folder = (Folder)parent;
			return folder.isLeaf();
		}
		return false;
	}

	
}
