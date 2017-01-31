package com.ucgen.letserasmus.web.view.application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.ucgen.common.util.FileUtil;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@ApplicationScoped
public class WebApplication extends BaseController {

	public static final String FB_APP_ID = "305890206479305";
	public static final String FB_APP_SECRET = "f1f5ad76a1b8ae942346b575e3c96ede";
	public static final String REDIRECT_URI = "http://localhost:8080/FLogin/fbhome";

	private static String urlPrefix;
	private static String rootPlaceImagePath;
	private static String rootProfileImagePath;
	
	static {
		urlPrefix = "http://localhost:8080/LetsErasmus";
		rootPlaceImagePath = urlPrefix + "/place/images";
		rootProfileImagePath = urlPrefix + "/user/images/";
	}
	
	public String getFacebookId() {
		return FB_APP_ID;
	}
	
	public String getFbAuthUrl() {
		String fbLoginUrl = "";
		try {
			fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
					+ FB_APP_ID + "&redirect_uri="
					+ URLEncoder.encode(REDIRECT_URI, "UTF-8")
					+ "&scope=email";
		} catch (UnsupportedEncodingException e) {
			//TODO : logging
		}
		return fbLoginUrl;
	}
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public String getEncodedUrlPrefix() throws UnsupportedEncodingException {
		return URLEncoder.encode(this.urlPrefix, "UTF-8");
	}
	
	public String getRootPlaceImagePath() {
		return rootPlaceImagePath;
	}

	public WebApplication() {
		
	}
	
	public String getNgController() {
		String requestUrl = super.getRequest().getRequestURL().toString().toUpperCase();
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
		}else {
			return "";
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
	
	public String getUserProfilePhotoUrl() {
		String photoUrl = null;
		User user = this.getUser();
		if (user != null) {
			if (user.getProfilePhotoId() != null) {
				String smallFileName = AppUtil.getSmallUserPhotoName(user.getId(), user.getProfilePhotoId(), EnmFileType.getFileType(user.getProfilePhoto().getFileType()));
				photoUrl = AppUtil.concatPath(rootProfileImagePath, user.getId().toString(), smallFileName);
			} else if (user.getProfileImageUrl() != null) {
				photoUrl = user.getProfileImageUrl();
			}
		}
		return photoUrl;
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
	
	public static String getUserSmallPhotoUrl(User user) {
		String photoUrl = null;
		if (user != null) {
			if (user.getProfilePhotoId() != null) {
				String smallFileName = AppUtil.getSmallUserPhotoName(user.getId(), user.getProfilePhotoId(), EnmFileType.getFileType(user.getProfilePhoto().getFileType()));
				photoUrl = AppUtil.concatPath(rootProfileImagePath, user.getId().toString(), smallFileName);
			}
		}
		return photoUrl;
	}
	
	public static String getUserLargePhotoUrl(User user) {
		String photoUrl = getUserSmallPhotoUrl(user);
		return photoUrl.replace("_small_", "_large_");
	}
	
}
