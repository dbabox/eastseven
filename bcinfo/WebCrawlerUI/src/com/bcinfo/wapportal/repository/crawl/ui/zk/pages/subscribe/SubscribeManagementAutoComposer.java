/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.SubscribeDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.SubscribeBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * @author dongq
 * 
 *         create time : 2009-11-24 ÏÂÎç04:22:25
 */
public class SubscribeManagementAutoComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Listbox subscribeListbox;
	private ListitemRenderer renderer = new SubscribeListitemRenderer();
	private Textbox folderId;
	
	private boolean live = true;
	private SubscribeDao dao;
	private UserBean user;
	private ChannelBean bean;
	
	public SubscribeManagementAutoComposer() {
		dao = new SubscribeDao();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Object obj = session.getAttribute("user");
		if(obj != null && arg != null){
			user = (UserBean)obj;
			bean = (ChannelBean)arg.get("bean");
			List<SubscribeBean> list = dao.getAutoSubscribeList(user.getUserId(), bean.getChannelId());
			if(list != null && !list.isEmpty()){
				subscribeListbox.setModel(new ListModelList(list, live));
				subscribeListbox.setItemRenderer(renderer);
			}
		}
	}
	
	//×Ô¶¯¶©¹º
	public void onClick$add(){
		if(folderId.getValue() != null && !"".equals(folderId.getValue()) && !"null".equals(folderId.getValue())){
			SubscribeBean order = new SubscribeBean();
			order.setChannelId(bean.getChannelId());
			order.setLocalCode(user.getLocalCode());
			order.setLocalFolderId(folderId.getValue());
			order.setUserId(user.getUserId());
			
			boolean isSave = dao.saveAutoSubscribeBean(order);
			if(isSave){
				List<SubscribeBean> list = dao.getAutoSubscribeList(user.getUserId(), bean.getChannelId());
				if(list != null && !list.isEmpty()){
					subscribeListbox.setModel(new ListModelList(list, live));
					subscribeListbox.setItemRenderer(renderer);
				}
				alert("¶©¹º³É¹¦");
			}else{
				alert("¶©¹ºÊ§°Ü");
			}
		}else{
			alert("ÇëÌîÐ´Òª¶©¹ºµÄÄ¿µÄÀ¸Ä¿ºÅ");
		}
	}
	
	public void onClick$mod(){
		
	}
	
	//×Ô¶¯ÍË¶©
	@SuppressWarnings("unchecked")
	public void onClick$del(){
		if(subscribeListbox.getSelectedCount() > 0){
			Set set = subscribeListbox.getSelectedItems();
			Iterator iter = set.iterator();
			List<Long> list = new ArrayList<Long>();
			while(iter.hasNext()){
				Listitem item = (Listitem)iter.next();
				list.add(((SubscribeBean)item.getValue()).getMappingId());
			}
			boolean bln = dao.deleteAuto(list);
			if(bln){
				List<SubscribeBean> _list = dao.getAutoSubscribeList(user.getUserId(), bean.getChannelId());
				if(_list != null && !_list.isEmpty()){
					subscribeListbox.setModel(new ListModelList(_list, live));
					subscribeListbox.setItemRenderer(renderer);
				}
				alert("ÍË¶©³É¹¦");
			}else{
				alert("ÍË¶©Ê§°Ü");
			}
		}else{
			alert("ÇëÑ¡ÔñÒªÍË¶©µÄ¼ÇÂ¼");
		}
	}
	
	class SubscribeListitemRenderer implements ListitemRenderer {
		@Override
		public void render(Listitem item, Object data) throws Exception {
			if(data instanceof SubscribeBean){
				SubscribeBean bean = (SubscribeBean)data;
				
				item.appendChild(new Listcell(bean.getMappingId().toString()));
				item.appendChild(new Listcell(bean.getLocalCode()));
				item.appendChild(new Listcell(bean.getChannelName()));
				item.appendChild(new Listcell(bean.getLocalFolderId()));
				
				item.setValue(data);
			}
			
		}
	}
}
