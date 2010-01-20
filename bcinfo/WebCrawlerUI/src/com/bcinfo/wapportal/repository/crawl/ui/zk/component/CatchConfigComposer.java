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
 *         create time : 2010-1-11 ����03:28:25
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
	
	//�������
	public void onClick$add(){
		/**/
		if(crawlUrl.getValue()!= null && !"".equals(crawlUrl.getValue())){
			String url = crawlUrl.getValue();
			//��ʾTip
			boolean bln = tip(url);
			if(bln){
				crawlBean = new CrawlBean();
				crawlBean.setChannelId(channelBean.getChannelId());
				crawlBean.setCrawlUrl(url);
				crawlBean.setCrawlStatus("����");
				crawlBean.setChannelName(channelBean.getChannelName());
				crawlBean.setCreateTime(sdf.format(new Date(System.currentTimeMillis())));
				bln = dao.save(crawlBean);
				if(bln){
					listModel.add(crawlBean);
					alert("�����ɹ�,��ַ"+url+"�µ����ݽ���1Сʱ��ʼץȡ,��24Сʱ��δץȡ���κ�����,����ϵ������.");
				}else{
					alert("����ʧ��");
				}
			}
		}else{
			alert("ץȡ��ַ����Ϊ��");
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
						String msg = "Ŀǰ������ץȡ"+url+"�µ�����,���������������ַ��ץȡ����.ȷ����Ҫ��Ӹ���ַ?";
						if(Messagebox.show(msg, "������ʾ", Messagebox.YES|Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
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
	
	//����
	@SuppressWarnings("unchecked")
	public void onClick$setup(){
		/**/
		//��ѡ
		if(listbox.getSelectedCount() == 0){
			alert("��ѡ��Ҫ�����ļ�¼");
		}else if(listbox.getSelectedCount() > 0){
			Set<Listitem> set = listbox.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				List<CrawlBean> crawlBeans = new ArrayList<CrawlBean>();
				for(Listitem item : set){
					CrawlBean bean = (CrawlBean)item.getValue();
					listModel.remove(bean);
					bean.setCrawlStatus("����");
					crawlBeans.add(bean);
					list.add(bean.getCrawlId());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "1");
					if(bln){
						listModel.removeAll(crawlBeans);
						listModel.addAll(crawlBeans);
						alert("�����ɹ�");
					}else{
						alert("����ʧ��");
					}
				}
			}
		}
		
	}
	
	//ͣ��
	@SuppressWarnings("unchecked")
	public void onClick$stop(){
		/**/
		//��ѡ
		if(listbox.getSelectedCount() == 0){
			alert("��ѡ��Ҫ�����ļ�¼");
		}else if(listbox.getSelectedCount() > 0){
			Set<Listitem> set = listbox.getSelectedItems();
			if(set!=null && !set.isEmpty()){
				List<Long> list = new ArrayList<Long>(set.size());
				List<CrawlBean> crawlBeans = new ArrayList<CrawlBean>();
				for(Listitem item : set){
					CrawlBean bean = (CrawlBean)item.getValue();
					bean.setCrawlStatus("ͣ��");
					crawlBeans.add(bean);
					list.add(bean.getCrawlId());
				}
				if(list != null && !list.isEmpty()){
					boolean bln = dao.updateStatus(list, "0");
					if(bln){
						listModel.removeAll(crawlBeans);
						listModel.addAll(crawlBeans);
						alert("�����ɹ�");
					}else{
						alert("����ʧ��");
					}
				}
			}
		}
		
	}
	
	//ɾ��
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
				alert("ɾ��ʧ��");
			}else{
				listModel.removeAll(crawlBeans);
			}
		}else if(count==1){
			crawlBean = (CrawlBean)this.listbox.getSelectedItem().getValue();
			bln = dao.delete(crawlBean);
			if(!bln){
				alert("ɾ��ʧ��");
			}else{
				listModel.remove(crawlBean);
			}
		}else if(count==0){
			alert("��ѡ��Ҫ�����ļ�¼");
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
