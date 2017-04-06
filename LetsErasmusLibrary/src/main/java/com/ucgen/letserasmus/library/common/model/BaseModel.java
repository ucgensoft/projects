package com.ucgen.letserasmus.library.common.model;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1318396349636631033L;

	private String ip;
	
	private String createdBy;
	private Date createdDate;
	private Date createdDateGmt;
	private String modifiedBy;
	private Date modifiedDate;
	private Date modifiedDateGmt;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
