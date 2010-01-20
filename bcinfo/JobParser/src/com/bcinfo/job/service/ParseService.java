/**
 * 
 */
package com.bcinfo.job.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.Cell;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.job.Main;
import com.bcinfo.job.dao.DaoService;
import com.bcinfo.job.model.JobBean;

/**
 * @author dongq
 * 
 *         create time : 2009-12-10 上午10:54:24
 */
public class ParseService implements Job {

	private static final Logger log = Logger.getLogger(ParseService.class);

	private static int column_num = 27;
	private long addCount = 0;
	private long modCount = 0;
	private long delCount = 0;
	public static final String ADD = "add";
	public static final String MODIFY = "mod";
	public static final String DELETE = "del";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("解析开始");
		String path = "";
		String name = "";
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(Main.CONFIG_PATH + "config.properties"));
			path = property.getProperty("excel.file.path");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		try {
			List<File> fileList = new ArrayList<File>();
			File dir = new File(path);
			log.debug("Excel文件夹:"+path);
			File[] files = dir.listFiles();
			if(files!=null&&files.length>0){
				for(File f : files){
					if(f.isDirectory()) continue;
					name = f.getName();
					if(!name.endsWith(".xls")){
						String dest = path+"/fail/"+name;
						log.debug("Fail Dest File : "+dest);
						File destFile = new File(dest);
						FileUtils.copyFile(f, destFile);
						log.info("文件"+name+"不能解析,移动至"+dest+","+(f.delete()?"成功":"失败"));
						continue;
					}
					InputStream excel = new FileInputStream(f);
					Map<String, List<JobBean>> map = new ParseService().parse(excel);
					DaoService dao = new DaoService();
					if(map !=null&&!map.isEmpty()){
						List<JobBean> list = map.get(ParseService.ADD);
						if(list!=null&&!list.isEmpty()){
							log.info(name+" -- 添加 ----------------------------------------- ");
							addCount = list.size();
							log.info(name+" 保存" + addCount + "条记录："+ (dao.saveBatch(list)?"成功":"失败"));
						}
						
						log.info(name+" -- 修改 ----------------------------------------- ");
						list = map.get(ParseService.MODIFY);
						if(list!=null&&!list.isEmpty()){
							log.info(name+" 修改" + modCount + "条记录：" + (dao.updateBatch(list)?"成功":"失败"));
						}
						
						log.info(name+" -- 删除 ----------------------------------------- ");
						list = map.get(ParseService.DELETE);
						if(list!=null&&!list.isEmpty()){
							log.info(name+" 删除" + delCount + "条记录："+ (dao.deleteBatch(list)?"成功":"失败"));
						}
					}
					String dest = path+"/success/"+name;
					File destFile = new File(dest);
					FileUtils.copyFile(f, destFile);
					fileList.add(f);
					excel.close();
					log.info("成功解析文件"+name+",移动至"+dest);
				}
			}
			if(!fileList.isEmpty()){
				for(File f : fileList){
					log.info("删除文件"+f.getAbsolutePath()+"："+(FileUtils.deleteQuietly(f)?"成功":"失败"));
				}
			}
			log.info("解析结束");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e);
			log.info("解析文件["+path+"/"+name+"]失败");
		}
	}
	
	public Map<String, List<JobBean>> parse(InputStream excel) {
		List<JobBean> list = null;
		Map<String, List<JobBean>> map = new HashMap<String, List<JobBean>>();
		try {

			Workbook book = Workbook.getWorkbook(excel);
			int count = book.getNumberOfSheets();
			for (int i = 0; i < count; i++) {
				Sheet sheet = book.getSheet(i);
				list = getJobBeanList(sheet);
				if(i==0&&list!=null&&!list.isEmpty()){
					map.put(ParseService.ADD, list);
				}else if(i==1&&list!=null&&!list.isEmpty()){
					map.put(ParseService.MODIFY, list);
				}else if(i==2&&list!=null&&!list.isEmpty()){
					map.put(ParseService.DELETE, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			try {
				excel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	public List<JobBean> getJobBeanList(Sheet sheet) {
		List<JobBean> list = null;
		JobBean jobBean = null;
		String logoPath = "";
		try {
			Properties property = new Properties();
			property.load(new FileInputStream(Main.CONFIG_PATH+"config.properties"));
			logoPath = property.getProperty("excel.file.logo.path");
			int rownum = sheet.getRows();
			list = new ArrayList<JobBean>();
			for(int index=1;index<rownum;index++){
				Cell first = sheet.getCell(0, index);
				if("Empty".equals(first.getType().toString())) continue;
				jobBean = new JobBean();
				for(int column=0;column<column_num;column++){
					Cell cell = sheet.getCell(column, index);
					Object value = null;
					//log.debug(index+":"+column+":"+cell.getType()+"|"+cell.getContents());
					if("Empty".equals(cell.getType().toString())){
						value = "";
					}else{
						if(cell instanceof LabelCell){
							LabelCell labelCell = (LabelCell)cell;
							//log.debug(column+":"+labelCell.getString());
							value = labelCell.getString();
						}else if(cell instanceof NumberCell){
							NumberCell numberCell = (NumberCell)cell;
							//log.debug(column+":"+numberCell.getContents());
							value = numberCell.getContents();
						}else if(cell instanceof DateCell){
							DateCell dateCell = (DateCell)cell;
							//log.debug(column+":"+dateCell.getDate());
							value = dateCell.getDate();
						}else{
							//log.debug(column+":");
							value = "";
						}
					}
					
					switch (column) {
					case 0:
						jobBean.setJobId(Long.parseLong(value.toString()));
						break;
					case 1:
						jobBean.setJobGsdq(value.toString());
						break;
					case 2:
						jobBean.setJobSshy(value.toString());
						break;
					case 3:
						jobBean.setJobGsmc(value.toString());
						break;
					case 4:
						jobBean.setJobLxdh(value.toString());
						break;
					case 5:
						jobBean.setJobXxdz(value.toString());
						break;
					case 6:
						jobBean.setJobYb(value.toString());
						break;
					case 7:
						jobBean.setJobSj(value.toString());
						break;
					case 8:
						jobBean.setJobLxr(value.toString());
						break;
					case 9:
						jobBean.setJobDzyx(value.toString());
						break;
					case 10:
						jobBean.setJobGsjj(value.toString());
						break;
					case 11:
						jobBean.setJobZwmc(value.toString());
						break;
					case 12:
						jobBean.setJobZwms(value.toString());
						break;
					case 13:
						jobBean.setJobZyyq(value.toString());
						break;
					case 14:
						jobBean.setJobXlyq(value.toString());
						break;
					case 15:
						jobBean.setJobGzjy(value.toString());
						break;
					case 16:
						jobBean.setJobXbyq(value.toString());
						break;
					case 17:
						jobBean.setJobSfjz(value.toString());
						break;
					case 18:
						jobBean.setJobYxfw(value.toString());
						break;
					case 19:
						jobBean.setJobZprs(value.toString());
						break;
					case 20:
						jobBean.setJobGzdd(value.toString());
						break;
					case 21:
						if(value!=null&&!"".equals(value))
						jobBean.setJobKsrq((Date)value);
						break;
					case 22:
						if(value!=null&&!"".equals(value))
						jobBean.setJobJsrq((Date)value);
						break;
					case 23:
						jobBean.setJobSftj(value.toString());
						break;
					case 24:
						jobBean.setJobSfrm(value.toString());
					case 25:
						jobBean.setJobTjdy(value.toString());
						break;
					case 26:
						if(value!=null&&!"".equals(value.toString())){
							jobBean.setJobLogo(logoPath+value.toString());
						}else{
							jobBean.setJobLogo(value.toString());
						}
						break;
					default:
						break;
					}
				}
				list.add(jobBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return list;
	}

	// TODO TEST
	public static void main(String[] args) {
		try {
			String path = System.getProperties().getProperty("user.dir") + "/test.xls";
			System.out.println(path);
			InputStream excel = new FileInputStream(path);
			Map<String, List<JobBean>> map = new ParseService().parse(excel);
			DaoService dao = new DaoService();
			if(map !=null&&!map.isEmpty()){
				List<JobBean> list = map.get(ParseService.ADD);
				System.out.println(" -- 添加 ----------------------------------------- ");
				if(list!=null&&!list.isEmpty()){
					boolean bln = dao.saveBatch(list);
					System.out.println(" 保存："+bln);
				}
				System.out.println(" -- 修改 ----------------------------------------- ");
				list = map.get(ParseService.MODIFY);
				if(list!=null&&!list.isEmpty()){
					boolean bln = dao.updateBatch(list);
					System.out.println(" 修改："+bln);
				}
				System.out.println(" -- 删除 ----------------------------------------- ");
				list = map.get(ParseService.DELETE);
				if(list!=null&&!list.isEmpty()){
					boolean bln = dao.deleteBatch(list);
					System.out.println(" 删除："+bln);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

}
