package com.ucgen.letserasmus.web.view.login;

import javax.faces.bean.ManagedBean;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class LoginController extends BaseController {

	private User user;
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public LoginController() {
		user = new User();
	}
	
	public void login() {
		String test = "";
	}
	
}
