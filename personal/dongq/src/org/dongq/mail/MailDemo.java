/**
 * 
 */
package org.dongq.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author Dongq
 * 
 */
public class MailDemo {

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;
	//private MimeMailMessage mimeMessage;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

//	public void setMimeMessage(MimeMailMessage mimeMessage) {
//		this.mimeMessage = mimeMessage;
//	}
	
	public void send() {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		String[] to = {"fengwq@flying-it.com","xusp@flying-it.com"};
		msg.setTo(to);
		msg.setText("群发病毒测试");
		try {
			this.mailSender.send(msg);
			System.out.println("mail sender...");
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

		
	}

	public void sendFile() throws Exception {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("58.213.123.141");

		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo("dongq@flying-it.com");

		helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);

		FileSystemResource res = new FileSystemResource(new File("D:/Backup/我的文档/My Pictures/hero_2_20091020.jpg"));
		helper.addInline("identifier1234", res);

		sender.send(message);
	}
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:spring.xml");
		MailDemo demo = (MailDemo)ctx.getBean("mailDemo");
		demo.sendFile();
		System.out.println("done...");
	}
}
