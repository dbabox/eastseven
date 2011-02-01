/**
 * 
 */
package org.dongq;

import java.text.SimpleDateFormat;

/**
 * @author dongq
 *
 */
public final class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		printDays();
		
		//Class.forName("");
	}

	public static void printDays() throws Exception {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long start = sdf.parse("2010-09-21 22:13:35").getTime();
		long time = System.currentTimeMillis() - start;
		long day = time / (24 * 60 * 60 * 1000);
		System.out.println("相识第" + day + "天");
		
//		start = sdf.parse("1982-03-28 00:00:00").getTime();
//		time = sdf.parse("1983-07-22 00:00:00").getTime() - start;
//		day = time / (24 * 60 * 60 * 1000);
//		System.out.println("天数：" + day);
	}
	
	
}
