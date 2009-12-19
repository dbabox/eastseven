/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.quartz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.QuartzDaoService;

/**
 * @author dongq
 * 
 *         create time : 2009-12-6 ÏÂÎç04:20:06
 */
public class QuartzComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Listbox listbox;
	private Listhead listhead;
	private Grid grid;
	private Columns columns;
	
	private QuartzDaoService dao;
	
	public QuartzComposer() {
		dao = new QuartzDaoService();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		String tableName = (String)arg.get("tableName");
		if(tableName!=null&&!"".equals(tableName)){
			List<Map<String, Object>> list = dao.getListAll(tableName);
			List<String> columnName = dao.getTableColumnName(tableName);
			if(columnName!=null&&!columnName.isEmpty()){
//				initListhead(columnName);
				initGridColumns(columnName);
			}
//			if(list!=null&&!list.isEmpty()){
//				listbox.setModel(new ListModelList(list));
//				listbox.setItemRenderer(new ItemRendererImpl());
//			}
			
			if(list!=null&&!list.isEmpty()){
				grid.setModel(new ListModelList(list));
				grid.setRowRenderer(new RowRendererImpl());
			}
		}
	}
	
	void initListhead(List<String> columnName) throws Exception {
		for(String name : columnName){
			listhead.appendChild(new Listheader(name));
		}
		if(listhead.getChildren()!=null&&!listhead.getChildren().isEmpty()){
			listhead.setParent(listbox);
		}
	}
	
	void initGridColumns(List<String> columnName) throws Exception {
		columns.getChildren().clear();
		int size = columnName.size();
		if(size<=3){
			for(int index=0;index<size;index++){
				columns.appendChild(new Column(columnName.get(index)));
			}
		}else{
			for(int index=0;index<3;index++){
				if(index==0){
					Column column = new Column("");
					column.setWidth("25px");
					columns.appendChild(column);
				}
				columns.appendChild(new Column(columnName.get(index)));
			}
		}
	}
	
	class RowRendererImpl implements RowRenderer {

		Detail detail;
		Html html;
		
		@SuppressWarnings("unchecked")
		@Override
		public void render(Row row, Object obj) throws Exception {
			Map<String, Object> map = (Map<String, Object>)obj;
			Set<String> keySet = map.keySet();
			int size = map.size();
			
			if(size<=3){
				for(String key : keySet){
					row.appendChild(new Label(map.get(key).toString()));
				}
			}else{
				int count = 0;
				for(String key : keySet){
					if(count==0){
						String content = "<TABLE>";
						for(String k : keySet){
							content += "<TR><TD>"+k+"</TD><TD>"+(map.get(k)!=null?map.get(k).toString():"")+"</TD></TR>";
						}
						content += "</TABLE>";
						html = new Html(content);
						detail = new Detail();
						detail.appendChild(html);
						row.appendChild(detail);
						row.appendChild(new Label(map.get(key).toString()));
					}else if(count==1||count==2){
						String label = map.get(key)!=null?map.get(key).toString():"";
						row.appendChild(new Label(label));
					}else{
						break;
					}
					count++;
				}
			}
			
		}
		
	}
	
	class ItemRendererImpl implements ListitemRenderer {

		List<Listheader> list;
		
		@SuppressWarnings("unchecked")
		public ItemRendererImpl() {
			list = listhead.getChildren();
		}
		
		@SuppressWarnings("unchecked")
		public void render(Listitem item, Object data) throws Exception {
			if(data instanceof Map && list != null){
				Map<String, Object> map = (Map<String, Object>)data;
				
				for(Listheader listheader : list){
					Object value = map.get(listheader.getLabel());
					if(value!=null)
						item.appendChild(new Listcell(value.toString()));
					else
						item.appendChild(new Listcell());
				}
			}
		}
		
	}
}
