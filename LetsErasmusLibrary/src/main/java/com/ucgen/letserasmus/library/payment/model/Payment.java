package com.ucgen.letserasmus.library.payment.model;

import java.math.BigDecimal;

public class Payment {

	private BigDecimal totalPrice;
	private BigDecimal commissionFee;
	private BigDecimal serviceFee;
	private Integer currencyId;
	private String blueSnapCurrencyCode;
	private String blueSnapHostedFieldToken;
	private String blueSnapTransactionId;
	private String merchantTransactionId;
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
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
	public String getBlueSnapCurrencyCode() {
		return blueSnapCurrencyCode;
	}
	public void setBlueSnapCurrencyCode(String blueSnapCurrencyCode) {
		this.blueSnapCurrencyCode = blueSnapCurrencyCode;
	}
	public String getBlueSnapHostedFieldToken() {
		return blueSnapHostedFieldToken;
	}
	public void setBlueSnapHostedFieldToken(String blueSnapHostedFieldToken) {
		this.blueSnapHostedFieldToken = blueSnapHostedFieldToken;
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
