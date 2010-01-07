package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ChannelDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 *
 */
public class ChannelTreeModel extends AbstractTreeModel{

	private static final long serialVersionUID = 1L;
	
	private ChannelDao dao;
	private List<ChannelBean> list;
	
	public ChannelTreeModel(Object root) {
		super(root);
		dao = new ChannelDao();
		list = dao.getChannelList((Long)root);
	}

	@Override
	public Object getChild(Object parent, int index) {
		Object obj = null;
		if(parent instanceof Long){
			obj = list.get(index);
		}else if(parent instanceof ChannelBean){
			obj = dao.getChannelList(((ChannelBean)parent).getChannelId()).get(index);
		}
		return obj;
	}

	@Override
	public int getChildCount(Object parent) {
		int count = 0;
		if(parent instanceof Long){
			count = list.size();
		}else if(parent instanceof ChannelBean){
			count = dao.getChannelList(((ChannelBean)parent).getChannelId()).size();
		}
		return count;
	}

	@Override
	public boolean isLeaf(Object node) {
		boolean leaf = false;
		if(node instanceof ChannelBean){
			leaf = dao.isLeaf((ChannelBean)node);
		}
		return leaf;
	}

}
