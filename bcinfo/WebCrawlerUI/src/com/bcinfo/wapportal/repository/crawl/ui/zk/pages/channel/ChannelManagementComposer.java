package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.ChannelDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;

/**
 * @author dongq
 * 
 */
public class ChannelManagementComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private ChannelBean bean;
	private ChannelDao dao;
	private boolean live = true;

	private Listbox channelListbox;
	private Div channelDiv;

	private ListitemRenderer renderer = new ChannelListboxRenderer();

	public ChannelManagementComposer() {
		dao = new ChannelDao();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Map map = arg;
		if (map != null && !map.isEmpty()) {
			bean = (ChannelBean) map.get("bean");
		}

		if (bean != null) {
			List<ChannelBean> list = dao.getChannelList(bean.getChannelId());
			if (list != null && !list.isEmpty()) {
				channelListbox.setModel(new ListModelList(list, live));
				channelListbox.setItemRenderer(renderer);
				channelListbox.setMultiple(true);
			}
		}
	}

	public void onClick$add() {
		// 单选
		if (bean != null) {
			channelDiv.getChildren().clear();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channelPid", bean.getChannelId());
			Window form = (Window) Executions.createComponents("/pages/channel/channel_form.zul", channelDiv, map);
			try {
				form.setTitle("新增");
				Button save = (Button) form.getFellow("save");
				save.addEventListener(Events.ON_CLICK, new SaveButtonEventListener(form));
				form.doModal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onClick$mod() {
		// 单选
		
		if(channelListbox.getSelectedCount() == 0){
			alert("请选择一条要修改的记录");
		}else if(channelListbox.getSelectedCount() == 1){
			if(bean != null){
				channelDiv.getChildren().clear();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bean", channelListbox.getSelectedItem().getValue());
				Window form = (Window) Executions.createComponents("/pages/channel/channel_edit.zul", channelDiv, map);
				try {
					form.setTitle("修改");
					Button save = (Button) form.getFellow("save");
					save.addEventListener(Events.ON_CLICK, new SaveButtonEventListener(form));
					form.doModal();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(channelListbox.getSelectedCount() > 1){
			alert("一次只能修改一条记录");
		}
		
	}

	public void onClick$del() {
		// 多选
		if(channelListbox.getSelectedCount() > 0){
			try {
				if(Messagebox.show("确定要删除?", "TODO", Messagebox.YES|Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			alert("请选择要删除的一条或多条记录");
		}
	}

	class SaveButtonEventListener implements EventListener {

		private Window win;
		private Longbox channelId;
		private Longbox channelPid;
		private Textbox channelName;
		private Textbox channelPath;
		private Textbox channelIndex;

		private ChannelDao _dao;
		private ChannelBean bean;

		public SaveButtonEventListener(Window win) {
			this.win = win;
			_dao = new ChannelDao();
		}

		@Override
		public void onEvent(Event event) throws Exception {
			if (win != null) {
				channelId = (Longbox) this.win.getFellow("channelId");
				channelPid = (Longbox) this.win.getFellow("channelPid");
				channelName = (Textbox) this.win.getFellow("channelName");
				channelPath = (Textbox) this.win.getFellow("channelPath");
				channelIndex = (Textbox) this.win.getFellow("channelIndex");
				bean = new ChannelBean(channelId.getValue(), channelPid.getValue(), channelName.getValue(), channelPath.getValue(), channelIndex.getValue(), null);
				boolean isSave = _dao.save(bean);
				if (isSave) {
					channelListbox.setModel(new ListModelList(_dao.getChannelList(bean.getChannelPid()), live));
					channelListbox.setItemRenderer(renderer);
					win.onClose();
				}
			}
		}
	}

	class ChannelListboxRenderer implements ListitemRenderer {

		@Override
		public void render(Listitem item, Object data) throws Exception {
			if (data instanceof ChannelBean) {
				ChannelBean bean = (ChannelBean) data;
				
				item.appendChild(new Listcell(bean.getChannelId().toString()));
				item.appendChild(new Listcell(bean.getChannelName()));
				item.appendChild(new Listcell(bean.getChannelPath()));
				item.appendChild(new Listcell(bean.getChannelIndex()));
				item.appendChild(new Listcell(bean.getCreateTime()));
				
				item.setValue(data);
			}
		}

	}
}
