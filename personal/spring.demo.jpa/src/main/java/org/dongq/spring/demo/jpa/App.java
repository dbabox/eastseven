package org.dongq.spring.demo.jpa;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static final String configLocation = "classpath:spring/applicationContext-core.xml";
	
	public static void main(String[] args) {
		System.out.println("Hello World!");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);
		
		System.out.println(ctx);
		RegionService service = ctx.getBean(RegionService.class);
		System.out.println(service);
		service.test();
	}
}
