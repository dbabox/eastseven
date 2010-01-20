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
import org.zkoss.zul.Textbox;

/**
 * @author dongq
 * 
 *         create time : 2010-1-18 ÏÂÎç02:39:28
 */
public class JobDetailComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Textbox jobName;
	private Listbox listbox, selectGroup, selectTrigger;
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
		jobName.setId("tb_job_name_"+groupName);
		selectGroup.setId("lb_qrtz_job_selectg_"+groupName);
		selectTrigger.setId("lb_qrtz_job_selectt_"+groupName);
		listbox.setId("lb_qrtz_job_"+groupName);
		if(this.scheduler!=null){
			String[] jobNames = this.scheduler.getJobNames(groupName);
			List<String> list = new ArrayList<String>();
			if(jobNames!=null&&jobNames.length>0){
				for(String name : jobNames){
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
	
	public void onSelect$listbox() {
		
	}
	
	public void onSelect$selectGroup() {
		
	}
	
	public void onSelect$selectTrigger() {
		
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
