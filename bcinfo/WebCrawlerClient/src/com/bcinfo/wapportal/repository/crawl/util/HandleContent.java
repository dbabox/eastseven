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
 *         create time : 2009-9-9 ����10:25:26<br>
 *         ������ȡ�����Դ<br>
 */
public final class HandleContent {

	private static Logger log = Logger.getLogger(HandleContent.class);
	
	public final static int MAX_LENGTH = 500;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMhhHHmmssSSS");
	
	//����һ�����õ���Դ��Ŀ��
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
	
	//������Ŀ����Դ���Ĵ���
	@SuppressWarnings("unchecked")
	public Folder handleFolder(Folder folder) throws Exception{
		OperationDB oper = new OperationDB();
		Folder usableFolder = null;
		List resources = null;
		if(folder != null){
			String content = folder.getContent();
			String title = folder.getTitle();
			String url = folder.getUrl();
			System.out.println(folder.getId()+" | "+url+" | "+title);
			if(content == null) throw new Exception(folder.getUrl()+" folder content is null");
			//��ʽ��Content
			//content = formatContent(content);
			
			List resourceList = sliptWithImageTag(content);
			if(resourceList != null){
				resources = new ArrayList();
				for(int i=0;i<resourceList.size();i++){
					String resContent = (String)resourceList.get(i);
					Resource resource = null;
					int type = ResourceType.WORDS;//Ĭ��Ϊ��������
					if(resContent.startsWith("<img")|| resContent.startsWith("<IMG")){
						log.info("ԭʼͼƬ��ַ:"+resContent);
						//�޳���Ҫ��ͼƬ
						if(resContent.contains("entphone.gif")) continue;
						if(resContent.contains("news_xy.gif")||resContent.contains("news_sy.gif")||resContent.contains("news_hy.gif")) continue;
						
						//ͼƬ:��ȡ��������IMG��ǩ�����Խ���ת��ΪImageTag�����ڴ���
						type = ResourceType.PIC;
						String imgSrc = null;
						
						Parser parser = new Parser();
						parser.setInputHTML(resContent);
						NodeList imgList = parser.extractAllNodesThatMatch(new NodeClassFilter(ImageTag.class));
						ImageTag imageTag = (ImageTag)imgList.elementAt(0);
						imgSrc = imageTag.getImageURL();
						//�����·����ͼƬ��ַ��ȫ
						if(imgSrc.indexOf("http:") == -1 && folder.getUrl() != null){
							log.info("�����·����ͼƬ��ַ��ȫ:"+imgSrc+" | "+folder.getUrl());
							imgSrc = CrawlerUtil.extractLinkHeader(folder.getUrl()) + imgSrc;
						}
						
						if(imgSrc != null && imgSrc.startsWith("http:")){
							log.info("ͼƬ��ַ:"+imgSrc);
							//��ͼƬ���ص��������ϣ�·��������Resource������
							//ͼƬ����ͳһΪ��pa_yyyyMMhhHHmmssSSS_i.jpg ��ʽ
							String imgName = "pa_"+sdf.format(new Date())+"_"+i+".jpg";
							
							String imgPath = oper.writeFile(imgSrc, imgName);
							if(imgPath == null) continue;
							imgPath +=  imgName;
							log.info("   ����ͼƬ "+imgName+"|"+imgSrc+"|����·����"+imgPath);
							resource = new Resource(i,folder,resContent,type,imgPath);
							Thread.sleep(1000);
						}
					}else{
						//���ֵ�һ�β��ӻ��з�
						//resContent = formatContent(resContent);
						resContent = resContent.replaceFirst(RegexUtil.REGEX_BR, "");
						
						//TODO ����Դ�����һ�μ���Ϣ��Դ
						if(i == resourceList.size()-1){
							//���һ��
							resContent += PublisherUtil.addMsgOrigin(url);
						}
						resource = new Resource(i,folder,resContent,type);
					}
					if(resource != null) resources.add(resource);
				}
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
			//IMG֮�������
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
//			//ͼ�Ļ��Ŵ���
//			resource = new ArrayList();
//			int start = 0;
//			//��ͼƬ����
//			String imgRegex = "<[iI][mM][gG]\\s+[^>]+>";
//			Matcher matcher = Pattern.compile(imgRegex).matcher(content);
//			
//			/*
//			 * ��img��ǩΪ�ֶα�ʶ�������ֺ�img���
//			 * */
//			while(matcher.find()){
//				int imgBegin = matcher.start();
//				int imgEnd = matcher.end();
//				
//				//IMG֮ǰ�����֣���IMG֮ǰ����IMG,���������IMG
//				String words = content.substring(start, imgBegin).trim();
//				if(words.length()>0 && words.length() <= MAX_LENGTH ){
//					//2000����,����ֻ��<br/>��ǩ
//					if(words.length() != "<br/>".length() && !words.matches("<br/>"))
//						resource.add(words);
//				}else if(words.length() > MAX_LENGTH){
//					//����2000
//					List wordsList = splitWords(content, MAX_LENGTH);
//					if(wordsList != null) resource.addAll(wordsList);
//				}
//				//IMG
//				String imgString = content.substring(imgBegin, imgEnd);
//				resource.add(imgString);
//				//point move to next position
//				start = imgEnd;
//			}
//			//IMG֮�������
//			String last = content.substring(start);
//			if(last.length()>0 && last.length() <= MAX_LENGTH)
//				resource.add(last);
//			else{
//				//����2000
//				List wordsList = splitWords(last, MAX_LENGTH);
//				if(wordsList != null) resource.addAll(wordsList);
//			}
//		}else{
//			//ȫ������
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
			e.printStackTrace();
		}
		return resource;
	}
}
