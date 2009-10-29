/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

/**
 * @author dongq
 * 
 *         create time : 2009-10-27 обнГ04:34:14
 */
public class TextTest {

	public static void main(String[] args) {
		String txt = "http://news.xinhuanet.com/world/2009-10/26/content_12334278.htm";
		System.out.println(txt.substring(0, txt.lastIndexOf("/")+1));
	}

}
