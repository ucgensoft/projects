package com.ucgen.letserasmus.web.api.payment;

public class UiPaymentMethod {

	private String cardHolderFirstName;
	private String cardHolderLastName;
	private String zipCode;
	private String cardInfoToken;
		
	public String getCardHolderFirstName() {
		return cardHolderFirstName;
	}
	public void setCardHolderFirstName(String cardHolderFirstName) {
		this.cardHolderFirstName = cardHolderFirstName;
	}
	public String getCardHolderLastName() {
		return cardHolderLastName;
	}
	public void setCardHolderLastName(String cardHolderLastName) {
		this.cardHolderLastName = cardHolderLastName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCardInfoToken() {
		return cardInfoToken;
	}
	public void setCardInfoToken(String cardInfoToken) {
		this.cardInfoToken = cardInfoToken;
	}
	
}