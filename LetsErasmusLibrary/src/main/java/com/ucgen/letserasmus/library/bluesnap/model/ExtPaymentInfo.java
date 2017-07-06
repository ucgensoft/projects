package com.ucgen.letserasmus.library.bluesnap.model;

import java.math.BigDecimal;

public class ExtPaymentInfo {

	private BigDecimal amount;
	private String recurringTransaction;
	private String merchantTransactionId;
	private String currency;
	private String cardTransactionType;
	private String pfToken;
	private ExtCardHolderInfo cardHolderInfo;
	private ExtVendorInfo vendorInfo;
	private String transactionId;
	private ExtTransactionFraudInfo transactionFraudInfo; 
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRecurringTransaction() {
		return recurringTransaction;
	}
	public void setRecurringTransaction(String recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}
	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}
	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCardTransactionType() {
		return cardTransactionType;
	}
	public void setCardTransactionType(String cardTransactionType) {
		this.cardTransactionType = cardTransactionType;
	}
	public String getPfToken() {
		return pfToken;
	}
	public void setPfToken(String pfToken) {
		this.pfToken = pfToken;
	}
	public ExtCardHolderInfo getCardHolderInfo() {
		return cardHolderInfo;
	}
	public void setCardHolderInfo(ExtCardHolderInfo cardHolderInfo) {
		this.cardHolderInfo = cardHolderInfo;
	}
	public ExtVendorInfo getVendorInfo() {
		return vendorInfo;
	}
	public void setVendorInfo(ExtVendorInfo vendorInfo) {
		this.vendorInfo = vendorInfo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public ExtTransactionFraudInfo getTransactionFraudInfo() {
		return transactionFraudInfo;
	}
	public void setTransactionFraudInfo(ExtTransactionFraudInfo transactionFraudInfo) {
		this.transactionFraudInfo = transactionFraudInfo;
	}
	
}
