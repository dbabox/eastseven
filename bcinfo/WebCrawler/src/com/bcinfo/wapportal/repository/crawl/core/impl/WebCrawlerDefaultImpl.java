/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;

import com.bcinfo.wapportal.repository.crawl.core.HtmlParse;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.core.ParseFactory;
import com.bcinfo.wapportal.repository.crawl.core.WebCrawler;
import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;
import com.bcinfo.wapportal.repository.crawl.util.CrawlerUtil;
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
			if(parseService!=null){
				
				folders = new ArrayList<FolderBO>();
				for(FolderBO folder : usableFolders){
					String title = folder.getTitle();
					String link = folder.getLink();
					String content = parseService.parse(link);
					String imgPathSet = "";
					//TODO ��¼��־,���ܳɹ����
					fileOperation.writeLog(folderId, link);
					/*
				boolean bln = fileOperation.writeLog(link);
				if(log.isDebugEnabled()){
					log.debug("   ��¼��־ "+link+" = "+bln);
				}
					 */
					
					if(content == null || "".equals(content) || "null".equals(content)){
						if(log.isDebugEnabled()){
							System.out.println("δȡ�����ݣ� LINK:"+link+" | TITLE:"+title);
						}
						continue;
					}
					if(content != null){
						if(log.isDebugEnabled()){
							System.out.println("------------------------ץȡ��������-------------------------------");
							System.out.println(content);
						}
						//TODO ��ʽ���ı�����
						content = content.replaceAll(">", "><br/>");
						String[] tmp = content.split(RegexUtil.REGEX_BR);
						Pattern pattern = Pattern.compile(RegexUtil.REGEX_IMG);
						Parser parser = new Parser();
						content = "";
						for(int i=0;i<tmp.length;i++){
							String cnt = tmp[i];
							if(cnt==null) continue;
							cnt = cnt.trim();
							if(cnt.length()>0){
								if(log.isDebugEnabled()){
									System.out.println("  "+i+"��<br/>split��ʽ��[size:"+cnt.length()+";total:"+tmp.length+"]��"+cnt);
								}
								//cnt = cnt.replaceAll("��", "");
								if(!cnt.startsWith("<")){
									cnt = RegexUtil.REGEX_BR + "��" + cnt;
								}else{
									//TODO ����ͼƬ�����滻�ı��е�ͼƬ��ַ
									String httpHeader = CrawlerUtil.extractLinkHeader(link);//������ַ
									String img = "";
									Matcher matcher = pattern.matcher(cnt);
									while(matcher.find()){
										
										int start = matcher.start();
										int end = matcher.end();
										String inputHTML =cnt.substring(start, end); 
										//System.out.println(" ***** ["+matcher.groupCount()+"]["+start+"-"+end+"]["+inputHTML+"] ***** ");
										parser.setInputHTML(inputHTML);
										NodeIterator iter = parser.elements();
										Node node = iter.nextNode();
										ImageTag imgTag = (ImageTag)node;
										String originUrl = imgTag.getImageURL();
										//TOM��ͼƬ������Щ�����·������ʱҪ�ֶ����ӳɾ���·������������ͼƬ�ǻᱨ��ָ�����
										if(!originUrl.contains("http://")){
											originUrl = CrawlerUtil.addLinkHeader(originUrl, httpHeader);
										}
										String fileName = originUrl.substring(originUrl.lastIndexOf("/")+1);
										//TODO ͼƬ���������أ�����
										String localUrl = fileOperation.writeFile(originUrl, fileName);
										if(localUrl!=null){
											//imgPathSet += localUrl+",";
											imgPathSet += originUrl+",";//���ݿⱣ��ʵ�ʵ�ͼƬ���ӵ�ַ
											//TODO �滻����IMG��ǩ
											//img = "<img src="+"http://127.0.0.1"+localUrl+">";
											
											//TODO ��ʽ��ԭIMG��ǩ��ֻ����src����
											img = "<img src="+originUrl+">";
											cnt = cnt.replace(inputHTML, img);
											//System.out.println("   ԭʼ�� "+inputHTML+"| ���أ�"+img);
										}
									}
								}
								content += cnt;
							}
						}
						
						if(log.isDebugEnabled()){
							System.out.println("------------------------ץȡ-------------------------------");
							System.out.println(link+" | "+title);
							
							System.out.println("------------------------��ʽ���ı�����-------------------------------");
							System.out.println("����                      :"+content);
							System.out.println("ͼƬ���·��  :"+imgPathSet);
							System.out.println(" ");
						}
						/**/
						folders.add(new FolderBO(folderId, title, link, content,imgPathSet));
					}
					
				}
				
			}else{
				System.out.println(" ץȡ��ַ["+url+"]Ŀǰ�����ܽ���... ");
			}
		}catch(Exception e){
			System.out.println("[Ŀ����Ŀ:"+folderId+" "+url+"]ץȡʧ��");
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return folders;
	}

	
}