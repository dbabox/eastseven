package com.bcinfo.wapportal.repository.crawl.ui.zk.pages;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menubar;

/**
 * @author dongq
 * 
 */
public class BorderLayoutComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

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
			Executions.createComponents("/pages/login/login.zul", contentDiv, null);
		}else{
			menubar.setVisible(true);
		}
	}

	public void onClick$channelMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		Executions.createComponents("/pages/channel/channel_layout.zul", contentDiv, map);
	}

	public void onClick$catchMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		Executions.createComponents("/pages/catch/catch_layout.zul", contentDiv, map);
	}

	public void onClick$mappingMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		Executions.createComponents("/pages/subscribe/subscribe_layout.zul", contentDiv, map);
	}

	public void onClick$mappingAutoMenuItem() {
		map = new HashMap<String, Object>();
		map.put("user", session.getAttribute("user"));
		contentDiv.getChildren().clear();
		Executions.createComponents("/pages/subscribe/subscribe_layout_auto.zul", contentDiv, map);
	}
	
	public void onClick$resourceMenuItem() {
		contentDiv.getChildren().clear();
		map = new HashMap<String, Object>();
		map.put("res_status", "0");
		map.put("user", session.getAttribute("user"));
		Executions.createComponents("/pages/resource/resource_layout.zul", contentDiv, map);
	}
	
	public void onClick$resourceMenuItemSend() {
		contentDiv.getChildren().clear();
		map = new HashMap<String, Object>();
		map.put("res_status", "1");
		map.put("user", session.getAttribute("user"));
		Executions.createComponents("/pages/resource/resource_layout.zul", contentDiv, map);
	}
}
