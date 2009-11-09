/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * @author dongq
 * 
 * create time : 2009-11-4 ÉÏÎç09:14:53
 */
public class PathTestMain {

	public static void main(String[] args) {
		Properties property = System.getProperties();
		if(property!=null){
			Set<Object> key = property.keySet();
			for(Object obj : key){
				System.out.println(obj+" : "+property.get(obj));
			}
			
			String userDir = property.getProperty("user.dir");
			try{
				File file = new File(userDir);
				if(file.exists() && file.isDirectory()){
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
