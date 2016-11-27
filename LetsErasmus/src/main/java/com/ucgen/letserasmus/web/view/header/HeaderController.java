package com.ucgen.letserasmus.web.view.header;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@SessionScoped
public class HeaderController extends BaseController {

	private User user;
	
	public User getUser() {
		return this.user;
	}
	
	public HeaderController() {
		user = new User();
	}
	
	public void login() {
		String test = "";
	}
	
}
