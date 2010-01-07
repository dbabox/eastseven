/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao;
import com.bcinfo.wapportal.repository.crawl.dao.CrawlResourceDao;
import com.bcinfo.wapportal.repository.crawl.domain.ChannelMapping;
import com.bcinfo.wapportal.repository.crawl.domain.CrawlResource;
import com.bcinfo.wapportal.repository.crawl.file.ConfigPropertyUtil;
import com.bcinfo.wapportal.repository.crawl.file.FtpUpload;
import com.bcinfo.wapportal.repository.crawl.file.ZipCompressor;
import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 下午04:44:11
 */
public class CrawlResourceServiceDefaultImpl implements CrawlResourceService {

	private static final Logger log = Logger.getLogger(CrawlResourceServiceDefaultImpl.class);
	
	public static int INSERT = 1;
	public static int UPDATE = 2;
	
	private CrawlResourceDao crawlResourceDao;
	private ChannelMappingDao channelMappingDao;
	private Properties property;
	
	String localFileDir = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
	Random random = new Random(1000L);
	
	public CrawlResourceServiceDefaultImpl() {
		this.crawlResourceDao = new CrawlResourceDao();
		this.channelMappingDao = new ChannelMappingDao();
		this.property = ConfigPropertyUtil.property;
		String os = null;
		os = System.getenv("OS");
		if(os!=null&&os.toLowerCase().contains("windows")){
			localFileDir = this.property.getProperty("resource.dir.windows");
		}else{
			localFileDir = this.property.getProperty("resource.dir.linux");
		}
	}
	
	//自动发送彩票
	@Override
	public Boolean sendResourceAuto() {
		boolean bln = false;
		CrawlResource resource;
		try{
			List<Map<String, String>> list = this.crawlResourceDao.getAutoSendCrawlResources();
			if(list != null && !list.isEmpty()){
				ChannelMapping mapping = null;
				int operation = INSERT;//默认值
				for(Map<String, String> map : list){
					resource = this.crawlResourceDao.getCrawlResourceDetail(Long.parseLong(map.get("resId")));
					mapping = new ChannelMapping();
					mapping.setLocalChannelId(map.get("localChannelId"));
					mapping.setLocalCode(map.get("localCode"));
					operation = Integer.parseInt(map.get("operation"));
					//TODO 彩票无审核直接发布代码10
					if(operation==1){
						operation = 10;
					}
					generateResourceFile(mapping, resource, operation);
				}
			}
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}
		
		return bln;
	}
	
