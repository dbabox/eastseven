/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ZulEvents;

import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;
import com.bcinfo.wapportal.repository.crawl.service.impl.CrawlResourceServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ResourceDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ResourceBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.util.StaticVariable;

/**
 * @author dongq
 * 
 *         create time : 2010-1-8 上午09:55:26
 */
public class ResourceComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Component parentComp;
	//页面组件
	private Paging paging;//分页组件
	private Grid grid;//表格组件
	private Listbox listbox, listboxSelect, listboxSelectPass;//列表组件
	private Textbox title, tb, fck;
	private Label fileLocation;
	private Button search, save, pass, passAll, send, sendAll, preview, fileCheck;
	private Datebox currentDate, currentDateEnd;
	private Splitter splitter;
	//private FCKeditor fck;
	
	private EventListener onPagingEventListener;
	private RowRenderer rowRenderer;
	private ListitemRenderer listitemRenderer;
	private ListModelList listModel;
	private Listitem selected;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final Date _currentDate = new Date(System.currentTimeMillis());
	private ChannelBean channelBean;
	private ResourceBean resourceBean;
	private ResourceDao dao;
	private List<ResourceBean> list;
	private String _title, _status;
	private CrawlResourceService service;
	private UserBean user;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		this.parentComp = parent;
		System.out.println("parent:"+parentComp);
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		dao = new ResourceDao();
		service = new CrawlResourceServiceDefaultImpl();
		onPagingEventListener = new PagingEventListener();
		rowRenderer = new ResourceGridRowRenderer();
		listitemRenderer = new ResourceListitemRenderer();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Object object = arg.get("channelBean");
		if (object != null) {
			channelBean = (ChannelBean) object;
		}

		if (channelBean != null) {
			initCommonComponents();
			initPaging();
			initListbox();
		}
		
	}

	void initCommonComponents(){
		fileCheck.setId("label_file_check_"+channelBean.getChannelId());
		if(fileLocation.getValue().contains("*****")) fileCheck.setVisible(false);
		fileLocation.setId("label_file_local_"+channelBean.getChannelId());
		listboxSelect.setId("listbox_select_"+channelBean.getChannelId());
		listboxSelectPass.setId("listbox_select_pass_"+channelBean.getChannelId());
		save.setId("btn_save_"+channelBean.getChannelId());
		pass.setId("btn_pass_"+channelBean.getChannelId());
		preview.setId("btn_preview_"+channelBean.getChannelId());
		passAll.setId("btn_pass_all_"+channelBean.getChannelId());
		send.setId("btn_send_"+channelBean.getChannelId());
		sendAll.setId("btn_send_all_"+channelBean.getChannelId());
		tb.setId("tb_"+channelBean.getChannelId());
		fck.setId("fck_"+channelBean.getChannelId());
		//fck.smartUpdate("value", true);
		//fck.disableClientUpdate(false);
		splitter.setId("spl_"+channelBean.getChannelId());
		title.setId("title_"+channelBean.getChannelId());
		currentDate.setId("datebox_start_"+channelBean.getChannelId());
		currentDateEnd.setId("datebox_end_"+channelBean.getChannelId());
		search.setId("btn_search_"+channelBean.getChannelId());
		currentDate.setValue(_currentDate);
		currentDateEnd.setValue(_currentDate);
	}
	
	void initPaging() {
		String id = "paging_chl_" + channelBean.getChannelId();
		paging.setId(id);
		list = dao.getResourceList(channelBean.getChannelId(), null, _title, sdf.format(_currentDate), sdf.format(_currentDate));
		if(list!=null && !list.isEmpty()){
			paging.setTotalSize(list.size());
			paging.addEventListener(ZulEvents.ON_PAGING, onPagingEventListener);
		}
	}

	void initListbox() {
		listbox.setId("listbox_chl_"+channelBean.getChannelId());
		_status = (String)listboxSelectPass.getSelectedItem().getValue();
		list = dao.getResourceList(channelBean.getChannelId(), _status, _title, sdf.format(_currentDate), sdf.format(_currentDate), 1, paging.getPageSize());
		listModel = new ListModelList(list, true);
		listbox.setModel(listModel);
		listbox.smartUpdate("model", true);
		listbox.setItemRenderer(listitemRenderer);
	}
	
	void initGrid() {
		/**/
		String id = "grid_chl_" + channelBean.getChannelId();
		grid.setId(id);
		list = dao.getResourceList(channelBean.getChannelId(), null, _title, "2009-01-01", sdf.format(_currentDate), 1, paging.getPageSize());
		listModel = new ListModelList(list, true);
		grid.setModel(listModel);
		//grid.smartUpdate("model", true);
		grid.setRowRenderer(rowRenderer);
		
	}
	
	/*--------- 按钮事件 -------------------------------------------------------------------------------------*/
	
	public void onSelect$listbox() {
		this.fileLocation.setValue(" ");
		if(listbox.getSelectedCount()>0){
			selected = (Listitem)listbox.getSelectedItems().iterator().next();
			if(selected!=null){
				Object obj = selected.getValue();
				ResourceBean bean = (ResourceBean)obj;
				this.tb.setValue(bean.getTitle());
				this.fck.setValue(bean.getContent().replaceAll("<p>|</p>", ""));
				if(bean.getFilePathSet()!=null&&!"".equals(bean.getFilePathSet())){
					this.fileLocation.setValue(bean.getFilePathSet());
					//this.fileCheck.setHref(bean.getFilePathSet());
					this.fileCheck.setVisible(true);
				}
			}
		}
	}
	
	public void onClick$search() {
		Long channelId = channelBean.getChannelId();
		_status = (String)listboxSelectPass.getSelectedItem().getValue();
		int start = 1;
		int end = paging.getPageSize();
		System.out.println("审核状态："+_status);
		int size = dao.getResourceListSize(channelId, _status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()));
		list = dao.getResourceList(channelId, _status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
		paging.setTotalSize(size);
		paging.addEventListener(ZulEvents.ON_PAGING, onPagingEventListener);
		listModel.clear();
		listModel.addAll(list);
	}
	
	//保存、审核时需判断内容是否存在关键字
	//保存
	public void onClick$save() {
		this.resourceBean = (ResourceBean)selected.getValue();
		boolean bln = checkKeyWords(resourceBean);
		if(bln){
			bln = saveResourceBean(this.resourceBean);
			if(!bln){
				try {
					Messagebox.show("保存失败");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			alert("标题或内容存在敏感关键字，请删除后再提交");
		}
	}
	
	//审核通过
	public void onClick$pass(){
		this.resourceBean = (ResourceBean)selected.getValue();
		boolean bln = checkKeyWords(resourceBean);
		if(bln){
			this.resourceBean.setStatus("1");
			bln = saveResourceBean(this.resourceBean);
			if(!bln){
				try {
					Messagebox.show("审核失败");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			alert("标题或内容存在敏感关键字，请删除后再提交");
		}
	}
	
	//发送
	public void onClick$send() {
		
	}
	
	//全部审核
	@SuppressWarnings("unchecked")
	public void onClick$passAll() {
		Set<Listitem> set = this.listbox.getSelectedItems();
		if(set!=null&&!set.isEmpty()){
			Iterator<Listitem> iter = set.iterator();
			List<Long> resIds = new ArrayList<Long>();
			boolean bln = false;
			while(iter.hasNext()){
				ResourceBean bean = (ResourceBean)iter.next().getValue();
				if(!checkKeyWords(bean)){
					break;
				}else{
					bln = true;
				}
				resIds.add(bean.getResId());
			}
			if(bln){
				System.out.println("resIds:"+resIds);
				bln = dao.updateResource(resIds);
				if(bln){
					alert("成功");
				}else{
					alert("失败");
				}
				//this.listbox.renderAll();
			}else{
				alert("标题或内容存在敏感关键字，请删除后再提交");
			}
		}else{
			alert("请选择要操作的记录");
		}
	}
	
	//全部发送
	@SuppressWarnings("unchecked")
	public void onClick$sendAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(session.getAttribute("user")==null){
			map.put("location", "local");
			Window login = (Window)Executions.createComponents("pages/login/login.zul", parentComp, map);
			login.doPopup();
		}else{
			if(this.listbox.getSelectedCount()>0){
				Set<Listitem> set = this.listbox.getSelectedItems();
				Iterator<Listitem> iter = set.iterator();
				List<Long> resId = new ArrayList<Long>();
				while(iter.hasNext()){
					ResourceBean bean = (ResourceBean)iter.next().getValue();
					if("1".equals(bean.getStatus())) resId.add(bean.getResId());
				}
				String sendType = this.listboxSelect.getSelectedItem().getValue().toString();
				user = (UserBean)session.getAttribute("user");
				map.put("sendType", sendType);
				boolean bln = service.sendResource(user.getUserId(), channelBean.getChannelId().toString(), resId, map);
				if(bln){
					alert("发送成功");
				}else{
					alert("发送失败");
				}
			}else{
				alert("请选择要操作的记录");
			}
		}
	}
	
	//下载验证
	public void onClick$fileCheck() {
		String link = this.fileLocation.getValue();
		boolean bln = checkDownload(link);
		if(bln){
			alert("可以下载");
		}else{
			alert("不能下载");
		}
	}
	
	private boolean checkDownload(String link) {
		boolean bln = false;
		InputStream is = null;
		try {
			URL url = new URL(link);
			URLConnection http = url.openConnection();
			http.addRequestProperty("Referer", link);
			is = http.getInputStream();
			if(is!=null){
				bln = true;
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bln;
	}
	
	/**
	 * 关键字检查
	 * @param resourceBean
	 * @return false:未通过,true:通过
	 */
	private boolean checkKeyWords(ResourceBean resourceBean){
		return checkKeyWords(resourceBean.getTitle(), resourceBean.getContent());
	}
	
	//关键字检查
	private boolean checkKeyWords(String title, String content){
		boolean bln = false;

		try {
			if(title!=null&&!"".equals(title)){
				if(!title.contains(StaticVariable.KEY_REFERENCE_SUBSTANCE)) bln = true;
			}
			if(content!=null&&!"".equals(content)){
				if(!content.contains(StaticVariable.KEY_REFERENCE_SUBSTANCE)) bln = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bln;
	}
	
	private boolean saveResourceBean(ResourceBean resourceBean) {
		boolean bln = false;
		resourceBean = (ResourceBean)selected.getValue();
		int index = listModel.indexOf(resourceBean);
		resourceBean.setTitle(tb.getValue());
		resourceBean.setContent(fck.getValue().replaceAll("<p>|</p>", ""));
		bln = dao.modifyResourceContentOrTitle(resourceBean);
		if(bln){
			listModel.remove(index);
			listModel.add(index, resourceBean);
		}
		return bln;
	}
	
	/*--------- 内部类 -------------------------------------------------------------------------------------*/
	
	//翻页事件监听
	class PagingEventListener implements EventListener {
		int active, start, end;
		@Override
		public void onEvent(Event event) throws Exception {
			active = paging.getActivePage();
			if (active == 0) {
				start = 1;
				end = paging.getPageSize();
			} else {
				start = active * paging.getPageSize();
				end = start + paging.getPageSize();
			}
			list = dao.getResourceList(channelBean.getChannelId(), _status, title.getValue(), sdf.format(currentDate.getValue()), sdf.format(currentDateEnd.getValue()), start, end);
			System.out.println("翻页结果:"+list.size());
			listModel.clear();
			listModel.addAll(list);
		}
	}
	
}
