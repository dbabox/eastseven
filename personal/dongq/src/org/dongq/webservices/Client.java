/**
 * 
 */
package org.dongq.webservices;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dongq.cfx.service.DongqService;

/**
 * @author dongq
 * 
 *         create time : 2010-8-9 下午03:39:42
 */
public class Client {

	private DongqService dongqService;
	
	public void setDongqService(DongqService dongqService) {
		this.dongqService = dongqService;
	}
	
	public String request() {
		return this.dongqService.helloWorld();
	}
	
	public static void main(String[] args) {
		System.out.println("start...");
		String configLocation = "classpath:spring-ws.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		Client dongq = (Client)context.getBean("client");
		System.out.println(dongq.request());
		System.out.println("end...");
	}

}
