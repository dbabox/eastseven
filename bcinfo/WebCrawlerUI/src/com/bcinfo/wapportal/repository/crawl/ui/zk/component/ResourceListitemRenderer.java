/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import org.zkoss.zul.Html;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ResourceBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-8 œ¬ŒÁ05:50:44
 */
public class ResourceListitemRenderer implements ListitemRenderer {

	ResourceBean bean;
	Html html;
	Listcell cell;
	int rowNum;
	
	public ResourceListitemRenderer() {
		rowNum = 1;
	}
	
	@Override
	public void render(Listitem item, Object data) throws Exception {
		bean = (ResourceBean) data;
		item.appendChild(new Listcell());
		item.appendChild(new Listcell(String.valueOf(rowNum)));
		item.appendChild(new Listcell(bean.getChannelName()));
		
		cell = new Listcell();
		html = new Html("<a href='"+bean.getLink()+"' target='_blank'>"+bean.getTitle()+"</a>");
		html.setParent(cell);
		item.appendChild(cell);
		
		if("1".equals(bean.getStatus()))
			item.appendChild(new Listcell("“—…Û"));
		else
			item.appendChild(new Listcell("Œ¥…Û"));
		item.appendChild(new Listcell(bean.getPics()));
		item.appendChild(new Listcell(bean.getWords()));
		item.appendChild(new Listcell(bean.getCreateTime()));
		item.setValue(bean);
		rowNum++;
	}
}
