/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-13 ÏÂÎç06:10:46
 */
public class TreeitemRendererImpl implements TreeitemRenderer {
	private String channelId;
	private String channelName;
	private Treerow treerow;

	@Override
	public void render(Treeitem item, Object data) throws Exception {
		treerow = new Treerow();
		ChannelBean bean = (ChannelBean) data;
		channelId = bean.getChannelId().toString();
		channelName = bean.getChannelName();
		treerow.appendChild(new Treecell(channelId));
		treerow.appendChild(new Treecell(channelName));
		item.appendChild(treerow);
		item.setValue(data);
	}

}
