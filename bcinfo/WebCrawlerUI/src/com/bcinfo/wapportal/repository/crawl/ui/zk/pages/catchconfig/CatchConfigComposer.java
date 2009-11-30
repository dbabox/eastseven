package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.catchconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Html;
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

	//添加配置
	public void onClick$add(){
		if(crawlUrl.getValue()!= null && !"".equals(crawlUrl.getValue())){
			boolean bln = dao.save(channelId, crawlUrl.getValue());
			if(bln){
				catchList.setModel(new ListModelList(dao.getCrawlList(channelId), live));
				alert("操作成功");
			}else{
				alert("操作失败");
			}
		}else{
			alert("抓取地址不能为空");
		}
	}
	
	//启用
	@SuppressWarnings("unchecked")
	public void onClick$setup(){
		//多选
		if(catchList.getSelectedCount() == 0){
			alert("请选择要操作的记录");
		}else if(catchList.getSelectedCount() > 0){
			Set<Listitem> set = catchList.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				for(Listitem item : set){
					list.add((Long)item.getValue());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "1");
					if(bln){
						catchList.setModel(new ListModelList(dao.getCrawlList(channelId), live));
						alert("操作成功");
					}else{
						alert("操作失败");
					}
				}
			}
		}
	}
	
	//停用
	@SuppressWarnings("unchecked")
	public void onClick$stop(){
		//多选
		if(catchList.getSelectedCount() == 0){
			alert("请选择要操作的记录");
		}else if(catchList.getSelectedCount() > 0){
			Set<Listitem> set = catchList.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				for(Listitem item : set){
					list.add((Long)item.getValue());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "0");
					if(bln){
						catchList.setModel(new ListModelList(dao.getCrawlList(channelId), live));
						alert("操作成功");
					}else{
						alert("操作失败");
					}
				}
			}
		}
	}
	
	class CrawlListitemRenderer implements ListitemRenderer{
		@Override
		public void render(Listitem item, Object data) throws Exception {		
			CrawlBean bean = (CrawlBean)data;
			
			item.appendChild(new Listcell(bean.getCrawlId().toString()));
			item.appendChild(new Listcell(bean.getChannelName()));
			Listcell html = new Listcell();
			html.appendChild(new Html("<a href='"+bean.getCrawlUrl()+"' target='_blank'>"+bean.getCrawlUrl()+"</a>"));
			item.appendChild(html);
			item.appendChild(new Listcell(bean.getCrawlStatus()));
			item.appendChild(new Listcell(bean.getCreateTime()));
			
			item.setValue(bean.getCrawlId());
		}
	}
}
