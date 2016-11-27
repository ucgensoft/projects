package com.ucgen.letserasmus.web.view.main;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ucgen.letserasmus.library.comment.service.ICommentService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class MainController extends BaseController {

	@ManagedProperty(value="#{commentService}")
	private ICommentService commentService;

	private User user;
	
	public ICommentService getCommentService() {
		return commentService;
	}
		
	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return "Hello There";
	}
	
	public MainController() {
		this.user = new User();
	}
	
	public void login() {
		try {
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
}
