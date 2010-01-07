/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-7 ÏÂÎç08:43:45
 */
public class ChannelTreeComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree channelTree;
	
	private Tab tab;
	private Tabs tabs;
	private Tabpanel tabpanel;
	private Tabpanels tabpanels;
	
	private String cmd;
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		System.out.println("doBeforeCompose:parent-"+parent);
		tabs = (Tabs)parent.getFellowIfAny("contentTabs");
		tabpanels = (Tabpanels)parent.getFellowIfAny("contentTabpanels");
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		System.out.println("doBeforeComposeChildren:comp-"+comp);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		Object object = arg.get("cmd");
		if(object!=null){
			cmd = (String)object;
		}
		
		channelTree = new Tree();
		channelTree.setModel(new ChannelTreeModel(0L));
		channelTree.setTreeitemRenderer(new ChannelTreeitemRenderer());
		channelTree.addEventListener(Events.ON_SELECT, new EventListenerImpl(cmd));
		
		channelTree.setParent(comp);
		System.out.println("tabs:"+tabs);
		System.out.println("tabs:"+this.tabpanels);
	}
	
	class EventListenerImpl implements EventListener {
		
		private String cmd;
		
		public EventListenerImpl(String cmd) {
			this.cmd = cmd;
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem selected = channelTree.getSelectedItem();
			ChannelBean bean = (ChannelBean)selected.getValue();
			String id = cmd + "_" + bean.getChannelId();
			createPanel(id, selected.getLabel(), null, null);
		}
		
		void createPanel(String id, String name, String uri, Map<String, String> map){
			try {
				tab = new Tab(name);
				tab.setId(id);
				tab.setClosable(true);
				tab.setSelected(true);
				tabpanel = new Tabpanel();
				tabpanel.setHeight("100%");
				//Component comp = Executions.createComponents(uri, null, map);
				//tabpanel.appendChild(comp);
				
				tabs.appendChild(tab);
				tabpanels.appendChild(tabpanel);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
