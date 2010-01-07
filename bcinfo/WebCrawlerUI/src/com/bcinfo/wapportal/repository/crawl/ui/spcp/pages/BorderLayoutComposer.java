/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.spcp.dao.SpcpDao;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.PageFolderBean;

/**
 * @author dongq
 * 
 *         create time : 2009-12-23 ÏÂÎç03:26:42
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree folderTree;
//	private EventListener listener = null;
//	private TreeModel model = new ChannelTreeModel(0L);
//	private TreeitemRenderer renderer = new ChannelTreeitemRenderer();

//	private Tabbox tabbox;
	private Tabs tabs;
	private Tab tab;
	private Tabpanels tabpanels;
	private Tabpanel tabpanel;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		folderTree.setTreeitemRenderer(new FolderTreeitemRenderer());
		folderTree.setModel(new FolderTreeModel(null));
		folderTree.addEventListener(Events.ON_SELECT, new FolderTreeitemListener());
	}

	class FolderTreeitemListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem selected = folderTree.getSelectedItem();
			if(selected!=null){
				String folderId = (String)selected.getValue();
				Map<String, String> map = new HashMap<String, String>();
				map.put("folderId", folderId);
				tab = new Tab(folderId);
				tab.setClosable(true);
				tab.setSelected(true);
				tabpanel = new Tabpanel();
				Executions.createComponents("/pages/spcp/spcp_list.zul", tabpanel, map);
				tabpanel.setHeight("100%");
				
				tabs.appendChild(tab);
				tabpanels.appendChild(tabpanel);
			}
		}
	}
	
	class FolderTreeitemRenderer implements TreeitemRenderer {
		
		public void render(Treeitem item, Object data) throws Exception {
			PageFolderBean bean = (PageFolderBean)data;
			item.setLabel(bean.getFolderName());
			item.setValue(bean.getFolderId());
		}
	}
	
	class FolderTreeModel extends AbstractTreeModel {
		private static final long serialVersionUID = 1L;

		private SpcpDao dao;
		private List<PageFolderBean> list;
		private PageFolderBean bean;
		
		public FolderTreeModel(Object root) {
			super(root);
			dao = new SpcpDao();
			list = dao.getPageFolderList(null);
		}

		@Override
		public Object getChild(Object parent, int index) {
			if(parent==null){
				return list.get(index);
			}else{
				//System.out.println(" get child "+parent+"|"+index);
				bean = (PageFolderBean)parent;
				return dao.getPageFolderList(bean.getFolderId()).get(index);
			}
		}

		@Override
		public int getChildCount(Object parent) {
			if(parent==null){
				return list.size();
			}else{
				//System.out.println(" get child count:"+parent);
				bean = (PageFolderBean)parent;
				return dao.getPageFolderList(bean.getFolderId()).size();
			}
		}

		@Override
		public boolean isLeaf(Object node) {
			//System.out.println(" is leaf:"+node);
			if(node instanceof PageFolderBean){
				bean = (PageFolderBean)node;
				return dao.isLeaf(bean.getFolderId());
			}else{
				return false;
			}
		}

	}
}
