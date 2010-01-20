/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.CrawlDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ChannelBean;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.CrawlBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-11 下午03:28:25
 */
public class CatchConfigComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Listbox listbox;
	private Textbox crawlUrl;
	private Button add, setup, stop, del;
	
	private ListitemRenderer renderer;
	private ListModelList listModel;
	
	private CrawlDao dao; 
	private ChannelBean channelBean;
	private CrawlBean crawlBean;
	private boolean live = true;
	
	public CatchConfigComposer() {
		dao = new CrawlDao();
	}
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		renderer = new CrawlListitemRenderer();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(arg != null){
			channelBean = (ChannelBean)arg.get("channelBean");
		}
		
		if(channelBean != null){
			initCommonsComponent();
			initListbox();
		}
	}
	
	void initCommonsComponent() {
		add.setId("btn_add_cat_"+channelBean.getChannelId());
		setup.setId("btn_setup_cat_"+channelBean.getChannelId());
		stop.setId("btn_stop_cat_"+channelBean.getChannelId());
		del.setId("btn_del_cat_"+channelBean.getChannelId());
		crawlUrl.setId("tb_crawlurl_cat_"+channelBean.getChannelId());
	}
	
	void initListbox() {
		listbox.setId("listbox_cat_"+channelBean.getChannelId());
		List<CrawlBean> list = dao.getCrawlList(channelBean.getChannelId());
		listModel = new ListModelList(list, live);
		
		listbox.setModel(listModel);
		listbox.setItemRenderer(renderer);
	}
	
	//添加配置
	public void onClick$add(){
		/**/
		if(crawlUrl.getValue()!= null && !"".equals(crawlUrl.getValue())){
			String url = crawlUrl.getValue();
			//提示Tip
			boolean bln = tip(url);
			if(bln){
				crawlBean = new CrawlBean();
				crawlBean.setChannelId(channelBean.getChannelId());
				crawlBean.setCrawlUrl(url);
				crawlBean.setCrawlStatus("正常");
				crawlBean.setChannelName(channelBean.getChannelName());
				crawlBean.setCreateTime(sdf.format(new Date(System.currentTimeMillis())));
				bln = dao.save(crawlBean);
				if(bln){
					listModel.add(crawlBean);
					alert("操作成功,网址"+url+"下的内容将在1小时后开始抓取,若24小时内未抓取到任何内容,请联系技术部.");
				}else{
					alert("操作失败");
				}
			}
		}else{
			alert("抓取地址不能为空");
		}
		
	}
	
	boolean tip(String url){
		boolean bln = false;
		Properties property = ConfigPropertyUtil.property;
		if(property!=null){
			String canCrawl = property.getProperty("can.crawl.website");
			if(canCrawl!=null&&!"".equals(canCrawl)){
				String[] website = canCrawl.split(",");
				for(String site : website){
					System.out.println("site:"+site);
					if(url.contains(site)){
						bln = true;
						break;
					}
				}
				if(!bln){
					try {
						String msg = "目前还不能抓取"+url+"下的内容,可向技术部发起该网址的抓取需求.确定还要添加该网址?";
						if(Messagebox.show(msg, "友情提示", Messagebox.YES|Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
							bln = true;
						}else{
							bln = false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bln;
	}
	
	//启用
	@SuppressWarnings("unchecked")
	public void onClick$setup(){
		/**/
		//多选
		if(listbox.getSelectedCount() == 0){
			alert("请选择要操作的记录");
		}else if(listbox.getSelectedCount() > 0){
			Set<Listitem> set = listbox.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				List<CrawlBean> crawlBeans = new ArrayList<CrawlBean>();
				for(Listitem item : set){
					CrawlBean bean = (CrawlBean)item.getValue();
					listModel.remove(bean);
					bean.setCrawlStatus("启用");
					crawlBeans.add(bean);
					list.add(bean.getCrawlId());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "1");
					if(bln){
						listModel.removeAll(crawlBeans);
						listModel.addAll(crawlBeans);
						alert("操作成功");
					}else{
						alert("操作失败");
					}
				}
			}
		}
		
	}
	
	//停用
	@SuppressWarnings("unchecked")
	public void onClick$stop(){
		/**/
		//多选
		if(listbox.getSelectedCount() == 0){
			alert("请选择要操作的记录");
		}else if(listbox.getSelectedCount() > 0){
			Set<Listitem> set = listbox.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				List<CrawlBean> crawlBeans = new ArrayList<CrawlBean>();
				for(Listitem item : set){
					CrawlBean bean = (CrawlBean)item.getValue();
					bean.setCrawlStatus("停用");
					crawlBeans.add(bean);
					list.add(bean.getCrawlId());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "0");
					if(bln){
						listModel.removeAll(crawlBeans);
						listModel.addAll(crawlBeans);
						alert("操作成功");
					}else{
						alert("操作失败");
					}
				}
			}
		}
		
	}
	
	//删除
	@SuppressWarnings("unchecked")
	public void onClick$del() {
		int count = this.listbox.getSelectedCount();
		boolean bln = false;
		if(count>1){
			Set<Listitem> set = this.listbox.getSelectedItems();
			Iterator<Listitem> iter = set.iterator();
			List<CrawlBean> crawlBeans = new ArrayList<CrawlBean>();
			while(iter.hasNext()){
				CrawlBean bean = (CrawlBean)iter.next().getValue();
				crawlBeans.add(bean);
			}
			bln = dao.delete(crawlBeans);
			if(!bln){
				alert("删除失败");
			}else{
				listModel.removeAll(crawlBeans);
			}
		}else if(count==1){
			crawlBean = (CrawlBean)this.listbox.getSelectedItem().getValue();
			bln = dao.delete(crawlBean);
			if(!bln){
				alert("删除失败");
			}else{
				listModel.remove(crawlBean);
			}
		}else if(count==0){
			alert("请选择要操作的记录");
		}
		
	}
	
	class CrawlListitemRenderer implements ListitemRenderer{
		
		int id = 1;
		
		@Override
		public void render(Listitem item, Object data) throws Exception {		
			CrawlBean bean = (CrawlBean)data;
			item.appendChild(new Listcell());
			item.appendChild(new Listcell(String.valueOf(id)));
			item.appendChild(new Listcell(bean.getChannelName()));
			Listcell html = new Listcell();
			html.appendChild(new Html("<a href='"+bean.getCrawlUrl()+"' target='_blank'>"+bean.getCrawlUrl()+"</a>"));
			item.appendChild(html);
			item.appendChild(new Listcell(bean.getCrawlStatus()));
			item.appendChild(new Listcell(bean.getCreateTime()));
			
			item.setValue(bean);
			id++;
		}
	}
}
