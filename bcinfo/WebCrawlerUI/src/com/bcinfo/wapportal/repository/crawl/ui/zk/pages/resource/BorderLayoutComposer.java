package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.resource;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeModel;
import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeitemRenderer;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq<br>
 *         Resource Layout
 * 
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree channelTree;
	private Div contentDiv;

	private String status = "0";

	public BorderLayoutComposer() {
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (arg != null && arg.get("res_status") != null) {
			status = (String) arg.get("res_status");
		}
		channelTree.setModel(new ChannelTreeModel(0L));
		channelTree.setTreeitemRenderer(new ChannelTreeitemRenderer());

		EventListener listener = new ChannelTreeitemListener();
		channelTree.addEventListener(Events.ON_SELECT, listener);
		Treeitem item = (Treeitem) channelTree.getItems().iterator().next();
		channelTree.selectItem(item);
		Event event = new Event(Events.ON_SELECT, item);
		listener.onEvent(event);
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
			map.put("status", status);
			Executions.createComponents("/pages/resource/resource_list.zul", contentDiv, map);
		}

	}
}
