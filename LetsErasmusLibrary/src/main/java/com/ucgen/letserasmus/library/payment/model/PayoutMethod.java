package com.ucgen.letserasmus.library.payment.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class PayoutMethod extends BaseModel {

	private static final long serialVersionUID = -632106142104147614L;

	private Long id;
	private Long userId;
	private String email;
	private String blueSnapCountryCode;
	private Long blueSnapVendorId;
	private String iban;
	
	public PayoutMethod() {
		
	}
	
	public PayoutMethod(Long userId) {
		this.userId = userId;
	}
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBlueSnapCountryCode() {
		return blueSnapCountryCode;
	}
	public void setBlueSnapCountryCode(String blueSnapCountryCode) {
		this.blueSnapCountryCode = blueSnapCountryCode;
	}
	public Long getBlueSnapVendorId() {
		return blueSnapVendorId;
	}
	public void setBlueSnapVendorId(Long blueSnapVendorId) {
		this.blueSnapVendorId = blueSnapVendorId;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	
}
