package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.channel;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Window;

import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeModel;
import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeitemRenderer;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ChannelDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq<br>
 *         Channel Layout
 * 
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree channelTree;
	private Div contentDiv;
	
	private EventListener listener = new ChannelTreeitemListener();
	private TreeModel model = new ChannelTreeModel(0L);
	private TreeitemRenderer renderer = new ChannelTreeitemRenderer();
	
	public BorderLayoutComposer() {
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		channelTree.setModel(model);
		channelTree.setTreeitemRenderer(renderer);
		channelTree.addEventListener(Events.ON_SELECT, listener);
		
		ChannelBean bean = new ChannelBean(0L, 0L, "root", "", "", "");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bean", bean);
		Executions.createComponents("/pages/channel/channel_management.zul", contentDiv, map);
	}

	public void onClick$addRootChannel(){
		contentDiv.getChildren().clear();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("channelPid", 0L);
		
		Window form = (Window)Executions.createComponents("/pages/channel/channel_form.zul", contentDiv, map);
		try {
			form.setTitle("Form");
			Button save = (Button)form.getFellow("save");
			save.addEventListener(Events.ON_CLICK, new SaveButtonEventListener(form));
			form.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class SaveButtonEventListener implements EventListener {
		
		private Window win;
		private Longbox channelPid;
		private Textbox channelName;
		private Textbox channelPath;
		private Textbox channelIndex;
		
		private ChannelDao dao;
		private ChannelBean bean;
		
		public SaveButtonEventListener(Window win) {
			this.win = win;
			dao = new ChannelDao();
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			if(win != null){
				channelPid = (Longbox)this.win.getFellow("channelPid");
				channelName = (Textbox)this.win.getFellow("channelName");
				channelPath = (Textbox)this.win.getFellow("channelPath");
				channelIndex = (Textbox)this.win.getFellow("channelIndex");
				bean = new ChannelBean(null, channelPid.getValue(), channelName.getValue(), channelPath.getValue(), channelIndex.getValue(), null);
				boolean isSave = dao.save(bean);
				if(isSave){
					win.onClose();
					channelTree.setModel(new ChannelTreeModel(0L));
					channelTree.setTreeitemRenderer(renderer);
					channelTree.addEventListener(Events.ON_SELECT, listener);
				}
			}
		}
	}
	
	class ChannelTreeitemListener implements EventListener {

		@Override
		public void onEvent(Event event) throws Exception {
			contentDiv.getChildren().clear();
			Treeitem data = channelTree.getSelectedItem();
			System.out.println(event.getName() + " : " + data.getLabel());
			ChannelBean bean = (ChannelBean) data.getValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bean", bean);
			Executions.createComponents("/pages/channel/channel_management.zul", contentDiv, map);
		}

	}
}
