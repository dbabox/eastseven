/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author dongq
 * 
 *         create time : 2009-9-16 ����10:35:29<br>
 *         �����Ϣ��Դ����Ӧ�Ĵ���
 */
public final class PublisherUtil {

	private static Logger log = Logger.getLogger(PublisherUtil.class);
	
	/**
	 * ��������
	 */
	public final static String SINA_SPORTS = "����Դ���Ĵ�������-����������";
	public final static String SOHU_SPORTS = "����Դ���Ĵ�������-�Ѻ�������";
	public final static String QQ_SPORTS = "����Դ���Ĵ�������-��Ѷ������";
	public final static String WANGYI_SPORTS = "����Դ���Ĵ�������-����������";
	public final static String IFENG = "����Դ���Ĵ�������-�������";
	
	/**
	 * �Ѻ�����
	 */
	public final static String SOHU_ENT = "����Դ���Ĵ�������-�Ѻ����֣�";
	
	/**
	 * TOM����
	 */
	public final static String TOM_ENT = "����Դ���Ĵ�������-TOM���֣�";
	
	/**
	 * ��Ѷ����
	 */
	public final static String QQ_ENT = "����Դ���Ĵ�������-��Ѷ���֣�";
	
	/**
	 * �»���
	 */
	public final static String XINHUA_NEWS = "����Դ���Ĵ�������-�»�����";
	
	/**
	 * ������ַ��ȡ����Ϣ��Դ<br>
	 * ��Բ�ͬʡ�У�����Դ�<br>
	 * @param url
	 * @return����Դ���Ĵ�������-XXXX��
	 */
	public static String addMsgOrigin(String url){
		String msgOrigin = "  ";
		
		Properties property = new ConfigPropertyUtil().getConfigProperty();
		if(property!=null){
			String area = property.getProperty("area");
			if(area!=null&&!"".equals(area)){
				if("028".equals(area)){
					try{
						url = url.toLowerCase();
						if(url.indexOf("sports")!=-1){
							//����Ӫ��Ҫ��ȫ��ͳһ����������
							msgOrigin += SINA_SPORTS;
//							if(url.indexOf("sina")!=-1) msgOrigin += SINA_SPORTS;
//							if(url.indexOf("163")!=-1) msgOrigin += WANGYI_SPORTS;
//							if(url.indexOf("qq")!=-1) msgOrigin += QQ_SPORTS;
//							if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_SPORTS;
//							if(url.indexOf("ifeng")!=-1) msgOrigin += IFENG;
						}
						if(url.indexOf("ent")!=-1){
							if(url.indexOf("qq")!=-1) msgOrigin += QQ_ENT;
							if(url.indexOf("tom")!=-1) msgOrigin += TOM_ENT;
							if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_ENT;
						}
						if(url.indexOf("xinhuanet")!=-1 || url.indexOf("www.news.cn")!=-1){
							msgOrigin += XINHUA_NEWS;
						}
					}catch(Exception e){
						return "����Դ���Ĵ���������";
					}
				}else if("0791".equals(area)){
					return "����Դ���»�����";
				}
			}else{
				//����Ĭ��ֵ
			}
		}
		
		return msgOrigin;
	}
	
}
