package com.ucgen.letserasmus.web.view.application;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@ApplicationScoped
public class WebApplication extends BaseController {

	private String urlPrefix;
	private String rootPlaceImagePath;
	private User user;
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public String getRootPlaceImagePath() {
		return rootPlaceImagePath;
	}

	public WebApplication() {
		this.urlPrefix = "http://localhost:8080/LetsErasmus";
		this.rootPlaceImagePath = this.urlPrefix + "/place/images";
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
	
}
