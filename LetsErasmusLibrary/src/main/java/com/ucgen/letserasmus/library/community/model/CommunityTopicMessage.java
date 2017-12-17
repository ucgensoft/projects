package com.ucgen.letserasmus.library.community.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class CommunityTopicMessage extends BaseModel {

	private static final long serialVersionUID = 6890280818504544729L;

	private Long id;
	private Long communityTopicId;
	private Long userId;
	private String description;
	
	private User user;
	
	public CommunityTopicMessage() {
		
	}
	
	public CommunityTopicMessage(Long id, Long communityTopicId) {
		this.id = id;
		this.communityTopicId = communityTopicId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCommunityTopicId() {
		return communityTopicId;
	}
	public void setCommunityTopicId(Long communityTopicId) {
		this.communityTopicId = communityTopicId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
}
