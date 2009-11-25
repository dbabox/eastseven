package com.bcinfo.wapportal.repository.crawl.ui.zk.dao;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.MenuBean;

/**
 * @author dongq
 * 
 */
public class MenuDao {

	public List<MenuBean> getAll(){
		List<MenuBean> list = new ArrayList<MenuBean>();
		
		list.add(new MenuBean(1L, 0L, "Edit", "/", 1L));
		list.add(new MenuBean(12L, 1L, "A", "/", 2L));
		list.add(new MenuBean(13L, 1L, "B", "/", 2L));
		list.add(new MenuBean(14L, 1L, "C", "/", 2L));
		
		list.add(new MenuBean(2L, 0L, "Source", "/", 1L));
		list.add(new MenuBean(3L, 0L, "Refactor", "/", 1L));
		
		return list;
	}
}
