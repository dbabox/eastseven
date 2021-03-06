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
 *         create time : 2009-10-14 上午10:12:38
 */
public class WebCrawlerDefaultImpl implements WebCrawler {

	private static Logger log = Logger.getLogger(WebCrawlerDefaultImpl.class);
	
	private HtmlParse htmlParse;
	private FileOperation fileOperation;
	
	private Parse parseService;
	
	public WebCrawlerDefaultImpl() {
		this.htmlParse = new HtmlParseDefaultImpl();
		this.fileOperation = new FileOperation();
	}
	
	@Override
	public List<FolderBO> crawl(Long crawlId, String folderId, String url) {
		List<FolderBO> folders = null;
		try{
			//取得可用地址
			List<FolderBO> usableFolders = htmlParse.getUsableCrawlLink(folderId, url);
			parseService = ParseFactory.getParseInstance(url);
			if(parseService!=null){
				
				folders = new ArrayList<FolderBO>();
				int count = 1;
				for(FolderBO folder : usableFolders){
					String title = folder.getTitle();
					String link = folder.getLink();
					String content = parseService.parse(link);
					String imgPathSet = "";
					//TODO 记录日志,不管成功与否
					fileOperation.writeLog(folderId, link);
					
					if(content == null || "".equals(content) || "null".equals(content)){
						continue;
					}
					if(content != null){
						//TODO 格式化文本内容
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
								if(!cnt.startsWith("<")){
									cnt = RegexUtil.REGEX_BR + cnt;
								}else{
									//TODO 下载图片，并替换文本中的图片地址
									String httpHeader = CrawlerUtil.extractLinkHeader(link);//顶级地址
									String img = "";
									Matcher matcher = pattern.matcher(cnt);
									while(matcher.find()){
										
										int start = matcher.start();
										int end = matcher.end();
										String inputHTML =cnt.substring(start, end);
										
										//新华网下一页图片
										if(inputHTML.contains("news_xy.gif")) continue;
										if(inputHTML.contains("news_sy.gif")) continue;
										if(inputHTML.contains("news_hy.gif")) continue;
										
										parser.setInputHTML(inputHTML);
										NodeIterator iter = parser.elements();
										Node node = iter.nextNode();
										ImageTag imgTag = (ImageTag)node;
										String originUrl = imgTag.getImageURL();
										
										if(log.isDebugEnabled()){
											//System.out.println("------------------------抓取到[ "+link+" ]图片地址-------------------------------");
											//System.out.println("原始图片地址："+originUrl);
										}
										//TOM的图片链接有些是相对路径，此时要手动添加成绝对路径，否则，下载图片是会报空指针错误
										if(!originUrl.contains("http://")){
											if(originUrl.startsWith("xin")){
												//新华网的图片链接地址是放在当前页面下的，如：<IMG src"xin_33210062622088432258116.jpg" border0>
												originUrl = link.substring(0, link.lastIndexOf("/")+1) + originUrl;
											}else if(originUrl.startsWith("attachment")){
												//环球网图片地址是统一存放在http://himg2.huanqiu.com/目录下的
												originUrl = "http://himg2.huanqiu.com/" + originUrl;
											}else{
												//TOM
												originUrl = CrawlerUtil.addLinkHeader(originUrl, httpHeader);
											}
										}
										imgPathSet += originUrl+",";//数据库保存实际的图片链接地址
										//TODO 格式化原IMG标签，只保留src属性
										img = "<img src=\""+originUrl+"\" alt=\"pic\" />";
										cnt = cnt.replace(inputHTML, img);

										if(log.isDebugEnabled()){
											//System.out.println("格式化后的图片地址："+originUrl);
											//TODO 图片保存至本地，备用
											//String fileName = originUrl.substring(originUrl.lastIndexOf("/")+1);
											//fileOperation.writeFile(originUrl, fileName);
										}
										
									}
								}
								content += cnt;
							}
						}
						
						if(log.isDebugEnabled()){
							//System.out.println("------------------------抓取-------------------------------");
							//System.out.println(link+" | "+title);
							
							//System.out.println("------------------------格式化文本内容-------------------------------");
							//System.out.println("内容                      :"+content);
							//System.out.println("图片存放路径  :"+imgPathSet);
							//System.out.println(" ");
						}
						/*2010-01-15：根据运营陈晓强的要求，内容首行不加<br />*/
						if(content.startsWith(RegexUtil.REGEX_BR)){
							content = content.replaceFirst(RegexUtil.REGEX_BR, "");
						}
						
						folders.add(new FolderBO(folderId, title, link, content,imgPathSet));
						
					}
					count ++;
				}
				
			}
		}catch(Exception e){
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return folders;
	}

	
}
