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

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceBO;
import com.bcinfo.wapportal.repository.crawl.domain.bo.ResourceType;
import com.bcinfo.wapportal.repository.crawl.file.FileOperation;

/**
 * @author dongq
 * 
 *         create time : 2009-9-9 上午10:25:26<br>
 *         处理爬取后的资源<br>
 */
public final class HandleContent {

	private static Logger log = Logger.getLogger(HandleContent.class);
	
	private final static int MAX_LENGTH_DEFAULT = 500;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMhhHHmmss");
	
	private int maxLength;
	private FileOperation fileOperation;
	
	public HandleContent() {
		this.maxLength = MAX_LENGTH_DEFAULT;
		this.fileOperation = new FileOperation();
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	//返回一个可用的资源栏目集
	public List<FolderBO> handleFolders(List<FolderBO> folderList) {
		List<FolderBO> folders = null;
		
		try{
			folders = new ArrayList<FolderBO>();
			int size = folderList.size();
			for(int i=0;i<size;i++){
				FolderBO usableFolder = null;
				FolderBO FolderBO = (FolderBO)folderList.get(i);
				try{
					usableFolder = handleFolder(FolderBO);
				}catch(Exception e){
				}
				if(usableFolder != null) folders.add(usableFolder);
			}
		}catch(Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			else log.info("内容处理失败");
		}
		return folders;
	}
	
	//单个栏目（资源）的处理
	public FolderBO handleFolder(FolderBO folder) throws Exception{
		FolderBO usableFolder = null;
		List<ResourceBO> resources = null;
		if(folder != null){
			String content = folder.getContent();
			String title = folder.getTitle();
			String url = folder.getLink();
			if(content == null){
				
				return null;
			}
			List<String> resourceList = sliptWithImageTag(content);
			if(resourceList != null){
				resources = new ArrayList<ResourceBO>();
				for(int i=0;i<resourceList.size();i++){
					String resContent = (String)resourceList.get(i);
					ResourceBO resource = null;
					int type = ResourceType.WORDS;//默认为文字类型
					if(resContent.indexOf("<img ") != -1 || resContent.indexOf("<IMG") != -1){
						//图片:截取的是整个IMG标签，可以将其转换为ImageTag，便于处理
						type = ResourceType.PIC;
						String imgSrc = null;
						
						Parser parser = new Parser();
						parser.setInputHTML(resContent);
						NodeList imgList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
						ImageTag imageTag = (ImageTag)imgList.elementAt(0);
						imgSrc = imageTag.getImageURL();
						//将相对路径的图片地址补全
						if(imgSrc.indexOf("http://") == -1) imgSrc = CrawlerUtil.extractLinkHeader(folder.getLink()) + imgSrc;
						
						if(imgSrc != null){ 
							//将图片下载到服务器上，路径保存在Resource对象中
							String imgName = imgSrc.substring(imgSrc.lastIndexOf("/")+1).toLowerCase();
							if(imgName.lastIndexOf(".") == -1) imgName = sdf.format(new Date()) + i + ".jpg";
							String imgPath = fileOperation.writeFile(imgSrc, imgName) + imgName;
							resource = new ResourceBO(i,folder,resContent,imgPath,type);
							Thread.sleep(1000);
						}
					}else{
						//TODO 在资源的最后一段加消息来源
						if(i == resourceList.size()-1){
							//最后一段
							resContent += PublisherUtil.addMsgOrigin(url);
						}
						resource = new ResourceBO(i,folder,resContent,type);
					}
					if(resource != null) resources.add(resource);
				}
			}
			if(resources != null){
				usableFolder = new FolderBO();
				usableFolder.setFolderId(folder.getFolderId());
				usableFolder.setContent(content);
				usableFolder.setTitle(title);
				usableFolder.setLink(url);
				usableFolder.setResources(resources);
			}
		}
		return usableFolder;
	}
	
	public List<String> sliptWithImageTag(String content) {
		List<String> resource = null;
		if(content == null || "".equals(content) || "null".equals(content)){
			log.info("待分段的内容是空的");
			return null;
		}
		try{
			if(content.indexOf("<img ") != -1 || content.indexOf("<IMG") != -1){
				//图文混排处理
				resource = new ArrayList<String>();
				int start = 0;
				//有图片链接
				String imgRegex = "<[iI][mM][gG]\\s+[^>]+>";
				Matcher matcher = Pattern.compile(imgRegex).matcher(content);
				
				/*
				 * 以img标签为分段标识，将文字和img拆分
				 * */
				while(matcher.find()){
					int imgBegin = matcher.start();
					int imgEnd = matcher.end();
					
					//IMG之前的文字，若IMG之前还是IMG,即连续多个IMG
					String words = content.substring(start, imgBegin).trim();
					if(words.length()>0 && words.length() <= maxLength ){
						//2000字内,避免只是<br/>标签
						if(words.length() != "<br/>".length() && !words.matches("<br/>"))
							resource.add(words);
					}else if(words.length() > maxLength){
						//超过2000
						List<String> wordsList = splitWords(content, maxLength);
						if(wordsList != null) resource.addAll(wordsList);
					}
					//IMG
					String imgString = content.substring(imgBegin, imgEnd);
					resource.add(imgString);
					//point move to next position
					start = imgEnd;
				}
				//IMG之后的文字
				String last = content.substring(start);
				if(last.length()>0 && last.length() <= maxLength)
					resource.add(last);
				else{
					//超过2000
					List<String> wordsList = splitWords(last, maxLength);
					if(wordsList != null) resource.addAll(wordsList);
				}
			}else{
				//全是文字
				resource = new ArrayList<String>();
				if(content.length() > maxLength){
					List<String> wordsList = splitWords(content, maxLength);
					if(wordsList != null) resource.addAll(wordsList);
				}else
					resource.add(content);
					
			}
		}catch(Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			else log.info("将内容分段失败");
		}
		return resource;
	}
	
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
			if(log.isDebugEnabled()) log.debug(e);
			else log.info("将文字按指定数量分段失败");
		}
		return resource;
	}
	
	public String formatContent(String targetCnt) {
		String content = null;
		try{
			String[] cnt = targetCnt.split(RegexUtil.REGEX_BR);
			content = "";
			for(int i=0; i<cnt.length; i++){
				String cntSplit = cnt[i].trim();
				if(cntSplit != "" || !"".equals(cntSplit) || !"null".equals(cntSplit)){
					if(!cntSplit.contains("<img") || !cntSplit.contains("<IMG")){
						content += RegexUtil.REGEX_BR + cntSplit;
					}else{
						content += cntSplit;
					}
				}
			}
		}catch(Exception e){
			content = targetCnt;
		}
		return content;
	}
}
