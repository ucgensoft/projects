package com.ucgen.letserasmus.web.view.application;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@ApplicationScoped
public class WebApplication extends BaseController {

	public static final String SESSION_USER = "user";
	
	private String urlPrefix;
	private String rootPlaceImagePath;
	private String rootProfileImagePath;
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public String getEncodedUrlPrefix() {
		return "https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&state=%2Fprofile&redirect_uri=http%3A%2F%2Flocalhost:8080%2FLetsErasmus&response_type=token&client_id=413623045258-hedcnepko6ovo8ruid4nbea4nk20osm8.apps.googleusercontent.com";
	}
	
	public String getRootPlaceImagePath() {
		return rootPlaceImagePath;
	}

	public WebApplication() {
		this.urlPrefix = "http://localhost:8080/LetsErasmus";
		this.rootPlaceImagePath = this.urlPrefix + "/place/images";
		this.rootProfileImagePath = this.urlPrefix + "/user/images/";
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
		} else {
			return "";
		}
	}
	
	public User getUser() {
		Object objUser = super.getSessionAttribute("user");
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
				photoUrl = this.rootProfileImagePath + "/" + user.getProfilePhotoId() + "/" + user.getProfilePhotoId() + "_profile.png";
			} else if (user.getProfileImageUrl() != null) {
				photoUrl = user.getProfileImageUrl();
			}
		}
		return photoUrl;
	}
	
	public String getLoginType() {
		String loginType = "";
		User user = (User) super.getSessionAttribute(SESSION_USER); 
		if (user != null && user.getLoginType() != null) {
			loginType = user.getLoginType().toString();
		}
		return loginType;
	}
	
}
