/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.file;

import java.io.BufferedInputStream;   
import java.io.File;   
import java.io.FileInputStream;   
import java.io.FileOutputStream;   
import java.util.List;
import java.util.zip.CRC32;   
import java.util.zip.CheckedOutputStream;   
  
import org.apache.tools.zip.ZipEntry;   
import org.apache.tools.zip.ZipOutputStream;   
/**
 * @author dongq
 * 
 *         create time : 2009-10-20 下午06:11:54
 */
public class ZipCompressor {

	public static void main(String[] args) {

	}

	static final int BUFFER = 8192;   
	  
    private File zipFile;   
  
    public ZipCompressor(String pathName) {   
        zipFile = new File(pathName);   
    }
    
    public void compress(String srcPathName) {   
        File file = new File(srcPathName);   
        if (!file.exists())   
            throw new RuntimeException(srcPathName + "不存在！");   
        try {   
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,   
                    new CRC32());   
            ZipOutputStream out = new ZipOutputStream(cos);   
            String basedir = "";   
            compress(file, out, basedir);   
            out.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }
    
    public boolean compress(List<File> list) {   
    	boolean bln = false;
        try {   
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());   
            ZipOutputStream out = new ZipOutputStream(cos);
            for(File file : list){
	            String basedir = "";
	            compress(file, out, basedir);
            }
            out.close();
            bln = true;
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }
        return bln;
    }
  
    private void compress(File file, ZipOutputStream out, String basedir) {   
        /* 判断是目录还是文件 */  
        if (file.isDirectory()) {   
            System.out.println("压缩：" + basedir + file.getName());   
            this.compressDirectory(file, out, basedir);   
        } else {   
            System.out.println("压缩：" + basedir + file.getName());   
            this.compressFile(file, out, basedir);   
        }   
    }   
  
    /** 压缩一个目录 */  
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {   
        if (!dir.exists())   
            return;   
  
        File[] files = dir.listFiles();   
        for (int i = 0; i < files.length; i++) {   
            /* 递归 */  
            compress(files[i], out, basedir + dir.getName() + "/");   
        }   
    }   
  
    /** 压缩一个文件 */  
    public void compressFile(File file, ZipOutputStream out, String basedir) {   
        if (!file.exists()) {   
            return;   
        }   
        try {   
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));   
            ZipEntry entry = new ZipEntry(basedir + file.getName());   
            out.putNextEntry(entry);   
            int count;   
            byte data[] = new byte[BUFFER];   
            while ((count = bis.read(data, 0, BUFFER)) != -1) {   
                out.write(data, 0, count);   
            }   
            bis.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }
}
