/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.domain.Folder;
import com.bcinfo.wapportal.repository.crawl.domain.Resource;


/**
 * @author dongq
 * 
 *         create time : 2009-9-9 上午10:25:26<br>
 *         处理爬取后的资源<br>
 */
public final class HandleContent {

	private static Logger log = Logger.getLogger(HandleContent.class);
	
	public final static int MAX_LENGTH = 500;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMhhHHmmssSSS");
	
	//返回一个可用的资源栏目集
	@SuppressWarnings("unchecked")
	public List handleFolders(List folderList) {
		List folders = null;
		
		try{
			folders = new ArrayList();
			int size = folderList.size();
			for(int i=0;i<size;i++){
				Folder usableFolder = null;
				Folder folder = (Folder)folderList.get(i);
				try{
					usableFolder = handleFolder(folder);
				}catch(Exception e){
					e.printStackTrace();
					log.error(e);
					log.error(sdf.format(new Date())+" catch fail:"+folder.getUrl()+"|"+folder.getTitle());
				}
				if(usableFolder != null) folders.add(usableFolder);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("  HandleContent.handleFolders error...");
			log.error(e);
		}
		return folders;
	}
	
	//单个栏目（资源）的处理
	@SuppressWarnings("unchecked")
	public Folder handleFolder(Folder folder) throws Exception{
		OperationDB oper = new OperationDB();
		Folder usableFolder = null;
		List resources = null;
		if(folder != null){
			String content = folder.getContent();
			String title = folder.getTitle();
			String url = folder.getUrl();
			String downloadFile = folder.getDownloadFile();
			System.out.println(folder.getId()+" | "+url+" | "+title);
			if(content == null) throw new Exception(folder.getUrl()+" folder content is null");
			//格式化Content
			//content = formatContent(content);
			
			List resourceList = sliptWithImageTag(content);
			if(resourceList != null){
				resources = new ArrayList();
				//int count = 0;
				for(int i=0;i<resourceList.size();i++){
					String resContent = (String)resourceList.get(i);
					Resource resource = null;
					int type = ResourceType.WORDS;//默认为文字类型
					if(resContent.startsWith("<img")|| resContent.startsWith("<IMG")){
						log.info("原始图片地址:"+resContent);
						//剔除不要的图片
						if(resContent.contains("entphone.gif")) continue;
						if(resContent.contains("news_xy.gif")||resContent.contains("news_sy.gif")||resContent.contains("news_hy.gif")) continue;
						
						//图片:截取的是整个IMG标签，可以将其转换为ImageTag，便于处理
						type = ResourceType.PIC;
						String imgSrc = null;
						
						Parser parser = new Parser();
						parser.setInputHTML(resContent);
						NodeList imgList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
						ImageTag imageTag = (ImageTag)imgList.elementAt(0);
						imgSrc = imageTag.getImageURL();
						//将相对路径的图片地址补全
						if(imgSrc.indexOf("http:") == -1 && folder.getUrl() != null){
							log.info("将相对路径的图片地址补全:"+imgSrc+" | "+folder.getUrl());
							imgSrc = CrawlerUtil.extractLinkHeader(folder.getUrl()) + imgSrc;
						}
						
						if(imgSrc != null && imgSrc.startsWith("http:")){
							log.info("图片地址:"+imgSrc);
							//将图片下载到服务器上，路径保存在Resource对象中
							//图片名称统一为：pa_yyyyMMhhHHmmssSSS_i.jpg 格式
							String imgName = "pa_"+sdf.format(new Date())+"_"+i+".jpg";
							
							String imgPath = oper.writeFile(imgSrc, imgName);
							if(imgPath == null) continue;
							imgPath +=  imgName;
							log.info("   下载图片 "+imgName+"|"+imgSrc+"|本地路径："+imgPath);
							resource = new Resource(i,folder,resContent,type,imgPath);
							Thread.sleep(1000);
						}
						
					}else{
						//文字第一段不加换行符
						//resContent = formatContent(resContent);
						resContent = resContent.replaceFirst(RegexUtil.REGEX_BR, "");
						
						//TODO 在资源的最后一段加消息来源
						if(i == resourceList.size()-1){
							//最后一段
							resContent += PublisherUtil.addMsgOrigin(url);
						}
						resource = new Resource(i,folder,resContent,type);
					}
					
					if(resource != null) resources.add(resource);
				}
			}
			
			//下载文件:手机主题或软件
			if(downloadFile!=null&&!"".equals(downloadFile)){
				Resource resource = new Resource();
				String filePath = oper.downloadFile(downloadFile);
				if(filePath==null){
					title += "[下载包为空]";
				}
				resource.setFolder(folder);
				resource.setResourcePath(filePath);
				resource.setResourceType(ResourceType.SOFTWARE);
				resource.setResourceContent(filePath);
				resources.add(resource);
				log.info(" 下载文件:"+filePath);
				//count++;
			}
			if(resources != null){
				usableFolder = new Folder();
				usableFolder.setId(folder.getId());
				usableFolder.setContent(content);
				usableFolder.setTitle(title);
				usableFolder.setUrl(url);
				usableFolder.setResources(resources);
				usableFolder.setResFileName(folder.getResFileName());
				usableFolder.setOperation(folder.getOperation());
				usableFolder.setDownloadFile(folder.getDownloadFile());
				usableFolder.setSendType(folder.getSendType());
			}
		}
		return usableFolder;
	}
	
	public String formatContent(String targetCnt) {
		String content = null;
		try{
			String[] cnt = targetCnt.split(RegexUtil.REGEX_BR);
			content = "";
			for(int i=0; i<cnt.length; i++){
				String cntSplit = cnt[i].trim();
				if(i == 0) content += cntSplit;
				else content += RegexUtil.REGEX_BR + cntSplit;
			}
		}catch(Exception e){
			content = targetCnt;
		}
		return content;
	}
	
	public List<String> sliptWithImageTag(String content) throws Exception {
		List<String> resource = null;
		
		List<String> list = null;
		if(content.contains("<img")||content.contains("<IMG")){
			int start = 0;
			String imgRegex = "<[iI][mM][gG]\\s+[^>]+>";
			Matcher matcher = Pattern.compile(imgRegex).matcher(content);
			list = new ArrayList<String>();
			while(matcher.find()){
				int imgBegin = matcher.start();
				int imgEnd = matcher.end();
				String words = content.substring(start, imgBegin).trim();
				if(words!=null&&words.length()>0) list.add(words);
				String imgString = content.substring(imgBegin, imgEnd);
				if(imgString!=null&&imgString.length()>0) list.add(imgString);
				start = imgEnd;
			}
			//IMG之后的文字
			String last = content.substring(start);
			list.add(last);
		}else{
			list = new ArrayList<String>();
			list.add(content);
		}
		
		if(list!=null && !list.isEmpty()){
			resource = new ArrayList<String>();
			for(String cnt : list){
				if(cnt.startsWith("<img")||cnt.startsWith("<IMG"))
					resource.add(cnt);
				else{
					List<String> tmp = splitWords(cnt, MAX_LENGTH);
					if(tmp != null && !tmp.isEmpty()) resource.addAll(tmp);
				}
			}
		}
		
		return resource;
	}
	
//	@SuppressWarnings("unchecked")
//	public List sliptWithImageTag(String content) throws Exception{
//		List resource = null;
//		if(content == null) throw new Exception("resource content is null");
//		if(content.indexOf("<img ") != -1 || content.indexOf("<IMG") != -1){
//			//图文混排处理
//			resource = new ArrayList();
//			int start = 0;
//			//有图片链接
//			String imgRegex = "<[iI][mM][gG]\\s+[^>]+>";
//			Matcher matcher = Pattern.compile(imgRegex).matcher(content);
//			
//			/*
//			 * 以img标签为分段标识，将文字和img拆分
//			 * */
//			while(matcher.find()){
//				int imgBegin = matcher.start();
//				int imgEnd = matcher.end();
//				
//				//IMG之前的文字，若IMG之前还是IMG,即连续多个IMG
//				String words = content.substring(start, imgBegin).trim();
//				if(words.length()>0 && words.length() <= MAX_LENGTH ){
//					//2000字内,避免只是<br/>标签
//					if(words.length() != "<br/>".length() && !words.matches("<br/>"))
//						resource.add(words);
//				}else if(words.length() > MAX_LENGTH){
//					//超过2000
//					List wordsList = splitWords(content, MAX_LENGTH);
//					if(wordsList != null) resource.addAll(wordsList);
//				}
//				//IMG
//				String imgString = content.substring(imgBegin, imgEnd);
//				resource.add(imgString);
//				//point move to next position
//				start = imgEnd;
//			}
//			//IMG之后的文字
//			String last = content.substring(start);
//			if(last.length()>0 && last.length() <= MAX_LENGTH)
//				resource.add(last);
//			else{
//				//超过2000
//				List wordsList = splitWords(last, MAX_LENGTH);
//				if(wordsList != null) resource.addAll(wordsList);
//			}
//		}else{
//			//全是文字
//			resource = new ArrayList();
//			if(content.length() > MAX_LENGTH){
//				List wordsList = splitWords(content, MAX_LENGTH);
//				if(wordsList != null) resource.addAll(wordsList);
//			}else
//				resource.add(content);
//				
//		}
//		return resource;
//	}
	
	//将文字按指定数量分段
	public List<String> splitWords(String content, int limit) {
		List<String> resource = null;
		try{
			if(content != null){
				resource = new ArrayList<String>();
				int length = content.length();
				int page = (length <= limit) ? 1 : new BigDecimal((double)length/limit).setScale(0, BigDecimal.ROUND_UP).intValue();
				for(int i=0;i<page;i++){
					int beginIndex = i*limit;
					int endIndex = (i+1)*limit;
					if(i == page-1){//处理最后一页
						endIndex = length - beginIndex + beginIndex;
					}
					String segment = content.substring(beginIndex, endIndex).trim();
	
					//处理分段截取时，将<br/>截断的情况
					//若段尾有残缺的<br/>标签，则将其去掉
					if(segment.endsWith("<")||segment.endsWith("<b")||segment.endsWith("<br")||segment.endsWith("<br/")||segment.endsWith("<br/>"))
						segment = segment.substring(0,segment.lastIndexOf("<"));
					//若段首有残缺的<br/>标签，则将其补全
					if(segment.startsWith(">")||segment.startsWith("/>")||segment.startsWith("r/>")||segment.startsWith("br/>"))
						segment = "<br/>" + segment.substring(segment.indexOf(">")+1);
					
					resource.add(segment);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resource;
	}
}
