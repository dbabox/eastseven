/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.system.log;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeModel;
import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeitemRenderer;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 *         create time : 2009-12-11 上午11:14:10
 */
public class AppLogComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree channelTree;
	private EventListener listener = null;
	private TreeModel model = new ChannelTreeModel(0L);
	private TreeitemRenderer renderer = new ChannelTreeitemRenderer();
	
	private Tabbox tabbox;
	private Tabs tabs;
	private Tab tab;
	private Tabpanels tabpanels;
	private Tabpanel tabpanel;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//初始化树形菜单
		channelTree.setModel(model);
		channelTree.setTreeitemRenderer(renderer);
		channelTree.addEventListener(Events.ON_SELECT, new ChannelTreeitemListener(tabs, tabpanels));
		//
	}
	
	class ChannelTreeitemListener implements EventListener {
		
		private Tabs tabs;
		private Tab tab;
		private Tabpanels tabpanels;
		private Tabpanel tabpanel;
		
		public ChannelTreeitemListener(Tabs tabs, Tabpanels tabpanels) {
			this.tabs = tabs;
			this.tabpanels = tabpanels;
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem data = channelTree.getSelectedItem();
			ChannelBean bean = (ChannelBean) data.getValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channelId", bean.getChannelId());
			
			tab = new Tab(bean.getChannelName());
			tab.setClosable(true);
			tab.setSelected(true);
			tabpanel = new Tabpanel();
			Executions.createComponents("/pages/system/log/app_log_grid.zul", tabpanel, map);
			tabpanel.setHeight("100%");
			
			tabs.appendChild(tab);
			tabpanels.appendChild(tabpanel);
		}

	}
}
