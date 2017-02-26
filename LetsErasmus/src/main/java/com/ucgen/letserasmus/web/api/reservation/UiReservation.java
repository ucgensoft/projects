package com.ucgen.letserasmus.web.api.reservation;

import java.util.Date;

public class UiReservation {

	private Long id;
	private Long placeId;
	private Date startDate;
	private Date endDate;
	private Integer guestNumber;
	private Integer status;
	
	private String messageText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getGuestNumber() {
		return guestNumber;
	}

	public void setGuestNumber(Integer guestNumber) {
		this.guestNumber = guestNumber;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}