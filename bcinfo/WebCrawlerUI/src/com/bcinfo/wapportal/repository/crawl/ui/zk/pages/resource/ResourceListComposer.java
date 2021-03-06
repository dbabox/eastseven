package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.bcinfo.wapportal.repository.crawl.dao.WapDao;
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
	private WapDao filterDao;
	private CrawlResourceService service;

	private Paging resourceListboxPaging;
	private Listbox resourceListbox;
	private Grid resourceGrid;
	private Div resourceDiv;
	
	private Textbox title;
	private Datebox currentDate;
	private Datebox currentDateEnd;
	
	public ResourceListComposer() {
		dao = new ResourceDao();
		filterDao = new WapDao();
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
		Date date = new Date();
		List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, null, sdf.format(date),sdf.format(date));
		if(list != null && !list.isEmpty()){
			
			resourceListboxPaging.setTotalSize(list.size());
			resourceListboxPaging.addEventListener(ZulEvents.ON_PAGING, new ResourceListboxPagingListener());
			
			list = dao.getResourceList(bean.getChannelId(), status, null, sdf.format(date), sdf.format(date), 1, resourceListboxPaging.getPageSize());
			ListModel listModel = new ListModelList(list, live);
			
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
			for(Long id : list){
				ResourceBean bean = dao.getResource(id);
				String cnt = bean.getContent();//filterHandle(bean.getContent());
				String tle = bean.getTitle();//filterHandle(bean.getTitle());
				if(cnt.contains("<font style=\"color: #ff0000\">")||tle.contains("<font style=\"color: #ff0000\">")){
					alert("当前内容或标题有需屏蔽的敏感关键字,请重新审核!");
					break;
				}else{
					bln = true;
				}
			}
			if(bln){
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
					_list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
					resourceGrid.setModel(new ListModelList(_list, live));
					alert("审核成功");
				}else{
					alert("审核失败");
				}
			}
			
		}
	}

	//全部审核
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
			for(Long id : list){
				ResourceBean bean = dao.getResource(id);
				String cnt = bean.getContent();//filterHandle(bean.getContent());
				String tle = bean.getTitle();//filterHandle(bean.getTitle());
				if(cnt.contains("<font style=\"color: #ff0000\">")||tle.contains("<font style=\"color: #ff0000\">")){
					alert("当前内容或标题有需屏蔽的敏感关键字,请重新审核!");
					break;
				}else{
					bln = true;
				}
			}
			if(bln){
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
					_list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
					resourceGrid.setModel(new ListModelList(_list, live));
					alert("审核成功");
				}else{
					alert("审核失败");
				}
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
			System.out.println(user + "在发布资源");
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
				Map<String, Object> map = new HashMap<String, Object>();
				bln = service.sendResource(user.getUserId(), bean.getChannelId().toString(), resourceIds, map);
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

	//全部发布
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
				Map<String, Object> map = new HashMap<String, Object>();
				bln = service.sendResource(user.getUserId(), bean.getChannelId().toString(), resourceIds, map);
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
	
	//删除
	public void onClick$del() {
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
				bln = dao.delete(list);
				if(bln){
					alert("删除成功");
				}else{
					alert("删除失败");
				}
		}
		}catch(Exception e){
			e.printStackTrace();
			alert("删除报错");
		}
	}
	
	//全部删除
	public void onClick$delAll() {
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
				bln = dao.delete(list);
				if(bln){
					alert("删除成功");
				}else{
					alert("删除失败");
				}
		}
		}catch(Exception e){
			e.printStackTrace();
			alert("删除报错");
		}
	}
	
	//查询
	public void onClick$search(){
		List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()));
		if(list != null && !list.isEmpty()){
			
			resourceListboxPaging.setTotalSize(list.size());
			resourceListboxPaging.addEventListener(ZulEvents.ON_PAGING, new ResourceListboxPagingListener());
			list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), 1, resourceListboxPaging.getPageSize());
			ListModel listModel = new ListModelList(list, live);
			
			resourceGrid.setModel(listModel);
			resourceGrid.setRowRenderer(new ResourceGridRowRenderer());
		}
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private String filterHandle(String content) {
		try {
			String cnt = content;
			List<String> list = filterDao.getAll();
			if(list!=null&&!list.isEmpty()){
				for(String key : list){
					cnt = cnt.replace(key, "<font style=\"color: #ff0000\">"+key+"</font>");
				}
			}
			return cnt;
		} catch (Exception e) {
			e.printStackTrace();
			return content;
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
				String title = bean.getTitle();
				//String content = bean.getContent();
				//TODO <font style="color: #ff0000"></font>
				//content = filterHandle(content);
				//bean.setContent(content);
				//title = filterHandle(title);
				//bean.setTitle(title);
				
				detail = new Detail();
				detail.addEventListener(Events.ON_OPEN, new DetailEventListener(detail,bean.getResId()));

				checkbox = new Checkbox();
				checkbox.setId(bean.getResId().toString());

				row.appendChild(detail);
				row.appendChild(checkbox);
				row.appendChild(new Label(bean.getChannelName()));
				row.appendChild(new Html("<a href='"+bean.getLink()+"' target='_blank'>"+title+"</a>"));
				
				Button btn = new Button("编辑");
				btn.addEventListener(Events.ON_CLICK, new EditButtonEventListener(bean));
				row.appendChild(btn);
				
				row.appendChild(new Label(bean.getStatus()));
				row.appendChild(new Label(bean.getPics()));
				row.appendChild(new Label(bean.getWords()));
				row.appendChild(new Label(bean.getCreateTime()));

			}
		}

	}

	class DetailEventListener implements EventListener {
		Detail detail;
		Long resId;
		public DetailEventListener(Detail detail, Long resId) {
			this.detail = detail;
			this.resId = resId;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			if(detail != null && resId != null){
				Component comp = detail.getFirstChild();
				if(comp == null){
					ResourceBean bean = dao.getResource(resId);
					String content = bean.getContent();
					if(!"".equals(bean.getFilePathSet())){
						content += "<br/>"+bean.getFilePathSet();
					}
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
			list = dao.getResourceList(bean.getChannelId(), status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
			resourceGrid.setModel(new ListModelList(list, live));
			System.out.println(event.getName() + "[" + active + "][" + start
					+ "-" + end + "][" + resourceListboxPaging.getPageCount()
					+ "]");
		}

	}

	//修改保存
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
				boolean bln = false;
				resId = (Longbox)win.getFellow("resId");
				title = (Textbox)win.getFellow("title");
				content = (FCKeditor)win.getFellow("content");
				String cnt = content.getValue();
				String tle = title.getValue();
				cnt = cnt.replaceAll("<p>|</p>", "");
				if(cnt.contains("<font style=\"color: #ff0000\">")||tle.contains("<font style=\"color: #ff0000\">")){
					alert("当前内容或标题有需屏蔽的敏感关键字,请重新审核!");
				}else{
					bln = dao.modifyResourceContentOrTitle(resId.getValue(), tle, cnt, status);
					if(bln){
						
						active = resourceListboxPaging.getActivePage();
						if (active == 0) {
							start = 1;
							end = resourceListboxPaging.getPageSize();
						} else {
							start = active * resourceListboxPaging.getPageSize();
							end = start + resourceListboxPaging.getPageSize();
						}
						
						List<ResourceBean> list = dao.getResourceList(bean.getChannelId(), status, "", sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
						resourceGrid.setModel(new ListModelList(list, live));
						System.out.println(resId.getValue()+"|"+"["+start+"-"+end+"]"+"|"+cnt);
						//alert("保存成功");
					}else{
						alert("保存失败");
					}
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
