/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.QuartzDaoService;

/**
 * @author dongq
 * 
 *         create time : 2009-12-10 下午04:26:55
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tabs tabs;//表头
	private Tabpanels tabpanels;//表体
	private Grid grid;
	private Tree tree;
	
	private QuartzDaoService dao;
	
	public BorderLayoutComposer() {
		dao = new QuartzDaoService();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initGrid();
		initTree();
	}
	
	public void onClick$quartzBtn() {
		Tabpanel panel = new Tabpanel();
		panel.setHeight("100%");
		execution.createComponents("/pages/quartz/quartz_table_info.zul", panel, null);
		Tab tab = new Tab("Quartz");
		tab.setClosable(true);
		tab.setSelected(true);
		this.tabs.appendChild(tab);
		this.tabpanels.appendChild(panel);
	}
	
	private void initTree() throws Exception {
		List<String> children = dao.getAllTablesName();
		
		TreeModel treeModel = new TreeModelImpl(children);
		TreeitemRenderer renderer = new TreeitemRendererImpl();
		
		tree.setModel(treeModel);
		tree.setTreeitemRenderer(renderer);
		tree.addEventListener(Events.ON_SELECT, new TreeSelectEventListener(tabs,tabpanels));
	}
	
	private void initGrid() throws Exception {
		
		Properties properties = System.getProperties();
		Set<Object> keySet = properties.keySet();
		List<KeyValue> list = new ArrayList<KeyValue>();
		for(Object key : keySet){
			if(key!=null&&!"".equals(key.toString()))
				list.add(new KeyValue(key.toString(), properties.get(key.toString()).toString()));
			//System.out.println(key+" : "+properties.getProperty(key.toString()));
		}
		grid.setModel(new ListModelList(list, true));
		RowRenderer renderer = new GridRowRenderer();
		grid.setRowRenderer(renderer);
	}
	
	class TreeModelImpl extends AbstractTreeModel {

		List<String> list;
		
		public TreeModelImpl(List<String> root) {
			super(root);
			list = root;
		}

		private static final long serialVersionUID = 1L;

		public Object getChild(Object parent, int index) {
			Object obj = list.get(index);
			return obj;
		}

		@SuppressWarnings("unchecked")
		public int getChildCount(Object parent) {
			int childCount = 0;
			if(parent instanceof List){
				childCount = ((List)parent).size();
			}
			return childCount;
		}

		public boolean isLeaf(Object node) {
			boolean isLeaf = true;
			if(node instanceof String) isLeaf = false;
			return isLeaf;
		}
		
	}
	
	class TreeitemRendererImpl implements TreeitemRenderer {

		public void render(Treeitem item, Object data) throws Exception {
			item.setLabel(data.toString());
			item.setValue(data);
		}
		
	}
	
	class TreeSelectEventListener implements EventListener {
		
		private Tabs tabs;
		private Tabpanels tabpanels;
		private Treeitem selected;
		
		public TreeSelectEventListener(Tabs tabs,Tabpanels tabpanels) {
			this.tabs = tabs;
			this.tabpanels = tabpanels;
		}
		
		public void onEvent(Event event) throws Exception {
			selected = tree.getSelectedItem();
			if(selected!=null){
				String label = selected.getLabel();
				Tabpanel panel = new Tabpanel();
				panel.setHeight("100%");
				Map<String, String> map = new HashMap<String, String>();
				map.put("tableName", label);
				execution.createComponents("/pages/quartz/quartz_table_info.zul", panel, map);
				Tab tab = new Tab(label);
				tab.setClosable(true);
				tab.setSelected(true);
				this.tabs.appendChild(tab);
				this.tabpanels.appendChild(panel);
			}
		}
	}
	
	class GridRowRenderer implements RowRenderer {
		public void render(Row row, Object data) throws Exception {
			if(data instanceof KeyValue){
				KeyValue obj = (KeyValue)data;
				row.appendChild(new Label(obj.getKey()));
				row.appendChild(new Label(obj.getValue()));
				
			}
			
		}
	}
	
	class KeyValue {
		private String key;
		private String value;
		
		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
	}
}
