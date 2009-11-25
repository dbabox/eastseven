package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 */
public class ChannelTreeitemRenderer implements TreeitemRenderer {

	@Override
	public void render(Treeitem item, Object data) throws Exception {
		if (data instanceof ChannelBean) {
			ChannelBean bean = (ChannelBean) data;
			item.setLabel(bean.getChannelName());
			item.setValue(bean);
		}
	}

}
