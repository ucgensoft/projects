package com.ucgen.letserasmus.web.view.application;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class WebApplication {

	private String urlPrefix;
	private String rootPlaceImagePath;
	
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
	
}
