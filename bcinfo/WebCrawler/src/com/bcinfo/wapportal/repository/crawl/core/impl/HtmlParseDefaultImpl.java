/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.wapportal.repository.crawl.core.HtmlParse;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;
import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
import com.bcinfo.wapportal.repository.crawl.util.RomeRSS;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����10:27:05
 */
public class HtmlParseDefaultImpl implements HtmlParse {

	//private Parser parser;
	
	private FileOperation fileOperation;
	
	public HtmlParseDefaultImpl() {
		this.fileOperation = new FileOperation();
	}
	
	@Override
	public List<FolderBO> getUsableCrawlLink(String folderId, String url) {
		List<FolderBO> usableLinks = null;
		List<FolderBO> folders = null;
		try{
			
			//ȡ�ô���ȡ��link���ϣ����ദ��url��URL��RSS
			if(url.startsWith("http://rss.")){
				folders = new RomeRSS().getAllLinks(url);
			}else{
				//TODO ò��HTMLParser�ֿ��Խ�JS���ݽ���Ϊ��̬ҳ����
				//�ۺϴ���ҳ������ץȡ�������������ݴ����js�ű��е����
				folders = CrawlerUtil.getAllLinks(url);
			}
			
			usableLinks = new ArrayList<FolderBO>();
			//TODO ��log�ļ��еļ�¼�Ƚϣ���ɸѡ������ȡ�ļ��Ϸ���
			List<String> logLinks = fileOperation.readLog(folderId);
			for(FolderBO folder : folders){
				String link = folder.getLink();
				
				if(folder.getTitle() == null) continue;
				else if("".equals(folder.getTitle()) || folder.getTitle().length() == 0) continue;
				
				if(!logLinks.contains(link)) usableLinks.add(folder);
				//TODO �������ݱȶ�
				//if(!fileOperation.contains(link)) usableLinks.add(folder);
			}
			
		}catch(Exception e){
			System.out.println("ɸѡ["+url+"]������ȡ��ַʧ��");
		}
		return usableLinks;
	}

}
