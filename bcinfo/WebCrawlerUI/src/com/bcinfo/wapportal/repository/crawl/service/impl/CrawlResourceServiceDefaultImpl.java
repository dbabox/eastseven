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

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.bcinfo.wapportal.repository.crawl.dao.ChannelMappingDao;
import com.bcinfo.wapportal.repository.crawl.dao.CrawlResourceDao;
import com.bcinfo.wapportal.repository.crawl.domain.ChannelMapping;
import com.bcinfo.wapportal.repository.crawl.domain.CrawlResource;
import com.bcinfo.wapportal.repository.crawl.file.FtpUpload;
import com.bcinfo.wapportal.repository.crawl.file.ZipCompressor;
import com.bcinfo.wapportal.repository.crawl.service.CrawlResourceService;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 ����04:44:11
 */
public class CrawlResourceServiceDefaultImpl implements CrawlResourceService {

	private CrawlResourceDao crawlResourceDao;
	private ChannelMappingDao channelMappingDao;
	
	
	public CrawlResourceServiceDefaultImpl() {
		this.crawlResourceDao = new CrawlResourceDao();
		this.channelMappingDao = new ChannelMappingDao();
	}
	
	@Override
	public Boolean sendResource(String channelId, String[] resourceIds) {
		boolean bln = false;
		Long resId;
		String localChannelId = "";
		CrawlResource resource;
		try{
			
			//ȡ���ձ�����
			List<ChannelMapping> mappingList = this.channelMappingDao.getChannelMappingList(Long.parseLong(channelId));
			for(ChannelMapping map : mappingList){
				System.out.println(map);
				//��ñ�����ĿID
				localChannelId = map.getLocalChannelId();
				//ȡ����Դ
				for(int i=0;i<resourceIds.length;i++){
					resId = Long.parseLong(resourceIds[i]);
					resource = this.crawlResourceDao.getCrawlResourceDetail(resId);
					if("0".equals(resource.getStatus())) continue;
					System.out.println(resource);
					//������Դ���ļ�ZIP
					generateResourceFile(map, resource);
				}
			}
			
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bln;
	}

	//��ʱ������������
	String filePath = "C:\\Download\\";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	//������Դ��
	public Boolean generateResourceFile(ChannelMapping mapping, CrawlResource resource){
		boolean bln = false;
		try{
			//������Դ����XML�ļ�
			bln = generateXMLFile(filePath, resource.getContent(), resource.getTitle(), mapping.getLocalChannelId(), resource.getImgPathSet(), resource.getCreateTime());
			System.out.println("������Դ����XML�ļ�:"+bln);
			//�����ɵ�XML�ļ������а�����ͼƬһͬ�����ZIP�ļ�
			if(bln){
				bln = packToZip(filePath, resource.getTitle(), resource.getImgPathSet());
				System.out.println("�����ɵ�XML�ļ������а�����ͼƬһͬ�����ZIP�ļ�:"+bln);
				if(bln){
					//TODO ��ʱ�����ɵ��ļ���FTP���ط���������
					FtpUpload ftp = new FtpUpload();
					boolean isOk = ftp.uploadFile("C:/Download/"+resource.getTitle()+".zip", "/usr/local/dongq/remotedir/"+sdf.format(new Date())+"_"+mapping.getLocalChannelId()+".zip");
					System.out.println("��ʱ�����ɵ��ļ���FTP���ط���������:"+isOk);
				}
			}else{
				System.out.println("ʧ����...");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bln;
	}
	
	private Boolean generateXMLFile(String filePath,String content, String title, String localChannelId, String imgPath, String date){
		boolean bln = false;
		try{
			Element root = new Element("resource");
			Document document = new Document(root);
			
			Element eLocalChannelId = new Element("localChannelId");
			eLocalChannelId.setText(localChannelId);
			
			Element eTitle = new Element("title");
			eTitle.setText(title);
			
			Element eCreateTime = new Element("createTime");
			eCreateTime.setText(date);
			
			Element eContent = new Element("content");
			eContent.addContent(new CDATA(content));
			
			Element eImgPath = new Element("imgPath");
			if(imgPath!=null){
				String[] imgStr = imgPath.split(",");
				Element ePath = null;
				for(int i=0;i<imgStr.length;i++){
					ePath = new Element("path");
					ePath.setText(imgStr[i]);
					eImgPath.addContent(ePath);
				}
			}
			
			root.addContent(eLocalChannelId);
			root.addContent(eTitle);
			root.addContent(eCreateTime);
			root.addContent(eContent);
			root.addContent(eImgPath);
			
			XMLOutputter outputter = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setEncoding("GBK");
			outputter.setFormat(format);
			outputter.output(document, new FileWriter(filePath+title+".xml"));
			
			bln = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return bln;
	}
	
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
