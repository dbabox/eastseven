/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 * @author dongq
 * 
 *         create time : 2010-1-7 下午07:19:45
 */
public class MainUI extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tab tab;
	private Tabs tabs,contentTabs;
	private Tabpanel tabpanel;
	private Tabpanels tabpanels,contentTabpanels;
	
	private Map<String, String> map;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//user = (UserBean)session.getAttribute("user");
		/*
		if(user==null){
			borderlayout.setVisible(false);
			Window login = (Window)Executions.createComponents("pages/login/login.zul", comp, null);
			login.doPopup();
		}
		*/
		System.out.println(comp.getId() + "|" + comp);
		map = new HashMap<String, String>();
	}

	public void onClose() {
		System.out.println("关闭");
	}

	public void onClick$sc_1(){
		createPanel("tab_sc_1", "四川风采1.0后台", "http://218.205.231.65");
	}
	
	public void onClick$sc_2(){
		createPanel("tab_sc_2", "四川风采2.0后台", "http://218.205.231.65/wapsyscore/");
	}
	
	public void onClick$jx_1(){
		createPanel("tab_jx_1", "江西风采1.0后台", "http://211.141.87.33");
	}
	
	public void onClick$jx_2(){
		createPanel("tab_jx_2", "江西风采2.0后台", "http://211.141.87.33/wapsyscore/");
	}
	
	//资源管理
	public void onClick$resMenuitem() {
		final String id = "resTab";
		map.put("cmd", "res");
		onClickMenuitem(id, "资源管理", "pages/component/tree.zul", map);
	}
	
	//频道管理
	@SuppressWarnings("unchecked")
	public void onClick$chlMenuitem() {
		final String id = "chlTab";
		map.put("cmd", "chl");
		
		boolean bln = false;
		//点击按钮时，先判断组件是否已创建，若已创建，则选中
		List<Tab> list = contentTabs.getChildren();
		if(list!=null&&!list.isEmpty()){
			for(Tab _tab : list){
				if(id.equals(_tab.getId())){
					_tab.setSelected(true);
					bln = true;
					break;
				}
			}
		}
		if(!bln){
			createTabpanel(id, "频道管理", "pages/component/channel.zul", map);
		}
	}
	
	//抓取配置
	public void onClick$catMenuitem() {
		final String id = "catTab";
		map.put("cmd", "cat");
		onClickMenuitem(id, "抓取配置", "pages/component/tree.zul", map);
	}
	
	//订购管理
	public void onClick$subMenuitem() {
		final String id = "subTab";
		map.put("cmd", "sub");
		onClickMenuitem(id, "订购管理", "pages/component/tree.zul", map);
		//createTabpanel(id, "订购管理", "pages/component/subscribe.zul", map);
	}

	//SPCP:实现本地1.0后台查询
	public void onClick$spcpMenuitem() {
		alert("开发中...");
	}
	
	public void onClick$aboutMenuitem() {
		alert("当前版本："+application.getVersion());
	}
	
	//JobDetail
	public void onClick$quartzMenuitem() {
		final String id = "quartzTab";
		onClickMenuitem(id, "QuartzManager", "pages/quartz/group_tree.zul", null);
	}
	
	@SuppressWarnings("unchecked")
	void onClickMenuitem(String id, String name, String uri, Map<String, String> map){
		boolean bln = false;
		//点击按钮时，先判断组件是否已创建，若已创建，则选中
		List<Tab> list = tabs.getChildren();
		if(list!=null&&!list.isEmpty()){
			for(Tab _tab : list){
				if(id.equals(_tab.getId())){
					_tab.setSelected(true);
					bln = true;
					break;
				}
			}
		}
		if(!bln){
			createPanel(id, name, uri, map);
		}
	}
	
	void createPanel(String id, String name, String src){
		try {
			tab = createTab(id, name);
			tabpanel = createTabpanel();
			Iframe iframe = new Iframe(src);
			iframe.setHeight("100%");
			iframe.setWidth("100%");
			tabpanel.appendChild(iframe);
			contentTabs.appendChild(tab);
			contentTabpanels.appendChild(tabpanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void createPanel(String id, String name, String uri, Map<String, String> map){
		try {
			tab = createTab(id, name);
			tabpanel = createTabpanel();
			Component comp = Executions.createComponents(uri, this.self, map);
			tabpanel.appendChild(comp);
			
			tabs.appendChild(tab);
			tabpanels.appendChild(tabpanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void createTabpanel(String id, String name, String uri, Map<String, String> map){
		try {
			tab = createTab(id, name);
			tabpanel = createTabpanel();
			Component comp = Executions.createComponents(uri, this.self, map);
			tabpanel.appendChild(comp);
			
			this.contentTabs.appendChild(tab);
			this.contentTabpanels.appendChild(tabpanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Tab createTab(String id, String name){
		tab = new Tab(name);
		tab.setId(id);
		tab.setClosable(true);
		tab.setSelected(true);
		return tab;
	}
	
	Tabpanel createTabpanel(){
		tabpanel = new Tabpanel();
		tabpanel.setHeight("100%");
		return tabpanel;
	}
}
