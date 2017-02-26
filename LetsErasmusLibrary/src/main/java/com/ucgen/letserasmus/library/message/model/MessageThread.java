package com.ucgen.letserasmus.library.message.model;

import java.util.ArrayList;
import java.util.List;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class MessageThread extends BaseModel {

	private static final long serialVersionUID = -4103137018298859208L;

	private Long id;
	private Long hostUserId;
	private Long clientUserId;
	private String threadTitle;
	private Integer entityType;
	private Long entityId;
	
	private User hostUser;
	private User clientUser;
	private Object entity;
	
	private List<Message> messageList;
	
	public List<Message> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getHostUserId() {
		return hostUserId;
	}
	public void setHostUserId(Long hostUserId) {
		this.hostUserId = hostUserId;
	}
	public Long getClientUserId() {
		return clientUserId;
	}
	public void setClientUserId(Long clientUserId) {
		this.clientUserId = clientUserId;
	}
	public String getThreadTitle() {
		return threadTitle;
	}
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	public Integer getEntityType() {
		return entityType;
	}
	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public User getHostUser() {
		return hostUser;
	}
	public void setHostUser(User hostUser) {
		this.hostUser = hostUser;
	}
	public User getClientUser() {
		return clientUser;
	}
	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	public void addMessage(Message message) {
		if (message != null) {
			if (this.messageList == null) {
				this.messageList = new ArrayList<Message>();
			}
			this.messageList.add(message);
		}
	}
}
