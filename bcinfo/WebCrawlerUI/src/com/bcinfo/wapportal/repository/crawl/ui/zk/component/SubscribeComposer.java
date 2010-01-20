/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

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
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.bcinfo.wapportal.repository.crawl.service.impl.CrawlResourceServiceDefaultImpl;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.SubscribeDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.SubscribeBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-12 下午04:53:05
 */
public class SubscribeComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Component parentComp;
	private Listbox listbox;
	private Textbox folderId;
	private Toolbarbutton add, del;
	private ListModelList listModel;
	private ListitemRenderer renderer;
	private Listitem selected;

	private UserBean user;
	private ChannelBean channelBean;
	private SubscribeBean subscribeBean;
	private SubscribeDao dao;
	private List<SubscribeBean> list;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		this.parentComp = parent;
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		dao = new SubscribeDao();
		renderer = new SubscribeListitemRenderer();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (arg != null) {
			channelBean = (ChannelBean) arg.get("channelBean");
		}
		initCommonsComponent();
		check();
		if (channelBean != null) {
			initListbox();
		}
	}

	void check(){
		Map<String, Object> map = new HashMap<String, Object>();
		if(session.getAttribute("user")==null){
			map.put("location", "local");
			Window login = (Window)Executions.createComponents("pages/login/login.zul", parentComp, map);
			login.doPopup();
		}
	}
	
	void initCommonsComponent() {
		folderId.setId("tb_sub_folder_" + channelBean.getChannelId());
		add.setId("btn_sub_add_" + channelBean.getChannelId());
		del.setId("btn_sub_del_" + channelBean.getChannelId());
	}

	void initListbox() {
		listbox.setId("listbox_sub_" + channelBean.getChannelId());
		user = (UserBean)session.getAttribute("user");
		channelBean = (ChannelBean) arg.get("channelBean");
		System.out.println(user);
		if(user!=null){
			System.out.println(channelBean);
			list = dao.getSubscribeList(user.getUserId(), channelBean.getChannelId());
			listModel = new ListModelList(list, true);
			listbox.setModel(listModel);
		}
		listbox.setItemRenderer(renderer);
	}

	public void onSelect$listbox() {
		selected = listbox.getSelectedItem();
		subscribeBean = (SubscribeBean)selected.getValue();
		folderId.setValue(subscribeBean.getLocalFolderId());
	}
	
	public void onClick$add() {
		//alert("folder:"+folderId.getValue()+"|channel:"+channelBean.getChannelId()+"_"+channelBean.getChannelName());
		if(folderId.getValue()!=null&&!"".equals(folderId.getValue())){
			if(folderId.getValue().length()!=6){
				alert("请尽量填写一个六位的栏目号");
			}
			subscribeBean = new SubscribeBean();
			subscribeBean.setChannelName(channelBean.getChannelName());
			subscribeBean.setChannelId(channelBean.getChannelId());
			subscribeBean.setLocalFolderId(folderId.getValue());
			subscribeBean.setLocalCode(user.getLocalCode());
			subscribeBean.setOperation(String.valueOf(CrawlResourceServiceDefaultImpl.INSERT));
			subscribeBean.setUserId(user.getUserId());
			subscribeBean.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
			boolean bln = dao.save(subscribeBean);
			if(bln){
				listModel.add(subscribeBean);
			}else{
				alert("操作失败");
			}
		}else{
			alert("栏目号不能为空(请尽量填写一个六位的栏目号)");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$del() {
		if(listbox.getSelectedCount()>0){
			Set<Listitem> set = listbox.getSelectedItems();
			List<SubscribeBean> subscribeBeans = new ArrayList<SubscribeBean>();
			for(Iterator<Listitem> iter = set.iterator();iter.hasNext();){
				subscribeBeans.add((SubscribeBean)iter.next().getValue());
			}
			boolean bln = dao.deleteSubscribeBeans(subscribeBeans);
			if(bln){
				listModel.removeAll(subscribeBeans);
				folderId.setValue("");
			}else{
				alert("操作失败");
			}
		}else{
			alert("请选择要退订的栏目");
		}
	}

	class SubscribeListitemRenderer implements ListitemRenderer {
		Integer rownum;

		public SubscribeListitemRenderer() {
			this.rownum = 1;
		}

		@Override
		public void render(Listitem item, Object data) throws Exception {
			SubscribeBean bean = (SubscribeBean) data;
			item.appendChild(new Listcell());
			item.appendChild(new Listcell(rownum.toString()));
			item.appendChild(new Listcell(bean.getLocalCode()));
			item.appendChild(new Listcell(bean.getChannelName()));
			item.appendChild(new Listcell(bean.getLocalFolderId()));

			item.setValue(data);
			rownum++;
		}
	}
}
