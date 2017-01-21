package com.ucgen.letserasmus.library.user.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class User extends BaseModel {
	
	private static final long serialVersionUID = -1886945765194866601L;

	private Long id;
	private String email;
	private String password;
	private String msisdn;
	private String firstName;
	private String lastName;
	private Integer status;
	private Integer emailVerified;
	private Integer msisdnVerified;
	private String userActivationKeyEmail;
	private String userActivationKeyMsisdn;
	private Long fileId;
	private String facebookTokenId;
	private String ip;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(Integer emailVerified) {
		this.emailVerified = emailVerified;
	}
	public Integer getMsisdnVerified() {
		return msisdnVerified;
	}
	public void setMsisdnVerified(Integer msisdnVerified) {
		this.msisdnVerified = msisdnVerified;
	}
	public String getUserActivationKeyEmail() {
		return userActivationKeyEmail;
	}
	public void setUserActivationKeyEmail(String userActivationKeyEmail) {
		this.userActivationKeyEmail = userActivationKeyEmail;
	}
	public String getUserActivationKeyMsisdn() {
		return userActivationKeyMsisdn;
	}
	public void setUserActivationKeyMsisdn(String userActivationKeyMsisdn) {
		this.userActivationKeyMsisdn = userActivationKeyMsisdn;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getFacebookTokenId() {
		return facebookTokenId;
	}
	public void setFacebookTokenId(String facebookTokenId) {
		this.facebookTokenId = facebookTokenId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getFullName() {
		return String.format("%s %s", this.getFirstName(), this.getLastName());
	}
	
}
