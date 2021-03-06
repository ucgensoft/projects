package com.ucgen.letserasmus.web.api.user.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.complaint.model.Complaint;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.user.enumeration.EnmLoginType;

public class UiUser extends BaseModel {
	
	private static final long serialVersionUID = -1886945765194866601L;

	private Long id;
	private String email;
	private String password;
	private String msisdn;
	private String msisdnCountryCode;
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
	private String googleId;
	private String facebookId;
	private String jobTitle;
	private String schoolName;
	private String birthDate;
	private String description;
	private String residenceLocationName;
	private String languages;
	private String googleEmail;
	private String facebookEmail;
	private String resetPasswordToken;
	private Integer profileTypeId;
	private Integer homeCountryId;
	private Integer homeCityId;
	private Integer homeUniversityId;
	private Integer erasmusCountryId;
	private Integer erasmusCityId;
	private Integer erasmusUniversityId;
	private String adminFlag;
	
	private FileModel profilePhoto;
	private List<Place> placeList;
	private Integer placeListingCount;
	
	private String profileImageUrl;
	private Integer loginType;
	private String url;
	
	private PayoutMethod payoutMethod;
	
	private Map<Integer, Map<Long, Favorite>> favoriteMap;
	private Map<Integer, Map<Long, Complaint>> complaintMap;
	
	public UiUser() {
		this(null);
	}
	
	public UiUser(Long id) {
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
	public String getMsisdnCountryCode() {
		return msisdnCountryCode;
	}
	public void setMsisdnCountryCode(String msisdnCountryCode) {
		this.msisdnCountryCode = msisdnCountryCode;
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
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
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

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public PayoutMethod getPayoutMethod() {
		return payoutMethod;
	}

	public void setPayoutMethod(PayoutMethod payoutMethod) {
		this.payoutMethod = payoutMethod;
	}

	public List<Place> getPlaceList() {
		return placeList;
	}

	public void setPlaceList(List<Place> placeList) {
		this.placeList = placeList;
	}

	public Integer getPlaceListingCount() {
		return placeListingCount;
	}

	public void setPlaceListingCount(Integer placeListingCount) {
		this.placeListingCount = placeListingCount;
	}

	public Integer getProfileTypeId() {
		return profileTypeId;
	}

	public void setProfileTypeId(Integer profileTypeId) {
		this.profileTypeId = profileTypeId;
	}

	public Integer getHomeCountryId() {
		return homeCountryId;
	}

	public void setHomeCountryId(Integer homeCountryId) {
		this.homeCountryId = homeCountryId;
	}

	public Integer getHomeCityId() {
		return homeCityId;
	}

	public void setHomeCityId(Integer homeCityId) {
		this.homeCityId = homeCityId;
	}

	public Integer getHomeUniversityId() {
		return homeUniversityId;
	}

	public void setHomeUniversityId(Integer homeUniversityId) {
		this.homeUniversityId = homeUniversityId;
	}

	public Integer getErasmusCountryId() {
		return erasmusCountryId;
	}

	public void setErasmusCountryId(Integer erasmusCountryId) {
		this.erasmusCountryId = erasmusCountryId;
	}

	public Integer getErasmusCityId() {
		return erasmusCityId;
	}

	public void setErasmusCityId(Integer erasmusCityId) {
		this.erasmusCityId = erasmusCityId;
	}

	public Integer getErasmusUniversityId() {
		return erasmusUniversityId;
	}

	public void setErasmusUniversityId(Integer erasmusUniversityId) {
		this.erasmusUniversityId = erasmusUniversityId;
	}

	public String getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<Integer, Map<Long, Favorite>> getFavoriteMap() {
		return favoriteMap;
	}

	public void setFavoriteMap(Map<Integer, Map<Long, Favorite>> favoriteMap) {
		this.favoriteMap = favoriteMap;
	}

	public void createFavoriteMap() {
		if (this.favoriteMap == null) {
			this.favoriteMap = new HashMap<Integer, Map<Long, Favorite>>();
		}
	}
	
	public void addFavorite(Favorite favorite) {
		if (this.favoriteMap == null) {
			this.favoriteMap = new HashMap<Integer, Map<Long, Favorite>>();
		}
		if (!this.favoriteMap.containsKey(favorite.getEntityType())) {
			Map<Long, Favorite> entityTypeMap = new HashMap<Long, Favorite>();
			this.favoriteMap.put(favorite.getEntityType(), entityTypeMap);
		}
		this.favoriteMap.get(favorite.getEntityType()).put(favorite.getEntityId(), favorite);
	}
	
	public void removeFavorite(Favorite favorite) {
		if (this.favoriteMap == null) {
			this.favoriteMap = new HashMap<Integer, Map<Long, Favorite>>();
		}
		if (this.favoriteMap.containsKey(favorite.getEntityType())) {
			this.favoriteMap.get(favorite.getEntityType()).remove(favorite.getEntityId());
		}
	}
	
	public void addFavoriteList(List<Favorite> favoriteList) {
		if (favoriteList != null) {
			for (Favorite favorite : favoriteList) {
				this.addFavorite(favorite);
			}
		}
	}

	public Map<Integer, Map<Long, Complaint>> getComplaintMap() {
		return complaintMap;
	}

	public void setComplaintMap(Map<Integer, Map<Long, Complaint>> complaintMap) {
		this.complaintMap = complaintMap;
	}
	
	public void createComplaintMap() {
		if (this.complaintMap == null) {
			this.complaintMap = new HashMap<Integer, Map<Long, Complaint>>();
		}
	}
	
	public void addComplaint(Complaint complaint) {
		if (this.complaintMap == null) {
			this.complaintMap = new HashMap<Integer, Map<Long, Complaint>>();
		}
		if (!this.complaintMap.containsKey(complaint.getEntityType())) {
			Map<Long, Complaint> entityTypeMap = new HashMap<Long, Complaint>();
			this.complaintMap.put(complaint.getEntityType(), entityTypeMap);
		}
		this.complaintMap.get(complaint.getEntityType()).put(complaint.getEntityId(), complaint);
	}
	
	public void addComplaintList(List<Complaint> complaintList) {
		if (complaintList != null) {
			for (Complaint complaint : complaintList) {
				this.addComplaint(complaint);
			}
		}
	}
	
	public String getLoginEmail() {
		String tmpEmail = null;
		if (this.getLoginType() != null) {
			if (this.loginType.equals(EnmLoginType.LOCAL_ACCOUNT.getId())) {
				tmpEmail = this.email;
			} else if (this.loginType.equals(EnmLoginType.GOOGLE.getId())) {
				tmpEmail = this.googleEmail;
			} else if (this.loginType.equals(EnmLoginType.GOOGLE.getId())) {
				tmpEmail = this.facebookEmail;
			}
		}
		return tmpEmail;
	}
}
