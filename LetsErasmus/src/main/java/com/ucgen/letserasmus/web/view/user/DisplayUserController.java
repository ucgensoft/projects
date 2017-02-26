package com.ucgen.letserasmus.web.view.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.commons.lang3.math.NumberUtils;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;
import com.ucgen.letserasmus.web.view.BaseController;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@ManagedBean
public class DisplayUserController extends BaseController {
	
	@ManagedProperty(value="#{userService}")
	private IUserService userService;
	
	@ManagedProperty(value="#{webApplication}")
	private WebApplication webApplication;

	private User user;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
		Object objUserId = super.getRequestParameter("userId");
		if (objUserId != null && NumberUtils.isDigits(objUserId.toString())) {
			user = this.userService.getUser(new User(Long.parseLong(objUserId.toString())));
		}
	}

	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}

	public User getUser() {
		return user;
	}
	/*
	public String getUserLargePhotoUrl() {
		return this.webApplication.getUserLargePhotoUrl(this.getUser());
	}
	*/
}
