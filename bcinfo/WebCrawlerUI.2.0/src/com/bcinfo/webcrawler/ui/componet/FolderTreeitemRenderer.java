/**
 * 
 */
package com.bcinfo.webcrawler.ui.componet;

import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.webcrawler.model.Folder;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 下午05:31:34
 */
public class FolderTreeitemRenderer implements TreeitemRenderer {

	public void render(Treeitem item, Object object) throws Exception {
		if(object instanceof Folder) {
			Folder folder = (Folder)object;
			item.setLabel(folder.getFolderName());
			item.setValue(folder);
		}
	}

}
