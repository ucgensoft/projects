package com.ucgen.letserasmus.web.api.reservation;

import java.util.Date;
import java.util.List;

import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.web.api.BaseUiModel;
import com.ucgen.letserasmus.web.api.payment.UiPaymentMethod;

public class UiReservation extends BaseUiModel {

	private Long id;
	private Long placeId;
	private Date startDate;
	private Date endDate;
	private Integer guestNumber;
	private Integer status;
	private String cardInfoToken;
	
	private UiPaymentMethod uiPaymentMethod;
	
	private String messageText;
	private List<Review> reviewList;
	
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

	public String getCardInfoToken() {
		return cardInfoToken;
	}

	public void setCardInfoToken(String cardInfoToken) {
		this.cardInfoToken = cardInfoToken;
	}

	public List<Review> getReviewList() {
		return reviewList;
	}

	public void setReviewList(List<Review> reviewList) {
		this.reviewList = reviewList;
	}

	public UiPaymentMethod getUiPaymentMethod() {
		return uiPaymentMethod;
	}

	public void setUiPaymentMethod(UiPaymentMethod uiPaymentMethod) {
		this.uiPaymentMethod = uiPaymentMethod;
	}
	
}
