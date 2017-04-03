package com.ucgen.letserasmus.library.log.model;

import java.util.Date;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class IntegrationLog extends BaseModel {

	private static final long serialVersionUID = 8109232846665365455L;

	private Long id;
	private Integer extSystemId;
	private Long userId;	
	private Integer operationId;
	private Date operationDate;
	private Long duration;
	private String request;
	private String response;
	private String responseCode;
	
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getExtSystemId() {
		return extSystemId;
	}

	public void setExtSystemId(Integer extSystemId) {
		this.extSystemId = extSystemId;
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

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
