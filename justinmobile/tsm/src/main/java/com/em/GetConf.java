package com.em;
/*
 * author: 		guizy
 * Date:		2008-06-24
 * Last Modify:	2008-06-24
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetConf {
    private Properties propertie;
    private FileInputStream fis;
    private FileOutputStream fos;
    private static Log logger = LogFactory.getLog("GetConf.class");

    /**
     * ��ʼ��Configuration��
     */
    public GetConf()
    {
        propertie = new Properties();
    }

    /**
     * ��ʼ��Configuration��
     * @param filePath Ҫ��ȡ�������ļ���·��+����
     */
    public GetConf(String filePath)
    {
        propertie = new Properties();
        try{
        	//System.out.println("filePath="+filePath);
        	//File   directory =   new   File(".");
        	//File   newPath   =   new   File(directory.getCanonicalPath()+"NewFolder");
        	//System.out.println("Current path="+newPath.toString());

            fis = new FileInputStream(filePath);
            propertie.load(fis);
            fis.close();
        } catch (FileNotFoundException ex){
        	logger.error("��ȡ�����ļ�--->ʧ�ܣ�- ԭ���ļ�·����������ļ�������");
        	logger.error("������Ϣ��"+ex.getMessage());
        } catch (IOException ex){
        	logger.error("װ���ļ�--->ʧ��!");
        	logger.error("������Ϣ��"+ex.getMessage());
        }
    }//end ReadConfigInfo(...)

    /**
     * ���غ������õ�key��ֵ
     * @param key ȡ����ֵ�ļ�
     * @return key��ֵ
     */
    public String getValue(String key)
    {
        if(propertie.containsKey(key)){
            String value = propertie.getProperty(key);//�õ�ĳһ���Ե�ֵ
            return value;
        }
        else
            return "";
    }


    /** *//**
     * ���غ������õ�key��ֵ
     * @param fileName properties�ļ���·��+�ļ���
     * @param key ȡ����ֵ�ļ�
     * @return key��ֵ
     */
    public String getValue(String fileName, String key)
    {
        try{
            String value = "";
            fis = new FileInputStream(fileName);
            propertie.load(fis);
            fis.close();
            if(propertie.containsKey(key)){
                value = propertie.getProperty(key);
                return value;
            }else
                return value;
        } catch (FileNotFoundException e){
        	logger.error("δ�ҵ��ļ������ļ������ڣ�");
            return "";
        } catch (IOException e){
        	logger.error("�ļ��쳣��");
            return "";
        } catch (Exception ex){
        	logger.error("�ļ��쳣��");
            return "";
        }
    }

    /**
     * ���properties�ļ������е�key����ֵ
     */
    public void clear()
    {
        propertie.clear();
    }//end clear();
    /**
     * �ı�����һ��key��ֵ����key������properties�ļ���ʱ��key��ֵ��value�����棬
     * ��key������ʱ����key��ֵ��value
     * @param key Ҫ����ļ�
     * @param value Ҫ�����ֵ
     */
    public void setValue(String key, String value)
    {
        propertie.setProperty(key, value);
    }

    /**
     * �����ĺ���ļ����ݴ���ָ�����ļ��У����ļ��������Ȳ����ڡ�
     * @param fileName �ļ�·��+�ļ�����
     * @param description �Ը��ļ�������
     */
    public void saveFile(String fileName, String description)
    {
        try{
        	fos=new FileOutputStream(fileName);
        	propertie.store(fos, description);
        	fos.close();
        } catch (FileNotFoundException e){
        	logger.error(e.getMessage());
        } catch (IOException ioe){
        	logger.error(ioe.getMessage());
        }
    }
/*
    public static void main(String[] args)
    {
    	GetConf rc=new GetConf("config/emconfig.properties");
        String host = rc.getValue("hsmip");
        String port = rc.getValue("hsmport");

        System.out.println("hsmip = " + host);
        System.out.println("hsmport = " + port);
    }//end main()
 */
}
