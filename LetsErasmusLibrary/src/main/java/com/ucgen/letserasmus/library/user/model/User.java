package com.ucgen.letserasmus.library.user.model;

import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.common.model.BaseModel;

public class User extends BaseModel {
	
	private static final long serialVersionUID = -1886945765194866601L;

	private Long id;
	private String email;
	private String password;
	private String msisdn;
	private String firstName;
	private String lastName;
	private String gender;
	private Integer status;
	private String emailVerified;
	private String msisdnVerified;
	private String userActivationKeyEmail;
	private String userActivationKeyMsisdn;
	private Long profilePhotoId;
	private String facebookTokenId;
	private String ip;
	private String googleId;
	
	private String profileImageUrl;
	
	private Integer loginType;

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
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}
	public String getMsisdnVerified() {
		return msisdnVerified;
	}
	public void setMsisdnVerified(String msisdnVerified) {
		this.msisdnVerified = msisdnVerified;
	}
	public Long getProfilePhotoId() {
		return profilePhotoId;
	}
	public void setProfilePhotoId(Long profilePhotoId) {
		this.profilePhotoId = profilePhotoId;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public String getFullName() {
		String fullName = "";
		fullName = StringUtil.append(fullName, this.firstName, " ");
		fullName = StringUtil.append(fullName, this.lastName, " ");
		return fullName;
	}
	
}
