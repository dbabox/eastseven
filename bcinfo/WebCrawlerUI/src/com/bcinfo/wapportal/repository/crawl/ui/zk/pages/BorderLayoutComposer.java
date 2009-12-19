package com.bcinfo.wapportal.repository.crawl.ui.zk.pages;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Menubar;

import com.bcinfo.wapportal.repository.crawl.dao.AppLogDao;

/**
 * @author dongq
 * 
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Grid grid;
	private Div contentDiv;
	private Menubar menubar;
	private Map<String, Object> map;

	public BorderLayoutComposer() {
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//login
		Object obj = session.getAttribute("user");
		if(obj == null){
			menubar.setVisible(false);
			contentDiv.getChildren().clear();
			contentDiv.setAlign("center");
			createContent("/pages/login/login.zul", contentDiv, null);
		}else{
			menubar.setVisible(true);
			initGrid();
		}
	}

	public void onClick$channelMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		createContent("/pages/channel/channel_layout.zul", contentDiv, map);
	}

	public void onClick$catchMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		createContent("/pages/catch/catch_layout.zul", contentDiv, map);
	}

	public void onClick$mappingMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		createContent("/pages/subscribe/subscribe_layout.zul", contentDiv, map);
	}

	public void onClick$mappingAutoMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		createContent("/pages/subscribe/subscribe_layout_auto.zul", contentDiv, map);
	}
	
	public void onClick$resourceMenuItem() {
		contentDiv.getChildren().clear();
		map = new HashMap<String, Object>();
		map.put("res_status", "0");
		map.put("user", session.getAttribute("user"));
		createContent("/pages/resource/resource_layout.zul", contentDiv, map);
	}
	
	public void onClick$resourceMenuItemSend() {
		contentDiv.getChildren().clear();
		map = new HashMap<String, Object>();
		map.put("res_status", "1");
		map.put("user", session.getAttribute("user"));
		createContent("/pages/resource/resource_layout.zul", contentDiv, map);
	}
	
	/**/
	public void onClick$uploadMenuItem() {
		createContent("/pages/upload/file_upload.zul", contentDiv, null);
	}
	
	public void onClick$quartzMenuItem() {
		createContent("/pages/quartz/quartz.zul", contentDiv, null);
	}
	
	public void onClick$appLogMenuItem() {
		createContent("/pages/system/log/app_log.zul", contentDiv, null);
	}
	
	public void onClick$catchConfiguration() {
		createContent("/pages/system/quartz/quartz_layout.zul", contentDiv, null);
	}
	
	void createContent(String uri, Component parent, Map<String, Object> map){
		contentDiv.getChildren().clear();
		Executions.createComponents(uri, parent, map);
	}
	
	void initGrid(){
		Map<String, String> map = new AppLogDao().getCatchSizeList(0L);
		if(map!=null&&!map.isEmpty()){
			grid.setModel(new ListModelMap(map, true));
			
		}
	}
}
