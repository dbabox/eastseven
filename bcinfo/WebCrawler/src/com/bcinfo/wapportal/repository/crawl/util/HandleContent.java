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
 *         create time : 2009-9-9 ����10:25:26<br>
 *         ������ȡ�����Դ<br>
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
	
	//����һ�����õ���Դ��Ŀ��
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
			else log.info("���ݴ���ʧ��");
		}
		return folders;
	}
	
	//������Ŀ����Դ���Ĵ���
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
					int type = ResourceType.WORDS;//Ĭ��Ϊ��������
					if(resContent.indexOf("<img ") != -1 || resContent.indexOf("<IMG") != -1){
						//ͼƬ:��ȡ��������IMG��ǩ�����Խ���ת��ΪImageTag�����ڴ���
						type = ResourceType.PIC;
						String imgSrc = null;
						
						Parser parser = new Parser();
						parser.setInputHTML(resContent);
						NodeList imgList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
						ImageTag imageTag = (ImageTag)imgList.elementAt(0);
						imgSrc = imageTag.getImageURL();
						//�����·����ͼƬ��ַ��ȫ
						if(imgSrc.indexOf("http://") == -1) imgSrc = CrawlerUtil.extractLinkHeader(folder.getLink()) + imgSrc;
						
						if(imgSrc != null){ 
							//��ͼƬ���ص��������ϣ�·��������Resource������
							String imgName = imgSrc.substring(imgSrc.lastIndexOf("/")+1).toLowerCase();
							if(imgName.lastIndexOf(".") == -1) imgName = sdf.format(new Date()) + i + ".jpg";
							String imgPath = fileOperation.writeFile(imgSrc, imgName) + imgName;
							resource = new ResourceBO(i,folder,resContent,imgPath,type);
							Thread.sleep(1000);
						}
					}else{
						//TODO ����Դ�����һ�μ���Ϣ��Դ
						if(i == resourceList.size()-1){
							//���һ��
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
			log.info("���ֶε������ǿյ�");
			return null;
		}
		try{
			if(content.indexOf("<img ") != -1 || content.indexOf("<IMG") != -1){
				//ͼ�Ļ��Ŵ���
				resource = new ArrayList<String>();
				int start = 0;
				//��ͼƬ����
				String imgRegex = "<[iI][mM][gG]\\s+[^>]+>";
				Matcher matcher = Pattern.compile(imgRegex).matcher(content);
				
				/*
				 * ��img��ǩΪ�ֶα�ʶ�������ֺ�img���
				 * */
				while(matcher.find()){
					int imgBegin = matcher.start();
					int imgEnd = matcher.end();
					
					//IMG֮ǰ�����֣���IMG֮ǰ����IMG,���������IMG
					String words = content.substring(start, imgBegin).trim();
					if(words.length()>0 && words.length() <= maxLength ){
						//2000����,����ֻ��<br/>��ǩ
						if(words.length() != "<br/>".length() && !words.matches("<br/>"))
							resource.add(words);
					}else if(words.length() > maxLength){
						//����2000
						List<String> wordsList = splitWords(content, maxLength);
						if(wordsList != null) resource.addAll(wordsList);
					}
					//IMG
					String imgString = content.substring(imgBegin, imgEnd);
					resource.add(imgString);
					//point move to next position
					start = imgEnd;
				}
				//IMG֮�������
				String last = content.substring(start);
				if(last.length()>0 && last.length() <= maxLength)
					resource.add(last);
				else{
					//����2000
					List<String> wordsList = splitWords(last, maxLength);
					if(wordsList != null) resource.addAll(wordsList);
				}
			}else{
				//ȫ������
				resource = new ArrayList<String>();
				if(content.length() > maxLength){
					List<String> wordsList = splitWords(content, maxLength);
					if(wordsList != null) resource.addAll(wordsList);
				}else
					resource.add(content);
					
			}
		}catch(Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			else log.info("�����ݷֶ�ʧ��");
		}
		return resource;
	}
	
	//�����ְ�ָ�������ֶ�
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
					if(i == page-1){//�������һҳ
						endIndex = length - beginIndex + beginIndex;
					}
					String segment = content.substring(beginIndex, endIndex).trim();
	
					//����ֶν�ȡʱ����<br/>�ضϵ����
					//����β�в�ȱ��<br/>��ǩ������ȥ��
					if(segment.endsWith("<")||segment.endsWith("<b")||segment.endsWith("<br")||segment.endsWith("<br/")||segment.endsWith("<br/>"))
						segment = segment.substring(0,segment.lastIndexOf("<"));
					//�������в�ȱ��<br/>��ǩ�����䲹ȫ
					if(segment.startsWith(">")||segment.startsWith("/>")||segment.startsWith("r/>")||segment.startsWith("br/>"))
						segment = "<br/>" + segment.substring(segment.indexOf(">")+1);
					
					resource.add(segment);
				}
			}
		}catch(Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			else log.info("�����ְ�ָ�������ֶ�ʧ��");
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
