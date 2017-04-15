package com.ucgen.letserasmus.library.bluesnap.model;

import java.math.BigDecimal;

public class ExtVendorInfo {

	private Long vendorId;
	private String email;
	private String country;
	private BigDecimal commissionAmount;
	
	public ExtVendorInfo() {
		
	}
	
	public ExtVendorInfo(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
}
