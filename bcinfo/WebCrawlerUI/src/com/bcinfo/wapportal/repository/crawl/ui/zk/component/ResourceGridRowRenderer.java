/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.component;

import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.ResourceBean;

/**
 * @author dongq
 * 
 *         create time : 2010-1-8 ÏÂÎç04:18:35
 */
public class ResourceGridRowRenderer implements RowRenderer {

	private String height = "15px";

	private Detail detail;
	private Checkbox checkbox;
	private Label label;
	private Html html;
	//private Include include;

	@Override
	public void render(Row row, Object data) throws Exception {

		ResourceBean bean = (ResourceBean) data;
		String title = bean.getTitle();

		detail = new Detail();
		detail.setHeight("100%");
		html = new Html();
		//include = new Include("pages/component/res_edit_form.zul");
		
		html.setContent(bean.getContent());
		detail.appendChild(html);
		row.appendChild(detail);
		
		checkbox = new Checkbox();
		row.appendChild(checkbox);

		label = new Label();
		label.setValue(bean.getChannelName());
		row.appendChild(label);

		row.appendChild(new Html("<a href='" + bean.getLink() + "' target='_blank' style='font-size:15px'>" + title + "</a>"));

		Button btn = new Button("±à¼­");
		row.appendChild(btn);

		label = new Label();
		label.setValue(bean.getStatus());
		row.appendChild(label);

		label = new Label();
		label.setValue(bean.getPics());
		row.appendChild(label);

		label = new Label();
		label.setValue(bean.getWords());
		row.appendChild(label);

		label = new Label();
		label.setValue(bean.getCreateTime());
		row.appendChild(label);

		row.setHeight(height);

	}

}
