/**
 * 
 */
package test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * @author dongq
 * 
 *         create time : 2009-12-9 下午03:52:11
 */
public class Main {

	public static void main(String[] args) throws Exception{
		String path = System.getProperties().getProperty("user.dir")+"/temp_xsp_x5.xls";
		//System.out.println(path+"\n");
		InputStream inp = new FileInputStream(path);
		Workbook book = Workbook.getWorkbook(inp);
		Sheet sheet = book.getSheet("非空终端型号分布");
		//int columns = sheet.getColumns();
		int rows = sheet.getRows();
		String mobile = "";
		String time = "";
		List<String> mobiles = new ArrayList<String>();
		List<String> times = new ArrayList<String>();
		for(int row=1;row<rows;row++){
			int key = row;
			if(key%2==0){
				times.add(sheet.getCell(0, row).getContents());
			}else{
				mobiles.add(sheet.getCell(0, row).getContents());
			}
			//String value = sheet.getCell(0, row).getContents();
			//if("".equals(value)) continue;
			//System.out.println("insert into temp_xsp_x5(MOBILE,time) values("+key+",'"+value+"');");
		}
		for(int index=0;index<mobiles.size();index++){
			mobile = mobiles.get(index);
			time = times.get(index);
			System.out.println("insert into temp_xsp_x5(MOBILE,time) values("+mobile+",'"+time+"');");
		}
	}

}
