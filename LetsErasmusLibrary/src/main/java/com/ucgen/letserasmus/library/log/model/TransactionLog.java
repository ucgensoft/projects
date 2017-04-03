package com.ucgen.letserasmus.library.log.model;

import java.util.Date;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class TransactionLog extends BaseModel {

	private static final long serialVersionUID = 8109232846665365455L;

	private Long id;
	private Long userId;	
	private Integer operationId;
	private Date operationDate;
	private Integer entityType;
	private Long entityId;
	
	private User user;
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
	public Integer getOperationId() {
		return operationId;
	}
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	public Date getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BaseModel getEntity() {
		return entity;
	}
	public void setEntity(BaseModel entity) {
		this.entity = entity;
	}
	
}
