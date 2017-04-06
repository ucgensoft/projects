package com.ucgen.letserasmus.library.payment.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class PaymentMethod extends BaseModel {

	private static final long serialVersionUID = 4310293962224338218L;

	private Long id;
	private Long userId;
	private Long blueSnapId;
	private String cardNumber;
	private String cardType;
	private String expDate;
	
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
	public Long getBlueSnapId() {
		return blueSnapId;
	}
	public void setBlueSnapId(Long blueSnapId) {
		this.blueSnapId = blueSnapId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	
}