	@Override
	public Boolean sendResource(Long userId, String channelId, String[] resourceIds) {
		boolean bln = false;
		Long resId;
		CrawlResource resource;
		try{
			
			//取对照表数据
			List<ChannelMapping> mappingList = this.channelMappingDao.getChannelMappingList(userId, Long.parseLong(channelId));
			for(ChannelMapping map : mappingList){
				System.out.println(map);
				//取得资源
				for(int i=0;i<resourceIds.length;i++){
					resId = Long.parseLong(resourceIds[i]);
					resource = this.crawlResourceDao.getCrawlResourceDetail(resId);
					if("0".equals(resource.getStatus())) continue;
					System.out.println(resource);
					//生成资源包文件XML,默认操作为INSERT
					generateResourceFile(map, resource, INSERT);
				}
			}
			
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bln;
	}

	//生成资源包
	public Boolean generateResourceFile(ChannelMapping mapping, CrawlResource resource, int operation){
		boolean bln = false;
		try{
			log.debug("生成资源包:"+resource);
			//生成资源内容XML文件
			String localFileName = generateXMLFile(localFileDir, resource.getContent(), resource.getTitle(), resource.getLink(), mapping.getLocalChannelId(), resource.getImgPathSet(), resource.getFilePathSet(), resource.getCreateTime(), operation);
			System.out.println("生成资源内容XML文件:"+localFileName);
			
			//TODO 暂时将生成的文件包FTP到地方服务器上
			if(localFileName!=null){
				FtpUpload ftp = new FtpUpload(mapping.getLocalCode());
				String local = localFileDir+localFileName;
				String remote = ftp.getRemoteDir()+localFileName;
				boolean isOk = ftp.uploadFile(local, remote);
				System.out.println(" FTP: "+local+" 到 "+remote);
				if(isOk){
					
					//删除生成的XML文件
					File file = new File(local);
					if(file.exists()){
						boolean b = file.delete();
						System.out.println("  FTP完成，删除生成的XML文件["+local+"] = "+b);
						if(!b){
							//删除失败，等待3秒后再删
							Thread.sleep(3*1000L);
							b = file.delete();
							System.out.println("  FTP完成，删除失败，等待3秒后再删["+local+"] = "+b);
						}
					}
				}
				System.out.println("暂时将生成的文件包FTP到地方服务器上:"+isOk);
			}else{
				System.out.println("失败了...");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}
		return bln;
	}
	
	/**
	 * 生产资源文件
	 * @param filePath
	 * @param content
	 * @param title
	 * @param link
	 * @param localChannelId
	 * @param imgPathSet
	 * @param filePathSet
	 * @param date
	 * @param operation
	 * @return
	 */
	private String generateXMLFile(String filePath,String content, String title, String link, String localChannelId, String imgPathSet, String filePathSet, String date, int operation){
		String fileName = null;
		try{
			
			Element root = new Element("resource");
			Document document = new Document(root);
			
			Element eLocalChannelId = new Element("localChannelId");
			eLocalChannelId.setText(localChannelId);
			
			Element eTitle = new Element("title");
			eTitle.setText(title);
			
			Element eLink = new Element("link");
			eLink.addContent(new CDATA(link));
			
			Element eCreateTime = new Element("createTime");
			eCreateTime.setText(date);
			
			Element eContent = new Element("content");
			eContent.addContent(new CDATA(content));
			
			Element eImgPath = new Element("imgPath");
			if(imgPathSet!=null){
				String[] imgStr = imgPathSet.split(",");
				Element ePath = null;
				for(int i=0;i<imgStr.length;i++){
					ePath = new Element("path");
					ePath.setText(imgStr[i]);
					eImgPath.addContent(ePath);
				}
			}
			
			Element eFilePath = new Element("filePath");
			if(filePathSet!=null&&!"".equals(filePathSet)){
				eFilePath.addContent(filePathSet);
			}
			
			Element eOperation = new Element("operation");
			eOperation.setText(String.valueOf(operation));
			
			root.addContent(eLocalChannelId);
			root.addContent(eTitle);
			root.addContent(eLink);
			root.addContent(eCreateTime);
			root.addContent(eContent);
			root.addContent(eImgPath);
			root.addContent(eFilePath);
			root.addContent(eOperation);
			
			XMLOutputter outputter = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setEncoding("GBK");
			outputter.setFormat(format);
			fileName = sdf.format(new Date())+"_"+localChannelId+"_"+Math.abs(random.nextLong())+".xml";
			outputter.output(document, new FileWriter(filePath+fileName));
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			fileName = null;
		}
		return fileName;
	}
	
	//本地测试时使用
	@SuppressWarnings("unused")
	private Boolean packToZip(String filePath, String title, String imgPath){
		boolean bln = false;
		try{
			List<File> list = new ArrayList<File>();
			ZipCompressor zipTool = new ZipCompressor(filePath+title+".zip");
			File file = null;
			if(imgPath!=null){
				String[] imgStr = imgPath.split(",");
				for(int i=0;i<imgStr.length;i++){
					String src = "E:/usr/local/jboss-3.2.7/server/default/deploy/spcpnew.war" + imgStr[i];
					file = new File(src);
					list.add(file);
				}
			}
			list.add(new File(filePath+title+".xml"));
			bln = zipTool.compress(list);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return bln;
	}
}
