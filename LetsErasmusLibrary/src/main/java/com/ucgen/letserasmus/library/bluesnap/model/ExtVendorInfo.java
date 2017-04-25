package com.ucgen.letserasmus.library.bluesnap.model;

import java.math.BigDecimal;

public class ExtVendorInfo {

	private Long vendorId;
	private String email;
	private String country;
	private BigDecimal commissionAmount;
	private ExtVendorPrincipal vendorPrincipal;
	private ExtVendorAgreement vendorAgreement;
	private ExtPayoutInfo payoutInfo;
	
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

	public ExtVendorPrincipal getVendorPrincipal() {
		return vendorPrincipal;
	}

	public void setVendorPrincipal(ExtVendorPrincipal vendorPrincipal) {
		this.vendorPrincipal = vendorPrincipal;
	}

	public ExtVendorAgreement getVendorAgreement() {
		return vendorAgreement;
	}

	public void setVendorAgreement(ExtVendorAgreement vendorAgreement) {
		this.vendorAgreement = vendorAgreement;
	}

	public ExtPayoutInfo getPayoutInfo() {
		return payoutInfo;
	}

	public void setPayoutInfo(ExtPayoutInfo payoutInfo) {
		this.payoutInfo = payoutInfo;
	}
	
}
