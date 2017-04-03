package com.ucgen.letserasmus.web.api.message;

import java.util.Date;
import java.util.List;

import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.reservation.model.Reservation;

public class UiMessageThread {

	private Long id;
	private String threadTitle;
	private Long activeUserId;
	private Long hostUserId;
	private String hostUserProfilePhotoUrl;
	private String hostUserProfilePhotoUrlMedium;
	private String hostUserFirstName;
	
	private Long clientUserId;
	private String clientUserProfilePhotoUrl;
	private String clientUserProfilePhotoUrlMedium;
	private String clientUserFirstName;
	
	private String messageText;
	private Date messageDate;
	
	private Reservation reservation;
	private String reservationPriceText;
	
	private List<UiMessage> messageList;
	private List<TransactionLog> transactionLogList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getThreadTitle() {
		return threadTitle;
	}
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	public Long getActiveUserId() {
		return activeUserId;
	}
	public void setActiveUserId(Long activeUserId) {
		this.activeUserId = activeUserId;
	}
	public Long getHostUserId() {
		return hostUserId;
	}
	public void setHostUserId(Long hostUserId) {
		this.hostUserId = hostUserId;
	}
	public String getHostUserProfilePhotoUrl() {
		return hostUserProfilePhotoUrl;
	}
	public void setHostUserProfilePhotoUrl(String hostUserProfilePhotoUrl) {
		this.hostUserProfilePhotoUrl = hostUserProfilePhotoUrl;
	}
	public String getHostUserProfilePhotoUrlMedium() {
		return hostUserProfilePhotoUrlMedium;
	}
	public void setHostUserProfilePhotoUrlMedium(String hostUserProfilePhotoUrlMedium) {
		this.hostUserProfilePhotoUrlMedium = hostUserProfilePhotoUrlMedium;
	}
	public String getHostUserFirstName() {
		return hostUserFirstName;
	}
	public void setHostUserFirstName(String hostUserFirstName) {
		this.hostUserFirstName = hostUserFirstName;
	}
	public Long getClientUserId() {
		return clientUserId;
	}
	public void setClientUserId(Long clientUserId) {
		this.clientUserId = clientUserId;
	}
	public String getClientUserProfilePhotoUrl() {
		return clientUserProfilePhotoUrl;
	}
	public void setClientUserProfilePhotoUrl(String clientUserProfilePhotoUrl) {
		this.clientUserProfilePhotoUrl = clientUserProfilePhotoUrl;
	}
	public String getClientUserProfilePhotoUrlMedium() {
		return clientUserProfilePhotoUrlMedium;
	}
	public void setClientUserProfilePhotoUrlMedium(String clientUserProfilePhotoUrlMedium) {
		this.clientUserProfilePhotoUrlMedium = clientUserProfilePhotoUrlMedium;
	}
	public String getClientUserFirstName() {
		return clientUserFirstName;
	}
	public void setClientUserFirstName(String clientUserFirstName) {
		this.clientUserFirstName = clientUserFirstName;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public Date getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	public String getReservationPriceText() {
		return reservationPriceText;
	}
	public void setReservationPriceText(String reservationPriceText) {
		this.reservationPriceText = reservationPriceText;
	}
	public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	public List<UiMessage> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<UiMessage> messageList) {
		this.messageList = messageList;
	}
	public List<TransactionLog> getTransactionLogList() {
		return transactionLogList;
	}
	public void setTransactionLogList(List<TransactionLog> transactionLogList) {
		this.transactionLogList = transactionLogList;
	}
	
}
