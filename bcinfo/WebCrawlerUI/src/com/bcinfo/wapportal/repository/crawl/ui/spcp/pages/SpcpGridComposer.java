/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.pages;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.spcp.dao.SpcpDao;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.PageFolderBean;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.ResourceBean;
import com.bcinfo.wapportal.repository.crawl.ui.spcp.domain.ResourceType;

/**
 * @author dongq
 * 
 *         create time : 2009-12-23 下午05:14:42
 */
public class SpcpGridComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private Grid grid;
	private String folderId;
	private List<PageFolderBean> list;
	private SpcpDao dao;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		folderId = (String)arg.get("folderId");
		if(folderId!=null&&!"".equals(folderId)){
			dao = new SpcpDao();
			boolean isLeaf = dao.isLeaf(folderId);
			if(isLeaf){
				//001001
				List<ResourceBean> resList = dao.getFolderResource(folderId);
				if(resList!=null&&!resList.isEmpty()){
					grid.setModel(new ListModelList(resList, true));
					grid.setRowRenderer(new FolderGridRenderer());
				}
			}else{
				list = dao.getPageFolderList(folderId);
				if(list!=null&&!list.isEmpty()){
					grid.setModel(new ListModelList(list, true));
					grid.setRowRenderer(new FolderGridRenderer());
				}
			}
		}
	}
	
	class FolderGridRenderer implements RowRenderer {

		@Override
		public void render(Row row, Object data) throws Exception {
			if(data instanceof PageFolderBean){
				PageFolderBean bean = (PageFolderBean)data;
				row.appendChild(new Detail());
				row.appendChild(new Label(bean.getFolderId()));
				row.appendChild(new Label(bean.getFolderName()));
				row.appendChild(new Label(bean.getFolderStatus()));
				row.appendChild(new Label(bean.getCreateTime()));
			}else if(data instanceof ResourceBean){
				ResourceBean bean = (ResourceBean)data;
				String status = "1".equals(bean.getResStatus())?"已审":"未审";

				String content = "<TABLE>";
				content += "<TR>";
				content += "<TD>类型</TD><TD>"+ResourceType.getTypeName(bean.getResTypeId().intValue())+"</TD>";
				content += "<TD>状态</TD><TD>"+status+"</TD>";
				content += "</TR>";
				
				content += "<TR>";
				content += "<TD>作者</TD><TD>"+bean.getResAuthor()+"</TD>";
				content += "<TD>大小</TD><TD>"+bean.getResSize()+"</TD>";
				content += "</TR>";
				
				content += "<TR>";
				content += "<TD>内容</TD><TD colspan='3'>"+bean.getResContent()+"</TD>";
				content += "</TR>";
				
				content += "<TR>";
				content += "<TD>路径</TD><TD colspan='3'>"+bean.getStoreFilePath()+"</TD>";
				content += "</TR>";
			
				content += "<TR>";
				content += "<TD>描述</TD><TD colspan='3'>"+bean.getResDesc()+"</TD>";
				content += "</TR>";
				content += "</TABLE>";
				
				Html html = new Html(content);
				Detail detail = new Detail();
				detail.appendChild(html);
				row.appendChild(detail);
				row.appendChild(new Label(bean.getResId().toString()));
				row.appendChild(new Label(bean.getFirstName()));
				row.appendChild(new Label(status));
				row.appendChild(new Label(bean.getCreateTime()));
			}
		}
	}
}
