/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

/**
 * @author dongq
 * 
 *         create time : 2009-9-16 ����10:35:29<br>
 *         �����Ϣ��Դ����Ӧ�Ĵ���
 */
public final class PublisherUtil {

	/**
	 * ��������
	 */
	public final static String SINA_SPORTS = "����Դ���Ĵ�������-����������";
	
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
	 * ������ַ��ȡ����Ϣ��Դ
	 * @param url
	 * @return����Դ���Ĵ�������-XXXX��
	 */
	public static String addMsgOrigin(String url){
		String msgOrigin = "  ";
		
		try{
			url = url.toLowerCase();
			if(url.indexOf("sports")!=-1){
				if(url.indexOf("sina")!=-1) msgOrigin += SINA_SPORTS;
			}
			if(url.indexOf("ent")!=-1){
				if(url.indexOf("qq")!=-1) msgOrigin += QQ_ENT;
				if(url.indexOf("tom")!=-1) msgOrigin += TOM_ENT;
				if(url.indexOf("sohu")!=-1) msgOrigin += SOHU_ENT;
			}
			
		}catch(Exception e){
			return "����Դ���Ĵ���������";
		}
		
		return msgOrigin;
	}
}
