/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.zk.pages.upload;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Fileupload;

/**
 * @author dongq
 * 
 *         create time : 2009-12-9 上午10:36:34
 */
public class FileUploadComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final String FILE_SUFFIX = "XLS";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public void onClick$upload(){
		try{
			Media media = Fileupload.get();
			String name = media.getName();
			String suffix = name.substring(name.lastIndexOf("."));
			String type = media.getContentType();
			if(FILE_SUFFIX.equalsIgnoreCase(suffix)){
				
				alert("name:"+name+"\n type:"+type);
			}else{
				alert("请上传xls格式的文件");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
