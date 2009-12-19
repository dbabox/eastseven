/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.system.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.bcinfo.wapportal.repository.crawl.dao.AppLogDao;
import com.bcinfo.wapportal.repository.crawl.domain.internal.AppLog;

/**
 * @author dongq
 * 
 *         create time : 2009-12-11 ÉÏÎç11:29:39
 */
public class AppLogGridComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private Grid grid;
	private Label size;
	private Datebox dateStart;
	private Datebox dateEnd;
	
	private Long channelId;
	private AppLogDao dao;
	
	public AppLogGridComposer() {
		dao = new AppLogDao();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.dateStart.setValue(new Date(System.currentTimeMillis()));
		this.dateEnd.setValue(new Date(System.currentTimeMillis()));
		if(arg != null){
			channelId = (Long)arg.get("channelId");
		}
		if(channelId != null){
			initGrid(sdf.format(this.dateStart.getValue()),sdf.format(this.dateEnd.getValue()));
		}
	}
	
	public void onClick$search() {
		initGrid(sdf.format(this.dateStart.getValue()),sdf.format(this.dateEnd.getValue()));
	}
	
	void initGrid(String start, String end) {
		List<AppLog> list = dao.getAppLogList(channelId,start,end);
		grid.setModel(new ListModelList(list,true));
		grid.setRowRenderer(new RowRendererImpl());

		Long count = dao.getCatchSize(channelId,start,end);
		size.setValue(count.toString());
		
	}
	
	class RowRendererImpl implements RowRenderer {
		@Override
		public void render(Row row, Object obj) throws Exception {
			if(obj instanceof AppLog){
				AppLog appLog = (AppLog)obj;
				Detail detail = new Detail();
				detail.appendChild(new Html("<strong>"+appLog.getLogMessage()+"</strong>"));
				
				row.appendChild(detail);
				row.appendChild(new Label(appLog.getLogId().toString()));
				row.appendChild(new Label(appLog.getChannelName()));
				row.appendChild(new Label(appLog.getUrl()));
				row.appendChild(new Label(appLog.getCatchCount().toString()));
				row.appendChild(new Label(appLog.getCreateTime()));
			}else{
				System.out.println(obj);
			}
		}
	}
}
