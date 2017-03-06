package com.ucgen.letserasmus.library.review.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class Review extends BaseModel {

	private static final long serialVersionUID = 8109232846665365455L;

	private Long id;
	private Long userId;	
	private Long reviewedUserId;
	private Integer entityType;
	private Long entityId;
	private Integer rank;
	private String description;
	
	private User user;
	private User reviewedUser;
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
	public Long getReviewedUserId() {
		return reviewedUserId;
	}
	public void setReviewedUserId(Long reviewedUserId) {
		this.reviewedUserId = reviewedUserId;
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
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
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
	public User getReviewedUser() {
		return reviewedUser;
	}
	public void setReviewedUser(User reviewedUser) {
		this.reviewedUser = reviewedUser;
	}
	public BaseModel getEntity() {
		return entity;
	}
	public void setEntity(BaseModel entity) {
		this.entity = entity;
	}
	
}
