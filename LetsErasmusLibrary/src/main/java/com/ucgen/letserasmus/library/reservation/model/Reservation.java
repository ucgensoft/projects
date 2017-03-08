package com.ucgen.letserasmus.library.reservation.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.user.model.User;

public class Reservation extends BaseModel {

	private static final long serialVersionUID = 4311807176239691534L;

	private Long id;
	private Long placeId;
	private Long hostUserId;
	private Long clientUserId;
	private Date startDate;
	private Date endDate;
	private Integer guestNumber;
	private BigDecimal placePrice;
	private BigDecimal commissionRate;
	private BigDecimal commissionFee;
	private BigDecimal serviceRate;
	private BigDecimal serviceFee;
	private Integer currencyId;
	private Integer status;
	private Long messageThreadId;
	private Long clientReviewId;
	private Long hostReviewId;
	
	private Place place;
	private User hostUser;
	private User clientUser;
	
	private MessageThread messageThread;
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
	public BigDecimal getPlacePrice() {
		return placePrice;
	}
	public void setPlacePrice(BigDecimal placePrice) {
		this.placePrice = placePrice;
	}
	public BigDecimal getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}
	public BigDecimal getCommissionFee() {
		return commissionFee;
	}
	public void setCommissionFee(BigDecimal commissionFee) {
		this.commissionFee = commissionFee;
	}
	public BigDecimal getServiceRate() {
		return serviceRate;
	}
	public void setServiceRate(BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}
	public BigDecimal getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getMessageThreadId() {
		return messageThreadId;
	}
	public void setMessageThreadId(Long messageThreadId) {
		this.messageThreadId = messageThreadId;
	}
	public Long getClientReviewId() {
		return clientReviewId;
	}
	public void setClientReviewId(Long clientReviewId) {
		this.clientReviewId = clientReviewId;
	}
	public Long getHostReviewId() {
		return hostReviewId;
	}
	public void setHostReviewId(Long hostReviewId) {
		this.hostReviewId = hostReviewId;
	}
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
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
	public MessageThread getMessageThread() {
		return messageThread;
	}
	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}
	public List<Review> getReviewList() {
		return reviewList;
	}
	public void setReviewList(List<Review> reviewList) {
		this.reviewList = reviewList;
	}
	
}
