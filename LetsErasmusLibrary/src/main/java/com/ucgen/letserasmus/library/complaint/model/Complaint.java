package com.ucgen.letserasmus.library.complaint.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class Complaint extends BaseModel {

	private static final long serialVersionUID = 3578621113920004447L;

	private Long id;
	private Long userId;	
	private Long hostUserId;
	private Integer entityType;
	private Long entityId;
	private String description;
	private Integer status;
	
	private User user;
	private User hostUser;
	private BaseModel entity;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getHostUserId() {
		return hostUserId;
	}
	public void setHostUserId(Long hostUserId) {
		this.hostUserId = hostUserId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getHostUser() {
		return hostUser;
	}
	public void setHostUser(User hostUser) {
		this.hostUser = hostUser;
	}
	public BaseModel getEntity() {
		return entity;
	}
	public void setEntity(BaseModel entity) {
		this.entity = entity;
	}
	
}
