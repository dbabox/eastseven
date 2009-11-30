package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkforge.fckez.FCKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ZulEvents;

import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;
import com.bcinfo.wapportal.repository.crawl.service.impl.CrawlResourceServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ResourceDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ResourceBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * @author dongq
 * 
 */
public class ResourceListComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private boolean live = true;
	private ChannelBean bean;
	private String status;
	private ResourceDao dao;
	private CrawlResourceService service;

	private Paging resourceListboxPaging;
	private Listbox resourceListbox;
	private Grid resourceGrid;
	private Div resourceDiv;
	
	private Textbox title;
	private Datebox currentDate;
	
	public ResourceListComposer() {
		dao = new ResourceDao();
		service = new CrawlResourceServiceDefaultImpl();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		System.out.println("doAfterCompose");
		System.out.println(arg);
		
		Map map = arg;
		bean = (ChannelBean) map.get("bean");
		status = (String)map.get("status");

		List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()));
		if(list != null && !list.isEmpty()){
			
			resourceListboxPaging.setTotalSize(list.size());
			resourceListboxPaging.addEventListener(ZulEvents.ON_PAGING, new ResourceListboxPagingListener());
			
			list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), 1, resourceListboxPaging.getPageSize());
			ListModel listModel = new ListModelList(list, live);
			
			// resourceListbox.setModel(listModel);
			// resourceListbox.setItemRenderer(new ResourceListitemRenderer());
			resourceGrid.setModel(listModel);
			resourceGrid.setRowRenderer(new ResourceGridRowRenderer());
		}
		
	}

	//审核
	public void onClick$add() {
		int size = resourceListboxPaging.getPageSize();
		List<Long> list = new ArrayList<Long>();
		for (int row = 0; row < size; row++) {
			Component comp = resourceGrid.getCell(row, 1);
			if (comp instanceof Checkbox) {
				Checkbox ckb = (Checkbox) comp;
				if (ckb.isChecked())
					list.add(Long.parseLong(ckb.getId()));
			}
		}
		if (list != null && !list.isEmpty()) {
			boolean bln = false;
			if (list.size() == 1) {
				bln = dao.updateResource(list.get(0));
			} else if (list.size() > 1) {
				bln = dao.updateResource(list);
			}
			if(bln){
				List<ResourceBean> _list;
				int active, start, end;
				active = resourceListboxPaging.getActivePage();
				if (active == 0) {
					start = 1;
					end = resourceListboxPaging.getPageSize();
				} else {
					start = active * resourceListboxPaging.getPageSize();
					end = start + resourceListboxPaging.getPageSize();
				}
				_list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), start, end);
				resourceGrid.setModel(new ListModelList(_list, live));
				alert("审核成功");
			}else{
				alert("审核失败");
			}
			
		}
	}

	public void onClick$addAll() {
		int size = resourceListboxPaging.getPageSize();
		List<Long> list = new ArrayList<Long>();
		for (int row = 0; row < size; row++) {
			Component comp = resourceGrid.getCell(row, 1);
			if (comp instanceof Checkbox) {
				Checkbox ckb = (Checkbox) comp;
				list.add(Long.parseLong(ckb.getId()));
			}
		}
		if (list != null && !list.isEmpty()) {
			boolean bln = false;
			if (list.size() == 1) {
				bln = dao.updateResource(list.get(0));
			} else if (list.size() > 1) {
				bln = dao.updateResource(list);
			}
			if(bln){
				List<ResourceBean> _list;
				int active, start, end;
				active = resourceListboxPaging.getActivePage();
				if (active == 0) {
					start = 1;
					end = resourceListboxPaging.getPageSize();
				} else {
					start = active * resourceListboxPaging.getPageSize();
					end = start + resourceListboxPaging.getPageSize();
				}
				_list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), start, end);
				resourceGrid.setModel(new ListModelList(_list, live));
				alert("审核成功");
			}else{
				alert("审核失败");
			}
			
		}
	}
	
	//发布
	public void onClick$mod() {
		//TODO 多选
		try{
			UserBean user = (UserBean)session.getAttribute("user");
			if(user == null){
				alert("长时间未操作，请重新登录");
				Executions.sendRedirect("/pages/index.zul");
			}
			int size = resourceListboxPaging.getPageSize();
			List<String> list = new ArrayList<String>();
			for (int row = 0; row < size; row++) {
				Component comp = resourceGrid.getCell(row, 1);
				if (comp instanceof Checkbox) {
					Checkbox ckb = (Checkbox) comp;
					if (ckb.isChecked())
						list.add(ckb.getId());
				}
			}
			if (list != null && !list.isEmpty()) {
				boolean bln = false;
				String[] resourceIds = new String[list.size()];
				for(int index=0;index<list.size();index++){
					resourceIds[index] = list.get(index);
				}
				bln = service.sendResource(user.getUserId(), bean.getChannelId().toString(), resourceIds);
				if(bln){
					alert("发布成功");
				}else{
					alert("发布失败");
				}
		}
		}catch(Exception e){
			e.printStackTrace();
			alert("发布报错");
		}
	}

	public void onClick$modAll() {
		//TODO 多选
		try{
			UserBean user = (UserBean)session.getAttribute("user");
			if(user == null){
				alert("长时间未操作，请重新登录");
				Executions.sendRedirect("/pages/index.zul");
			}
			int size = resourceListboxPaging.getPageSize();
			List<String> list = new ArrayList<String>();
			for (int row = 0; row < size; row++) {
				Component comp = resourceGrid.getCell(row, 1);
				if (comp instanceof Checkbox) {
					Checkbox ckb = (Checkbox) comp;
					list.add(ckb.getId());
				}
			}
			if (list != null && !list.isEmpty()) {
				boolean bln = false;
				String[] resourceIds = new String[list.size()];
				for(int index=0;index<list.size();index++){
					resourceIds[index] = list.get(index);
				}
				bln = service.sendResource(user.getUserId(), bean.getChannelId().toString(), resourceIds);
				if(bln){
					alert("发布成功");
				}else{
					alert("发布失败");
				}
		}
		}catch(Exception e){
			e.printStackTrace();
			alert("发布报错");
		}
	}
	
	//查询
	public void onClick$search(){
		List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()));
		if(list != null && !list.isEmpty()){
			
			resourceListboxPaging.setTotalSize(list.size());
			resourceListboxPaging.addEventListener(ZulEvents.ON_PAGING, new ResourceListboxPagingListener());
			System.out.println("search condition : "+title.getValue()+"|"+sdf.format(currentDate.getValue()));
			list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), 1, resourceListboxPaging.getPageSize());
			ListModel listModel = new ListModelList(list, live);
			
			resourceGrid.setModel(listModel);
			resourceGrid.setRowRenderer(new ResourceGridRowRenderer());
		}
	}
	
	class ResourceListitemRenderer implements ListitemRenderer {

		Checkbox checkbox;

		@Override
		public void render(Listitem item, Object data) throws Exception {
			if (data instanceof ResourceBean) {
				ResourceBean bean = (ResourceBean) data;

				checkbox = new Checkbox();
				checkbox.setId(bean.getResId().toString().trim());

				item.setValue(bean);
				Listcell cell = new Listcell();
				cell.appendChild(checkbox);

				item.appendChild(cell);
				item.appendChild(new Listcell(bean.getChannelName()));
				item.appendChild(new Listcell(bean.getTitle()));
				item.appendChild(new Listcell(bean.getStatus()));
				item.appendChild(new Listcell(bean.getCreateTime()));

				item.addEventListener(Events.ON_DOUBLE_CLICK, new ResourceListitemListener());
			}
		}

	}

	class ResourceGridRowRenderer implements RowRenderer {

		Detail detail;
		Checkbox checkbox;

		@Override
		public void render(Row row, Object data) throws Exception {
			if (data instanceof ResourceBean) {
				ResourceBean bean = (ResourceBean) data;

				detail = new Detail();
				detail.addEventListener(Events.ON_OPEN, new DetailEventListener(detail,bean.getContent()));
				//detail.appendChild(new Html(bean.getContent()));

				checkbox = new Checkbox();
				checkbox.setId(bean.getResId().toString());

				row.appendChild(detail);
				row.appendChild(checkbox);
				row.appendChild(new Label(bean.getChannelName()));
				row.appendChild(new Html("<a href='"+bean.getLink()+"' target='_blank'>"+bean.getTitle()+"</a>"));
				
				Button btn = new Button("编辑");
				btn.addEventListener(Events.ON_CLICK, new EditButtonEventListener(bean));
				row.appendChild(btn);
				
				row.appendChild(new Label(bean.getStatus()));
				row.appendChild(new Label(bean.getPics()));
				row.appendChild(new Label(String.valueOf(bean.getContent().length())));
				row.appendChild(new Label(bean.getCreateTime()));

			}
		}

	}

	class DetailEventListener implements EventListener {
		Detail detail;
		String content;
		public DetailEventListener(Detail detail, String content) {
			this.detail = detail;
			this.content = content;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			if(detail != null && content != null){
				Component comp = detail.getFirstChild();
				if(comp == null){
					detail.appendChild(new Html(content));
				}
			}
		}
	}
	
	class EditButtonEventListener implements EventListener {
		
		ResourceBean bean;
		
		public EditButtonEventListener(ResourceBean bean) {
			this.bean = bean;
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			if(bean != null){
				resourceDiv.getChildren().clear();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bean", bean);
				Window win = (Window)Executions.createComponents("/pages/resource/resource_form.zul", resourceDiv, map);
				if(win != null){
					Button saveBtn = (Button)win.getFellow("save");
					saveBtn.addEventListener(Events.ON_CLICK, new SaveButtonEventListener(win));
					win.doModal(); 
				}
			}
		}
	}
	
	class ResourceListboxPagingListener implements EventListener {

		List<ResourceBean> list;
		int active, start, end;

		@Override
		public void onEvent(Event event) throws Exception {
			System.out.println(event.getName());
			active = resourceListboxPaging.getActivePage();
			if (active == 0) {
				start = 1;
				end = resourceListboxPaging.getPageSize();
			} else {
				start = active * resourceListboxPaging.getPageSize();
				end = start + resourceListboxPaging.getPageSize();
			}
			list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), start, end);
			resourceGrid.setModel(new ListModelList(list, live));
			System.out.println(event.getName() + "[" + active + "][" + start
					+ "-" + end + "][" + resourceListboxPaging.getPageCount()
					+ "]");
		}

	}

	class SaveButtonEventListener implements EventListener {
		
		Window win;
		
		Longbox resId;
		Textbox title;
		FCKeditor content;
		
		int active, start, end;
		
		public SaveButtonEventListener(Window win) {
			this.win = win;
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			if(win != null){
				resId = (Longbox)win.getFellow("resId");
				title = (Textbox)win.getFellow("title");
				content = (FCKeditor)win.getFellow("content");
				String cnt = content.getValue();//(String)content.getAttribute("value");
				cnt = cnt.replaceAll("<p>|</p>", "");
				boolean bln = dao.modifyResourceContentOrTitle(resId.getValue(), title.getValue(), cnt);
				if(bln){
					
					active = resourceListboxPaging.getActivePage();
					if (active == 0) {
						start = 1;
						end = resourceListboxPaging.getPageSize();
					} else {
						start = active * resourceListboxPaging.getPageSize();
						end = start + resourceListboxPaging.getPageSize();
					}
					
					List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), start, end);
					resourceGrid.setModel(new ListModelList(list, live));
					System.out.println(resId.getValue()+"|"+title.getValue()+"|"+cnt);
					alert("保存成功");
				}else{
					alert("保存失败");
				}
				win.onClose();
			}
		}
	}
	
	class ResourceListitemListener implements EventListener {

		@Override
		public void onEvent(Event event) throws Exception {
			Listitem selected = resourceListbox.getSelectedItem();
			if (selected != null) {
				Object obj = selected.getValue();
				ResourceBean bean = (ResourceBean) obj;
				alert(bean.getContent());
			}
		}

	}
}
