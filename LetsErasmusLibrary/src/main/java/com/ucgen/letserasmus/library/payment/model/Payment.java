package com.ucgen.letserasmus.library.payment.model;

import java.math.BigDecimal;

public class Payment {

	private BigDecimal entityPrice;
	private BigDecimal commissionFee;
	private BigDecimal serviceFee;
	private Integer currencyId;
	private String currencyCode;
	private String cardInfoToken;
	private String blueSnapTransactionId;
	private String merchantTransactionId;
	
	public BigDecimal getEntityPrice() {
		return entityPrice;
	}
	public void setEntityPrice(BigDecimal entityPrice) {
		this.entityPrice = entityPrice;
	}
	public BigDecimal getCommissionFee() {
		return commissionFee;
	}
	public void setCommissionFee(BigDecimal commissionFee) {
		this.commissionFee = commissionFee;
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
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCardInfoToken() {
		return cardInfoToken;
	}
	public void setCardInfoToken(String cardInfoToken) {
		this.cardInfoToken = cardInfoToken;
	}
	public String getBlueSnapTransactionId() {
		return blueSnapTransactionId;
	}
	public void setBlueSnapTransactionId(String blueSnapTransactionId) {
		this.blueSnapTransactionId = blueSnapTransactionId;
	}
	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}
	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}
	
}
