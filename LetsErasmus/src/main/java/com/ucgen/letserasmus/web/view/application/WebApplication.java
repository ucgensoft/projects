package com.ucgen.letserasmus.web.view.application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@Service
@Scope("singleton")
public class WebApplication extends BaseController {
	
	public static final String FB_APP_ID = "305890206479305";
	
	public static final String LOCAL_APP_PATH = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp";

	private String urlPrefix;
	private String rootPlaceImageUrl;
	private String rootProfileImageUrl;
	private String emailVerificationUrl;
	private String rootProfilePhotoPath;
	private String profilePhotoUrlTemplate;
	private String defaultProfilePhotoUrl;
	private String profilePhotoPathTemplate;
		
	public String getFacebookId() {
		return FB_APP_ID;
	}
		
	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public String getEncodedUrlPrefix() throws UnsupportedEncodingException {
		return URLEncoder.encode(this.getUrlPrefix(), "UTF-8");
	}
	
	public String getRootPlaceImageUrl() {
		return rootPlaceImageUrl;
	}

	public String getEmailVerificationUrl() {
		return emailVerificationUrl;
	}

	public void setEmailVerificationUrl(String emailVerificationUrl) {
		this.emailVerificationUrl = emailVerificationUrl;
	}
	
	public String getRootProfilePhotoPath() {
		return this.rootProfilePhotoPath;
	}

	public WebApplication() {
		urlPrefix = "http://localhost:8080/LetsErasmus";
		rootPlaceImageUrl = urlPrefix + "/place/images";
		rootProfileImageUrl = urlPrefix + "/user/images";
		emailVerificationUrl = urlPrefix + "/pages/Main.xhtml?user=#paramUserId#&code=#paramEmailVerificationCode#&op=1";
		rootProfilePhotoPath = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp\\user\\images\\";
		
		defaultProfilePhotoUrl = rootProfileImageUrl + "/default_profile_#size#.png";
		profilePhotoUrlTemplate = rootProfileImageUrl + "/#userId#/user_#userId#_photo_#photoId#_#size#.png";
	}
	
	public String getNgController() {
		String requestUrl = super.getRequest().getRequestURL().toString();
		requestUrl = requestUrl.trim().replace(this.urlPrefix, "");
		if (requestUrl.isEmpty() || requestUrl.equals("/")) {
			return "mainCtrl";
		} else {
			requestUrl = requestUrl.toUpperCase();
			if (requestUrl.contains("PAGES/MAIN.XHTML")) {
				return "mainCtrl";
			} else if (requestUrl.contains("PAGES/SEARCHRESULT.XHTML")) {
				return "searchResultCtrl";
			} else if (requestUrl.contains("PAGES/PLACE.XHTML")) {
				return "placeCtrl";
			} else if (requestUrl.contains("PAGES/PLACEDETAIL.XHTML")) {
				return "placeDetailCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/EDITUSER.XHTML")) {
				return "editUserCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/LISTINGS.XHTML")) {
				return "listingsCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/VERIFICATION.XHTML")) {
				return "trustAndverificationCtrl";
			} else if (requestUrl.contains("PAGES/VERIFICATION.XHTML")) {
				return "verificationCtrl";
			} else if (requestUrl.contains("PAGES/RESERVATION.XHTML")) {
				return "reservationCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/MESSAGELIST.XHTML")) {
				return "messageListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/CONVERSATION.XHTML")) {
				return "conversationCtrl";
			} else {
				return "";
			}	
		}
	}
	
	public User getUser() {
		Object objUser = super.getSessionAttribute(EnmSession.USER.getId());
		if (objUser != null) {
			return (User) objUser;
		} else {
			return null;
		}
	}
	
	public String getGoogleId() {
		return "413623045258-hedcnepko6ovo8ruid4nbea4nk20osm8.apps.googleusercontent.com";
	}
		
	public String getLoginType() {
		String loginType = "";
		User user = (User) super.getSessionAttribute(EnmSession.USER.getId()); 
		if (user != null && user.getLoginType() != null) {
			loginType = user.getLoginType().toString();
		}
		return loginType;
	}
	
	public String getFacebookTokenId() {
		String facebookTokenId = "";
		User user = (User) super.getSessionAttribute(EnmSession.USER.getId()); 
		if (user != null && user.getFacebookTokenId() != null) {
			facebookTokenId = user.getFacebookTokenId();
		}
		return facebookTokenId;
	}
	
	public String setActiveOperation(int operationId) {
		if (super.getSession() != null) {
			super.getSession().removeAttribute(EnmSession.ACTIVE_OPERATION.getId());;
			super.getSession().setAttribute(EnmSession.ACTIVE_OPERATION.getId(), EnmOperation.getOperation(operationId));;
		}
		return "";
	}
	
	/*
	public String getUserSmallPhotoUrl(User user) {
		String photoUrl = null;
		if (user != null && user.getProfilePhotoId() != null) {
			String smallFileName = AppUtil.getSmallUserPhotoName(user.getId(), user.getProfilePhotoId(), EnmFileType.getFileType(user.getProfilePhoto().getFileType()));
			photoUrl = AppUtil.concatPath(rootProfileImagePath, user.getId().toString(), smallFileName);
		} else {
			photoUrl = this.defaultMediumProfilePhotoUrl;
		}
		return photoUrl;
	}
	
	public String getUserLargePhotoUrl(User user) {
		String photoUrl = null;
		if (user.getProfilePhotoId() != null) {
			photoUrl = getUserSmallPhotoUrl(user);
			if (photoUrl != null) {
				photoUrl = photoUrl.replace("_small_", "_large_");
			}
		} else {
			photoUrl = this.defaultMediumProfilePhotoUrl;
		}
		
		return photoUrl;
	}
	*/
	
	public String getActiveUserProfilePhotoUrl(String size) {
		String photoUrl = null;
		User user = this.getUser();
		if (user != null) {
			if (user.getProfilePhotoId() != null) {
				photoUrl = this.getUserProfilePhotoUrl(user.getId(), user.getProfilePhotoId(), size);
			} else if (user.getProfileImageUrl() != null) {
				photoUrl = user.getProfileImageUrl();
			}
		}
		return photoUrl;
	}
	
	public String getUserProfilePhotoUrl(Long userId, Long photoId, String size) {
		String photoUrl = null;
		if (photoId != null && photoId > 0) {
			photoUrl = this.profilePhotoUrlTemplate.replace("#userId#", userId.toString());
			photoUrl = photoUrl.replace("#photoId#", photoId.toString());
		} else {
			photoUrl = this.defaultProfilePhotoUrl;
		}
		photoUrl = photoUrl.replace("#size#", size);
		return photoUrl;
	}
	
	public boolean isFooterVisible() {
		String requestUrl = super.getRequest().getRequestURL().toString();
		requestUrl = requestUrl.trim().replace(this.urlPrefix, "");
		if (requestUrl.contains("PAGES/PLACE.XHTML") 
				|| requestUrl.contains("PAGES/SEARCHRESULT.XHTML")) {
			return false;
		} else {
			return true;
		}		
	}
	
}
