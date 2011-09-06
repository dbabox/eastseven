/**
 * 
 */
package org.dongq.analytics.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dongq.analytics.service.QuestionnairePaperService;
import org.dongq.analytics.service.QuestionnairePaperServiceImpl;
import org.zkoss.util.media.Media;
import org.zkoss.zk.Version;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 * @author eastseven
 * 
 */
public class IndexComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(IndexComposer.class);
	
	private final String FILE_SUFFIX = "xls,xlsx";
	
	Div mainDiv;
	QuestionnairePaperService service;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		service = new QuestionnairePaperServiceImpl();
	}
	
	/**
	 * 上传
	 * @param event
	 */
	public void onUpload$file(UploadEvent event) {
		Media file = event.getMedia();
		logger.info(file.getName());
		logger.info(file.getContentType());
		logger.info(file.getFormat());
		if(FILE_SUFFIX.contains(file.getFormat().toLowerCase())) {
			InputStream excel = file.getStreamData();
			logger.info(excel);
			boolean bln = service.parseQuestionnaireTemplate(excel);
			String msg = bln ? "问卷导入成功" : "问卷导入失败";
			try {
				Messagebox.show(msg, "title", Messagebox.OK, "", new EventListener() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						mainDiv.getChildren().clear();
						Executions.createComponents("questionnaire_grid.zul", mainDiv, null);
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			alert("非excel格式文件不能上传");
		}
	}
	
	/**
	 * 下载
	 */
	public void onClick$download() {
		logger.info(application.getRealPath("/"));
		logger.info(application.getResourcePaths("/"));
		try {
			Filedownload.save(this.application.getResource("/template.xls"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//
	public void onClick$open() {
		mainDiv.getChildren().clear();
		execution.createComponents("questionnaire_grid.zul", mainDiv, null);
	}

	public void onClick$answer() {
		execution.sendRedirect("questionnaire_login.zul", "blank");
	}
	
	public void onClick$about() {
		this.alert(Version.RELEASE);
	}
}
