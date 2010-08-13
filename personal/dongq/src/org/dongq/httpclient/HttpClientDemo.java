/**
 * 
 */
package org.dongq.httpclient;


import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author dongq
 * 
 *         create time : 2010-8-12 下午07:57:57
 */
public final class HttpClientDemo {

	static final String UA = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8";
	public static void main(String[] args) throws Exception {
		final String uri = "http://www.moko.cc/jsps/common/login.jsp";
		
		HttpClient httpClient = new HttpClient();
		
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);//RFC_2109是支持较普遍的一个，还有其他cookie协议   
		HttpState initialState = new HttpState();   
		Cookie cookie=new Cookie();   
		cookie.setDomain("www.moko.cc");   
		cookie.setPath("/");   
		cookie.setName("多情环");   
		cookie.setValue("多情即无情");   
		initialState.addCookie(cookie);   
		httpClient.setState(initialState);  
		
		PostMethod method = new PostMethod(uri);
		
		method.addRequestHeader("User-Agent", UA);
		method.addParameter("Referer", "http://www.moko.cc/jsps/common/login.jsp");
		
		method.addParameter("usermingzi", "eastseven@foxmail.com");
		method.addParameter("userkey", "1983722");
		method.addParameter("userType", "1");
		
		int code = httpClient.executeMethod(method);
		System.out.println("响应代码：" + code);
		System.out.println("响应内容：" + method.getResponseBodyAsString());
		
		method.releaseConnection();
	}

}
