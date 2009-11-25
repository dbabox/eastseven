package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.catchconfig;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.CrawlDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.CrawlBean;

/**
 * 
 * @author dongq
 * 
 *         create time : 2009-11-23 下午04:09:12
 */
public class CatchConfigComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Listbox catchList;
	private Textbox crawlUrl;
	
	private ListitemRenderer renderer = new CrawlListitemRenderer();
	
	private CrawlDao dao; 
	private Long channelId;
	private boolean live = true;
	
	public CatchConfigComposer() {
		dao = new CrawlDao();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(arg != null){
			channelId = (Long)arg.get("channelId");
		}
		
		if(channelId != null){
			List<CrawlBean> list = dao.getCrawlList(channelId);
			catchList.setModel(new ListModelList(list, live));
			catchList.setItemRenderer(renderer);
			catchList.setMultiple(true);
		}
	}

	public void onClick$add(){
		alert("TODO "+this.crawlUrl.getValue());
	}
	
	public void onClick$setup(){
		//多选
		alert("TODO setup");
	}
	
	public void onClick$stop(){
		//多选
		alert("TODO stop");
	}
	
	class CrawlListitemRenderer implements ListitemRenderer{
		@Override
		public void render(Listitem item, Object data) throws Exception {		
			CrawlBean bean = (CrawlBean)data;
			
			item.appendChild(new Listcell(bean.getCrawlId().toString()));
			item.appendChild(new Listcell(bean.getChannelName()));
			item.appendChild(new Listcell(bean.getCrawlUrl()));
			item.appendChild(new Listcell(bean.getCrawlStatus()));
			item.appendChild(new Listcell(bean.getCreateTime()));
			
			item.setValue(data);
		}
	}
}
