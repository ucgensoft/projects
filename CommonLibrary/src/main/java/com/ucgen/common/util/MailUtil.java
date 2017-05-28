package com.ucgen.common.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailUtil {
	
	//private final String SYSTEM_EMAIL = "ucgensoft@gmail.com";
	//private final String SYSTEM_EMAIL_PASSWORD = "LetsErasmus";
	
	private String adminEmail;
	private String adminEmailPassword;
	private String smtpServer;
	
	private JavaMailSender mailSender;
	
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
		
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminEmailPassword() {
		return adminEmailPassword;
	}

	public void setAdminEmailPassword(String adminEmailPassword) {
		this.adminEmailPassword = adminEmailPassword;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	
	public void sendMail(String content, List<String> toList, List<String> ccList, String subject, File file) throws MailParseException {

		initializeMailSender();

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(this.adminEmail);
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
		String[] smptpServerIpPort = this.smtpServer.split(":");
		
		String paramHost = smptpServerIpPort[0]; //"smtp.gmail.com";
		String paramPort = smptpServerIpPort[1]; //"587";
		String paramTimeout = "20000";
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		
		Properties props = new Properties();
		
		if (NumberUtil.isPositiveInteger(paramTimeout)) {
			props.setProperty("mail.smtp.connectiontimeout", paramTimeout);
			props.setProperty("mail.smtp.timeout", paramTimeout);
		} else {
			props.setProperty("mail.smtp.connectiontimeout", "60000");
			props.setProperty("mail.smtp.timeout", "60000");
		}
		
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
        
		sender.setUsername(this.adminEmail);
		sender.setPassword(this.adminEmailPassword);
		
		sender.setJavaMailProperties(props);
		
		sender.setHost(paramHost);
		if (NumberUtil.isPositiveInteger(paramPort)) {
			sender.setPort(Integer.valueOf(paramPort));
		}

		this.mailSender = sender;
	}

}