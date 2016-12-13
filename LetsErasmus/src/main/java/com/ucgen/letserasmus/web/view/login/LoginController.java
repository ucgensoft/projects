package com.ucgen.letserasmus.web.view.login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@SessionScoped
public class LoginController extends BaseController {

	private User user;
	
	public User getUser() {
		if (this.user == null || this.user.getEmail() == null) {
			Object objUser = super.getSession().getAttribute("USER");
			if (objUser instanceof User) {
				this.user = (User) objUser;
			}
		}
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public LoginController() {
		user = new User();
	}
	
	public void login() {
		
	}
	
}
