package com.ucgen.letserasmus.library.community.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class CommunityTopic extends BaseModel {

	private static final long serialVersionUID = -844364983129509442L;

	private Long id;
	private Long communityGroupId;
	private Long userId;
	private String title;
	private String description;
	private String subUrl;
	private Date lastActivityDate;
	
	private CommunityGroup communityGroup;
	private User user;
	private List<CommunityTopicMessage> messageList;
	
	public CommunityTopic() {
		this(null, null);
	}
	
	public CommunityTopic(Long id, Long communityGroupId) {
		this.id = id;
		this.communityGroupId = communityGroupId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityGroupId() {
		return communityGroupId;
	}

	public void setCommunityGroupId(Long communityGroupId) {
		this.communityGroupId = communityGroupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubUrl() {
		return subUrl;
	}

	public void setSubUrl(String subUrl) {
		this.subUrl = subUrl;
	}

	public Date getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(Date lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public CommunityGroup getCommunityGroup() {
		return communityGroup;
	}

	public void setCommunityGroup(CommunityGroup communityGroup) {
		this.communityGroup = communityGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CommunityTopicMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<CommunityTopicMessage> messageList) {
		this.messageList = messageList;
	}

	public void addTopicMessage(CommunityTopicMessage message) {
		if (this.messageList == null) {
			this.messageList = new ArrayList<CommunityTopicMessage>();
		}
		this.messageList.add(message);
	}
	
}
