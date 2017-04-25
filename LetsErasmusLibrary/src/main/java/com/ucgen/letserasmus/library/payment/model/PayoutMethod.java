package com.ucgen.letserasmus.library.payment.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class PayoutMethod extends BaseModel {

	private static final long serialVersionUID = -632106142104147614L;

	private Long id;
	private Long userId;
	private Long blueSnapVendorId;
	private String payoutType;
	private BigDecimal commissionPercent;
	private String baseCurrency;
	
	private Long vendorNationalId;
	private Long vendorTaxId;
	private String vendorFirstName;
	private String vendorLastName;
	private String vendorEmail;
	private Date vendorBirthDate;
	private String vendorAddress;
	private String vendorAddress2;
	private String vendorCountry;
	private String vendorCity;
	private String vendorState;
	private String vendorZip;
	
	private Long bankAccountId;
	private String bankAccountHolderName;
	private String bankAccountClass;
	private String bankAccountType;
	private String bankAccountIban;
	
	private String bankName;
	private Long bankId;
	private String bankCountry;
	private String bankState;
	private String bankSwiftBic;
	
	public PayoutMethod() {
		this(null);
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

	public Long getBlueSnapVendorId() {
		return blueSnapVendorId;
	}

	public void setBlueSnapVendorId(Long blueSnapVendorId) {
		this.blueSnapVendorId = blueSnapVendorId;
	}

	public String getPayoutType() {
		return payoutType;
	}

	public void setPayoutType(String payoutType) {
		this.payoutType = payoutType;
	}

	public BigDecimal getCommissionPercent() {
		return commissionPercent;
	}

	public void setCommissionPercent(BigDecimal commissionPercent) {
		this.commissionPercent = commissionPercent;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Long getVendorNationalId() {
		return vendorNationalId;
	}

	public void setVendorNationalId(Long vendorNationalId) {
		this.vendorNationalId = vendorNationalId;
	}

	public Long getVendorTaxId() {
		return vendorTaxId;
	}

	public void setVendorTaxId(Long vendorTaxId) {
		this.vendorTaxId = vendorTaxId;
	}

	public String getVendorFirstName() {
		return vendorFirstName;
	}

	public void setVendorFirstName(String vendorFirstName) {
		this.vendorFirstName = vendorFirstName;
	}

	public String getVendorLastName() {
		return vendorLastName;
	}

	public void setVendorLastName(String vendorLastName) {
		this.vendorLastName = vendorLastName;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public Date getVendorBirthDate() {
		return vendorBirthDate;
	}

	public void setVendorBirthDate(Date vendorBirthDate) {
		this.vendorBirthDate = vendorBirthDate;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getVendorAddress2() {
		return vendorAddress2;
	}

	public void setVendorAddress2(String vendorAddress2) {
		this.vendorAddress2 = vendorAddress2;
	}

	public String getVendorCountry() {
		return vendorCountry;
	}

	public void setVendorCountry(String vendorCountry) {
		this.vendorCountry = vendorCountry;
	}

	public String getVendorCity() {
		return vendorCity;
	}

	public void setVendorCity(String vendorCity) {
		this.vendorCity = vendorCity;
	}

	public String getVendorState() {
		return vendorState;
	}

	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}

	public String getVendorZip() {
		return vendorZip;
	}

	public void setVendorZip(String vendorZip) {
		this.vendorZip = vendorZip;
	}

	public Long getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getBankAccountHolderName() {
		return bankAccountHolderName;
	}

	public void setBankAccountHolderName(String bankAccountHolderName) {
		this.bankAccountHolderName = bankAccountHolderName;
	}

	public String getBankAccountClass() {
		return bankAccountClass;
	}

	public void setBankAccountClass(String bankAccountClass) {
		this.bankAccountClass = bankAccountClass;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankAccountIban() {
		return bankAccountIban;
	}

	public void setBankAccountIban(String bankAccountIban) {
		this.bankAccountIban = bankAccountIban;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankCountry() {
		return bankCountry;
	}

	public void setBankCountry(String bankCountry) {
		this.bankCountry = bankCountry;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState;
	}

	public String getBankSwiftBic() {
		return bankSwiftBic;
	}

	public void setBankSwiftBic(String bankSwiftBic) {
		this.bankSwiftBic = bankSwiftBic;
	}
	
}
