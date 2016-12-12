package com.ucgen.letserasmus.web.view.application;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class WebApplication {

	private String urlPrefix;
	
	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public WebApplication() {
		this.urlPrefix = "/LetsErasmus";
	}
	
}
