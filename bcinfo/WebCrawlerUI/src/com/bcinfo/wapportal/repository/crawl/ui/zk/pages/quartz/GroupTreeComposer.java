/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.SimpleTreeModel;
import org.zkoss.zul.SimpleTreeNode;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

/**
 * @author dongq
 * 
 *         create time : 2010-1-18 ÏÂÎç12:05:33
 */
public class GroupTreeComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tab tab;
	private Tabs tabs;
	private Tabpanel tabpanel/*,jobGroupPanel,triGroupPanel*/;
	private Tabpanels tabpanels;
	private Component parentComp;
	private Tree jobGroupTree,triGroupTree;
	private SimpleTreeModel model;
	private SimpleTreeNode root;
	private Treeitem selected;
	
	private Scheduler scheduler;
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			System.out.println("doBeforeCompose:parent-"+parent);
			tabs = (Tabs)parent.getFellowIfAny("contentTabs");
			tabpanels = (Tabpanels)parent.getFellowIfAny("contentTabpanels");
			this.parentComp = parent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		initJobGroupTree();
		initTriggerGroupTree();
		
	}
	
	void initJobGroupTree() {
		try {
			List<String> list = new ArrayList<String>();
			String[] jobGroupNames = this.scheduler.getJobGroupNames();
			if(jobGroupNames!=null&&jobGroupNames.length>0){
				for(String name : jobGroupNames){
					list.add(name);
				}
			}
			if(list.isEmpty()) list.add("JobDefault");
			root = new SimpleTreeNode("job", list);
			model = new SimpleTreeModel(root);
			jobGroupTree.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void initTriggerGroupTree() {
		try {
			List<String> list = new ArrayList<String>();
			String[] triggerGroupNames = this.scheduler.getTriggerGroupNames();
			if(triggerGroupNames!=null&&triggerGroupNames.length>0){
				for(String name : triggerGroupNames){
					list.add(name);
				}
			}
			if(list.isEmpty()) list.add("TriDefault");
			root = new SimpleTreeNode("trigger", list);
			model = new SimpleTreeModel(root);
			triGroupTree.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onSelect$jobGroupTree() {
		selected = this.jobGroupTree.getSelectedItem();
		if(selected!=null){
			String value = selected.getValue().toString();
			String id = "job_"+value;
			boolean bln = false;
			List<Tab> list = tabs.getChildren();
			if(list!=null&&!list.isEmpty()){
				for(Tab _tab : list){
					if(id.equals(_tab.getId())){
						_tab.setSelected(true);
						bln = true;
						break;
					}
				}
			}
			if(!bln){
				Map<String, String> map = new HashMap<String, String>();
				map.put("groupName", value);
				createPanel(id, "JOB - "+selected.getLabel(), "pages/quartz/job_list.zul", map);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onSelect$triGroupTree() {
		selected = this.triGroupTree.getSelectedItem();
		if(selected!=null){
			String value = selected.getValue().toString();
			String id = "trigger_"+value;
			boolean bln = false;
			List<Tab> list = tabs.getChildren();
			if(list!=null&&!list.isEmpty()){
				for(Tab _tab : list){
					if(id.equals(_tab.getId())){
						_tab.setSelected(true);
						bln = true;
						break;
					}
				}
			}
			if(!bln){
				Map<String, String> map = new HashMap<String, String>();
				map.put("groupName", value);
				createPanel(id, "TRIGGER - "+selected.getLabel(), "pages/quartz/trigger_list.zul", map);
			}
		}
	}
	
	void createPanel(String id, String name, String uri, Map<String, String> map){
		try {
			tab = new Tab(name);
			tab.setId(id);
			tab.setClosable(true);
			tab.setSelected(true);
			tabpanel = new Tabpanel();
			tabpanel.setHeight("100%");
			Component comp =  Executions.createComponents(uri, parentComp, map);
			tabpanel.appendChild(comp);
			
			tabs.appendChild(tab);
			tabpanels.appendChild(tabpanel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class TreeitemOnSelectEventListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			
		}
	}
	
	class TreeitemRendererImpl implements TreeitemRenderer {
		@Override
		public void render(Treeitem item, Object data) throws Exception {
			item.setLabel(data.toString().trim());
			item.setValue(data.toString().trim());
		}
	}
}
