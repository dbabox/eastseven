/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.quartz;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 * @author dongq
 * 
 *         create time : 2010-1-18 ÏÂÎç03:02:51
 */
public class TriggerComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Listbox listbox;
	private ListModelList model;
	private String groupName;
	
	private Scheduler scheduler;
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		groupName = (String)arg.get("groupName");
		listbox.setId("listbox_qrtz_tri_"+groupName);
		if(this.scheduler!=null){
			String[] triNames = this.scheduler.getTriggerNames(groupName);
			List<String> list = new ArrayList<String>();
			if(triNames!=null&&triNames.length>0){
				for(String name : triNames){
					list.add(name);
				}
			}
			if(!list.isEmpty()){
				model = new ListModelList(list,true);
				listbox.setModel(model);
				listbox.setItemRenderer(new ListitemRendererImpl());
			}
		}
	}
	
	class ListitemRendererImpl implements ListitemRenderer {
		int rownum = 1;
		@Override
		public void render(Listitem item, Object data) throws Exception {
			item.appendChild(new Listcell(String.valueOf(rownum)));
			item.appendChild(new Listcell(data.toString()));
			item.setValue(data);
			rownum++;
		}
	}
}
