package com.ucgen.letserasmus.library.batch;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.MailUtil;
import com.ucgen.letserasmus.library.mail.model.Email;

@Service
public class EmailSender implements Runnable {

	private MailUtil mailUtil;

	private LinkedBlockingQueue<Email> emailQueu;
	
	@Autowired
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}
	
	public EmailSender() {
		emailQueu = new LinkedBlockingQueue<Email>();
	}
	
	@PostConstruct
	public void initialize() {
		Thread mailSenderThread = new Thread(this);
		mailSenderThread.start();
	}
	
	public void addEmail(Email email) {
		this.emailQueu.add(email);
	}
	
	private Email getEmail() throws InterruptedException {
		return this.emailQueu.take();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Email email = this.getEmail();
				this.mailUtil.sendMail(email.getContent(), email.getToList(), email.getCcList(), email.getSubject(), email.getFile());
			} catch (Exception e) {
				System.out.println("EmailSender is failed with error: " + CommonUtil.getExceptionMessage(e));
			}
			
		}
	}
	
	
	
}
