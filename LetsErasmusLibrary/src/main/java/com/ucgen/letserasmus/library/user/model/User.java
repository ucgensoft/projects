package com.ucgen.letserasmus.library.user.model;

import java.util.Date;

import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.file.model.FileModel;

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
	private String facebookId;
	private String jobTitle;
	private String schoolName;
	private Date birthDate;
	private String description;
	private String residenceLocationName;
	private String languages;
	private String googleEmail;
	private String facebookEmail;
	
	private FileModel profilePhoto;
	
	private String profileImageUrl;
	private Integer loginType;
	
	public User() {
		this(null);
	}
	
	public User(Long id) {
		this.id = id;
	}
	
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
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResidenceLocationName() {
		return residenceLocationName;
	}
	public void setResidenceLocationName(String residenceLocationName) {
		this.residenceLocationName = residenceLocationName;
	}
	public String getLanguages() {
		return languages;
	}
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	public FileModel getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(FileModel profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	public String getFacebookEmail() {
		return facebookEmail;
	}

	public void setFacebookEmail(String facebookEmail) {
		this.facebookEmail = facebookEmail;
	}

	public String getFullName() {
		String fullName = "";
		fullName = StringUtil.append(fullName, this.firstName, " ");
		fullName = StringUtil.append(fullName, this.lastName, " ");
		return fullName;
	}
	public String getMaskedMsisdn() {
		if (this.msisdn != null) {
			return this.msisdn.substring(0, 3) + "*** *** " + this.msisdn.substring(this.msisdn.length() - 4);
		} else {
			return null;
		}
	}
	
}
