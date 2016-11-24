package com.ucgen.common.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailUtil {
	
	private JavaMailSender mailSender;
	
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendMail(String content, String from, List<String> toList, List<String> ccList, String subject, File file) throws MailParseException {

		initializeMailSender();

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(from);
			for (String email : toList) {
				helper.addTo(email);
			}
			if (ccList != null && ccList.size() > 0) {
				for (String email : ccList) {
					helper.addCc(email);
				}
			}
			helper.setSubject(subject);
			helper.setText(content, true);

			if(file != null) {
				FileSystemResource fileSystemResource = new FileSystemResource(file);
				helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}

	}
	
	private synchronized void initializeMailSender() {
		String paramHost = null;
		String paramPort = null;
		String paramTimeout = null;
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		
		Properties props = new Properties();
		
		if (NumberUtil.isPositiveInteger(paramTimeout)) {
			props.setProperty("mail.smtp.connectiontimeout", paramTimeout);
			props.setProperty("mail.smtp.timeout", paramTimeout);
		} else {
			props.setProperty("mail.smtp.connectiontimeout", "60000");
			props.setProperty("mail.smtp.timeout", "60000");
		}
		
		sender.setJavaMailProperties(props);
		
		sender.setHost(paramHost);
		if (NumberUtil.isPositiveInteger(paramPort)) {
			sender.setPort(Integer.valueOf(paramPort));
		}

		this.mailSender = sender;
	}
	

}