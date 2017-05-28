package com.ucgen.letserasmus.library.mail.model;

import java.io.File;
import java.util.List;

public class Email {

	private String subject;
	private String content;
	private List<String> toList;
	private List<String> ccList;
	private File file;
	
	public Email(String subject, String content, List<String> toList, List<String> ccList, File file) {
		this.subject = subject;
		this.content = content;
		this.toList = toList;
		this.ccList = ccList;
		this.file = file;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getToList() {
		return toList;
	}
	public void setToList(List<String> toList) {
		this.toList = toList;
	}
	public List<String> getCcList() {
		return ccList;
	}
	public void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
