package com.ucgen.letserasmus.web.view.application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ucgen.common.model.Size;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@Service
@Scope("singleton")
public class WebApplication extends BaseController {
	
	public static final String FB_APP_ID = "305890206479305";
	
	public static final String LOCAL_APP_PATH = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp";

	private String urlPrefix;
	
	private String emailVerificationUrl;
	
	private String rootUserPhotoPath;
	private String rootPlacePhotoPath;
	
	private String userPhotoUrlTemplate;
	private String placePhotoUrlTemplate;
	private String rootPlaceImageUrl;
	private String rootUserImageUrl;
	private String defaultUserPhotoUrl;
	
	private String userPhotoNameTemplate;
	private String placePhotoNameTemplate;
	
	private String placePhotoPath;
	private String userPhotoPath;
	
	private Size smallUserPhotoSize;
	private Size mediumUserPhotoSize;
	
	private Size smallPlacePhotoSize;
	private Size mediumPlacePhotoSize;
		
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
	
	public String getRootUserPhotoPath() {
		return this.rootUserPhotoPath;
	}
	
	public String getRootPlacePhotoPath() {
		return this.rootPlacePhotoPath;
	}

	public String getUserPhotoUrlTemplate() {
		return userPhotoUrlTemplate;
	}

	public String getDefaultUserPhotoUrl() {
		return defaultUserPhotoUrl;
	}

	public String getRootUserImageUrl() {
		return rootUserImageUrl;
	}

	public String getUserPhotoNameTemplate() {
		return userPhotoNameTemplate;
	}

	public String getPlacePhotoNameTemplate() {
		return placePhotoNameTemplate;
	}

	public Size getSmallUserPhotoSize() {
		return smallUserPhotoSize;
	}

	public Size getMediumUserPhotoSize() {
		return mediumUserPhotoSize;
	}

	public Size getSmallPlacePhotoSize() {
		return smallPlacePhotoSize;
	}

	public Size getMediumPlacePhotoSize() {
		return mediumPlacePhotoSize;
	}

	public WebApplication() {
		
		emailVerificationUrl = urlPrefix + "/pages/Main.xhtml?user=#paramUserId#&code=#paramEmailVerificationCode#&op=1";
		
		urlPrefix = "http://localhost:8080/LetsErasmus";
		
		rootPlaceImageUrl = urlPrefix + "/place/images";
		rootUserImageUrl = urlPrefix + "/user/images";
		
		
		rootUserPhotoPath = LOCAL_APP_PATH + "\\user\\images\\";
		rootPlacePhotoPath = LOCAL_APP_PATH + "\\place\\images\\";
		
		userPhotoNameTemplate = "user_#userId#_#photoId#_#size#.png";
		placePhotoNameTemplate = "place_#placeId#_#photoId#_#size#.png";
		
		defaultUserPhotoUrl = rootUserImageUrl + "/default_profile_#size#.png";
		userPhotoUrlTemplate = rootUserImageUrl + "/#userId#/" + userPhotoNameTemplate;
		placePhotoUrlTemplate = rootPlaceImageUrl + "/#placeId#/" + placePhotoNameTemplate;
		
		placePhotoPath = AppUtil.concatPath(rootPlacePhotoPath, "#placeId#", placePhotoNameTemplate);
		userPhotoPath = AppUtil.concatPath(rootUserPhotoPath, "#userId#", userPhotoNameTemplate);
		
		smallUserPhotoSize = new Size(300f, 300f);
		smallUserPhotoSize = new Size(400f, 700f);
		
		smallPlacePhotoSize = new Size(500f, 300f);
		mediumPlacePhotoSize = new Size(800f, 800f);
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
			} else if (requestUrl.contains("PAGES/DASHBOARD/TRUSTANDVERIFICATION.XHTML")) {
				return "trustAndverificationCtrl";
			} else if (requestUrl.contains("PAGES/VERIFICATION.XHTML")) {
				return "verificationCtrl";
			} else if (requestUrl.contains("PAGES/RESERVATION.XHTML")) {
				return "reservationCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/MESSAGELIST.XHTML")) {
				return "messageListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/CONVERSATION.XHTML")) {
				return "conversationCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/RESERVATIONLIST.XHTML")) {
				return "reservationListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/TRIPLIST.XHTML")) {
				return "tripListCtrl";
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

	public String getUserPhotoName(Long userId, Long photoId, EnmSize size) {
		String photoName = this.userPhotoNameTemplate.replace("#userId#", userId.toString());
		photoName = photoName.replace("#photoId#", photoId.toString());
		photoName = photoName.replace("#size#", size.getValue());
		return photoName;
	}
	
	public String getPlacePhotoName(Long placeId, Long photoId, EnmSize size) {
		String photoName = this.placePhotoNameTemplate.replace("#placeId#", placeId.toString());
		photoName = photoName.replace("#photoId#", photoId.toString());
		photoName = photoName.replace("#size#", size.getValue());
		return photoName;
	}
	
	public String getActiveUserPhotoUrl(String size) {
		String photoUrl = null;
		User user = this.getUser();
		if (user != null) {
			if (user.getProfilePhotoId() != null) {
				photoUrl = this.getUserPhotoUrl(user.getId(), user.getProfilePhotoId(), size);
			} else if (user.getProfileImageUrl() != null) {
				photoUrl = user.getProfileImageUrl();
			}
		}
		return photoUrl;
	}
	
	public String getUserPhotoUrl(Long userId, Long photoId, String size) {
		String photoUrl = null;
		if (photoId != null && photoId > 0) {
			photoUrl = this.userPhotoUrlTemplate.replaceAll("#userId#", userId.toString());
			photoUrl = photoUrl.replaceAll("#photoId#", photoId.toString());
		} else {
			photoUrl = this.defaultUserPhotoUrl;
		}
		photoUrl = photoUrl.replace("#size#", size);
		return photoUrl;
	}
	
	public String getUserPhotoPath(Long userId, Long photoId, String size) {
		String photoPath = this.userPhotoPath.replaceAll("#userId#", userId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public String getPlacePhotoUrl(Long placeId, Long photoId, String size) {
		String photoPath = this.placePhotoUrlTemplate.replaceAll("#placeId#", placeId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public String getPlacePhotoPath(Long placeId, Long photoId, String size) {
		String photoPath = this.placePhotoPath.replaceAll("#placeId#", placeId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public boolean isFooterVisible() {
		String requestUrl = super.getRequest().getRequestURL().toString();
		requestUrl = requestUrl.trim().replace(this.urlPrefix, "").toUpperCase();
		if (requestUrl.contains("PAGES/PLACE.XHTML") 
				|| requestUrl.contains("PAGES/SEARCHRESULT.XHTML")) {
			return false;
		} else {
			return true;
		}		
	}
	
}
