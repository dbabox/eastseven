/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;

import com.bcinfo.wapportal.repository.crawl.core.HtmlParse;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.core.ParseFactory;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-14 ����10:12:38
 */
public class WebCrawlerDefaultImpl implements WebCrawler {

	private static Logger log = Logger.getLogger(WebCrawlerDefaultImpl.class);
	
	private HtmlParse htmlParse;
	private FileOperation fileOperation;
	//private HandleContent handleContent;
	
	private Parse parseService;
	
	public WebCrawlerDefaultImpl() {
		this.htmlParse = new HtmlParseDefaultImpl();
		this.fileOperation = new FileOperation();
		//this.handleContent = new HandleContent();
	}
	
	@Override
	public List<FolderBO> crawl(String folderId, String url) {
		List<FolderBO> folders = null;
		try{
			//ȡ�ÿ��õ�ַ
			List<FolderBO> usableFolders = htmlParse.getUsableCrawlLink(folderId, url);
			parseService = ParseFactory.getParseInstance(url);
			folders = new ArrayList<FolderBO>();
			for(FolderBO folder : usableFolders){
				String title = folder.getTitle();
				String link = folder.getLink();
				String content = parseService.parse(link);
				
				//TODO ��¼��־,���ܳɹ����
				fileOperation.writeLog(folderId, link);
				/*
				boolean bln = fileOperation.writeLog(link);
				if(log.isDebugEnabled()){
					log.debug("   ��¼��־ "+link+" = "+bln);
				}
				*/
				
				if(content == null || "".equals(content) || "null".equals(content)) continue;
				if(content != null){
					
					//TODO ��ʽ���ı�����
					
					String[] tmp = content.split(RegexUtil.REGEX_BR);
					Pattern pattern = Pattern.compile(RegexUtil.REGEX_IMG);
					Parser parser = new Parser();
					content = "";
					for(int i=0;i<tmp.length;i++){
						String cnt = tmp[i].trim();
						if(cnt.length()>0){
							cnt = cnt.replaceAll("��", "");
							if(!cnt.startsWith("<")){
								cnt = RegexUtil.REGEX_BR + "��" + cnt;
							}else{
								//TODO ����ͼƬ�����滻�ı��е�ͼƬ��ַ
								String img = "";
								Matcher matcher = pattern.matcher(cnt);
								while(matcher.find()){
									int start = matcher.start();
									int end = matcher.end();
									String inputHTML =cnt.substring(start, end); 
									parser.setInputHTML(inputHTML);
									NodeIterator iter = parser.elements();
									ImageTag imgTag = (ImageTag)iter.nextNode();
									String originUrl = imgTag.getImageURL();
									String fileName = originUrl.substring(originUrl.lastIndexOf("/")+1);
									String localUrl = fileOperation.writeFile(originUrl, fileName);
									if(localUrl!=null){
										//�滻����IMG��ǩ
										img = "<img src="+"http://127.0.0.1"+localUrl+">";
										cnt = cnt.replace(inputHTML, img);
										//System.out.println("   ԭʼ�� "+inputHTML+"| ���أ�"+img);
										System.out.println("");
									}
								}
							}
						}
						content += cnt;
					}
					
					if(log.isDebugEnabled()){
						System.out.println("------------------------ץȡ-------------------------------");
						System.out.println(link+" | "+title);
						
						System.out.println("------------------------��ʽ���ı�����-------------------------------");
						System.out.println(content);
						System.out.println(" ");
					}
					/**/
					folders.add(new FolderBO(folderId, title, link, content));
				}
				
			}
			
			//�ֶδ���
			/*
			List<FolderBO> list = handleContent.handleFolders(folders);
			if(log.isDebugEnabled()){
				int count = 1;
				for(FolderBO folder : list){
					log.debug(count);
					log.debug(folder.getFolderId()+" | "+folder.getLink()+" | "+folder.getTitle());
					List<ResourceBO> res = folder.getResources();
					int segment = 1;
					for(ResourceBO r : res){
						if(r.getType() == ResourceType.PIC){
							log.debug(segment+"  "+r.getContent()+" | "+r.getPath());
						}else if (r.getType() == ResourceType.WORDS){
							log.debug(segment+"  "+r.getContent());
						}
						segment++;
					}
					System.out.println(" ");
					count++;
				}
			}
			if(list!=null && !list.isEmpty()){
				folders.clear();
				folders.addAll(list);
			}
			*/
		}catch(Exception e){
			System.out.println("[Ŀ����Ŀ:"+folderId+" "+url+"]ץȡʧ��");
			if(log.isDebugEnabled()){
				log.debug(e);
			}
		}
		
		return folders;
	}

	
}
