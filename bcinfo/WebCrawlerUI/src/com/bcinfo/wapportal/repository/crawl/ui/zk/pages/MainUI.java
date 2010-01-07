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
	private Tabs tabs;
	private Tabpanel tabpanel;
	private Tabpanels tabpanels;

	private Map<String, String> map;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		System.out.println(comp.getId() + "|" + comp);
		map = new HashMap<String, String>();
	}

	public void onClose() {
		System.out.println("关闭");
	}

	@SuppressWarnings("unchecked")
	public void onClick$resMenuitem() {
		final String id = "resTab";
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
			map.put("cmd", "res");
			createPanel(id, "资源管理", "pages/component/tree.zul", map);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$chlMenuitem() {
		final String id = "chlTab";
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
			map.put("cmd", "chl");
			createPanel(id, "频道管理", "pages/component/tree.zul", map);
		}
	}
	
	void createPanel(String id, String name, String uri, Map<String, String> map){
		try {
			tab = new Tab(name);
			tab.setId(id);
			tab.setClosable(true);
			tab.setSelected(true);
			tabpanel = new Tabpanel();
			tabpanel.setHeight("100%");
			Component comp = Executions.createComponents(uri, this.self, map);
			tabpanel.appendChild(comp);
			
			tabs.appendChild(tab);
			tabpanels.appendChild(tabpanel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
