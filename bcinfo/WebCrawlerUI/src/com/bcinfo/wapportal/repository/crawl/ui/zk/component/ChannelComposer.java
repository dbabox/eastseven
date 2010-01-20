/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ChannelDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-13 下午05:30:06
 */
public class ChannelComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Tree tree;
	private Label channelId;
	private Textbox channelName;
	private Button add, addRoot, mod, del;
	private TreeModel model;
	
	private ChannelDao dao;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		model = new ChannelTreeModel(0L);
		dao = new ChannelDao();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String cmd = (String) arg.get("cmd");
		
		channelId.setId("label_chl"+cmd);
		add.setId("btn_chl_add_"+cmd);
		addRoot.setId("btn_chl_add_root_"+cmd);
		mod.setId("btn_chl_mod_"+cmd);
		del.setId("btn_chl_del_"+cmd);
		tree.setId("tree_" + cmd);
		tree.setModel(model);
		tree.setTreeitemRenderer(new TreeitemRendererImpl());
	}

	public void onSelect$tree() {
		ChannelBean bean = (ChannelBean)this.tree.getSelectedItem().getValue();
		this.channelId.setValue(bean.getChannelId().toString());
		this.channelName.setValue(bean.getChannelName());
	}
	
	public void onClick$add() {
		if(this.tree.getSelectedCount()>0){
			ChannelBean parent = (ChannelBean)this.tree.getSelectedItem().getValue();
			ChannelBean bean = new ChannelBean();
			bean.setChannelName(channelName.getValue());
			bean.setChannelPid(parent.getChannelId());
			boolean bln = dao.save(bean);
			if(bln){
				model = new ChannelTreeModel(0L);
				tree.setModel(model);
			}else{
				alert("操作失败");
			}
		}else{
			alert("请选择一个频道");
		}
	}
	
	public void onClick$addRoot() {
		//ChannelBean parent = (ChannelBean)this.tree.getSelectedItem().getValue();
		ChannelBean bean = new ChannelBean();
		bean.setChannelName(channelName.getValue());
		bean.setChannelPid(0L);
		bean.setChannelIndex("1");
		bean.setChannelPath("/");
		boolean bln = dao.save(bean);
		if(bln){
			model = new ChannelTreeModel(0L);
			tree.setModel(model);
		}else{
			alert("操作失败:"+bean);
		}
	}
	
	public void onClick$mod() {
		if(this.tree.getSelectedCount()>0){
			ChannelBean bean = (ChannelBean)this.tree.getSelectedItem().getValue();
			bean.setChannelName(channelName.getValue());
			boolean bln = dao.save(bean);
			if(bln){
				model = new ChannelTreeModel(0L);
				tree.setModel(model);
			}else{
				alert("操作失败:"+bean);
			}
		}else{
			alert("请选择一个频道");
		}
	}
	
	public void onClick$del() {
		if(this.tree.getSelectedCount()>0){
			ChannelBean bean = (ChannelBean)this.tree.getSelectedItem().getValue();
			boolean bln = dao.delete(bean.getChannelId());
			if(bln){
				model = new ChannelTreeModel(0L);
				tree.setModel(model);
			}else{
				alert("操作失败:"+bean);
			}
		}else{
			alert("请选择一个频道");
		}
	}
	
	class TreeitemRendererImpl implements TreeitemRenderer {
		private String channelId;
		private String channelName;
		private Treerow treerow;
		@Override
		public void render(Treeitem item, Object data) throws Exception {
			treerow = new Treerow();
			ChannelBean bean = (ChannelBean) data;
			channelId = bean.getChannelId().toString();
			channelName = bean.getChannelName();
			treerow.appendChild(new Treecell(channelId));
			treerow.appendChild(new Treecell(channelName));
			item.appendChild(treerow);
			item.setValue(data);
		}
	}
}
