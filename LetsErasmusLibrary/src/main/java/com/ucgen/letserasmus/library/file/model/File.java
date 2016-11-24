package com.ucgen.letserasmus.library.file.model;

import java.util.Date;

public class File {

	private Long id;
	private String fileName;
	private int fileType;
	private int entityType;
	private Long entityId;
	private String createdBy;
	private Date createdDate;
	private Date createdDateGmt;
	private String modifiedBy;
	private Date modifiedDate;
	private Date modifiedDateGmt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
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
	public Date getCreatedDateGmt() {
		return createdDateGmt;
	}
	public void setCreatedDateGmt(Date createdDateGmt) {
		this.createdDateGmt = createdDateGmt;
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
	public Date getModifiedDateGmt() {
		return modifiedDateGmt;
	}
	public void setModifiedDateGmt(Date modifiedDateGmt) {
		this.modifiedDateGmt = modifiedDateGmt;
	}
	
	
	
}
