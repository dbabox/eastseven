package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.catchconfig;

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
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeModel;
import com.bcinfo.wapportal.repository.crawl.ui.zk.component.ChannelTreeitemRenderer;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-11-23 ÏÂÎç03:57:57
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree channelTree;
	private Div contentDiv;

	private EventListener listener = new ChannelTreeitemListener();
	private TreeModel model = new ChannelTreeModel(0L);
	private TreeitemRenderer renderer = new ChannelTreeitemRenderer();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		channelTree.setModel(model);
		channelTree.setTreeitemRenderer(renderer);
		channelTree.addEventListener(Events.ON_SELECT, listener);

	}

	class ChannelTreeitemListener implements EventListener {
		@Override
		public void onEvent(Event event) throws Exception {
			contentDiv.getChildren().clear();
			Treeitem data = channelTree.getSelectedItem();
			ChannelBean bean = (ChannelBean) data.getValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channelId", bean.getChannelId());
			Executions.createComponents("/pages/catch/catch_config.zul", contentDiv, map);
		}

	}
}
