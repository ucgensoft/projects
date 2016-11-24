package com.ucgen.letserasmus.library.comment.model;

import java.util.Date;

public class Comment {

	private Long id;
	private int entityType;
	private Long entityId;
	private Long authorUserId;
	private String commentContent;	
	private int status;
	private String createdBy;
	private Date createdDate;
	private Date createdDateGmt;
	private String modifiedBy;
	private Date modifiedDate;
	private Date modifiedDateGmt;
	
	public int getEntityType() {
		return entityType;
	}
	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public Long getAuthorUserId() {
		return authorUserId;
	}
	public void setAuthorUserId(Long authorUserId) {
		this.authorUserId = authorUserId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Date getCreatedDateGmt() {
		return createdDateGmt;
	}
	public void setCreatedDateGmt(Date createdDateGmt) {
		this.createdDateGmt = createdDateGmt;
	}
	public Date getModifiedDateGmt() {
		return modifiedDateGmt;
	}
	public void setModifiedDateGmt(Date modifiedDateGmt) {
		this.modifiedDateGmt = modifiedDateGmt;
	}
	
}
