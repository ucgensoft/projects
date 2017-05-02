package com.ucgen.letserasmus.library.payment.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public class PayoutMethodRowMapper extends BaseRowMapper<PayoutMethod> {

	public static final String TABLE_NAME = "PAYMENT_METHOD";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_EXTERNAL_SYSTEM_ID = "EXTERNAL_SYSTEM_ID";
	public static final String COL_BLUESNAP_VENDOR_ID = "BLUESNAP_VENDOR_ID";
	public static final String COL_STRIPE_ACCOUNT_ID = "STRIPE_ACCOUNT_ID";
	
	public static final String COL_COMMISSION_PERCENT = "COMMISSION_PERCENT";
	public static final String COL_PAYOUT_TYPE = "PAYOUT_TYPE";
	public static final String COL_BASE_CURRENCY = "BASE_CURRENCY";
	
	public static final String COL_VENDOR_ENTITY_TYPE = "VENDOR_ENTITY_TYPE";
	public static final String COL_VENDOR_NATIONAL_ID = "VENDOR_NATIONAL_ID";
	public static final String COL_VENDOR_TAXID = "VENDOR_TAXID";
	public static final String COL_VENDOR_FIRST_NAME = "VENDOR_FIRST_NAME";
	public static final String COL_VENDOR_LAST_NAME = "VENDOR_LAST_NAME";
	public static final String COL_VENDOR_EMAIL = "VENDOR_EMAIL";
	public static final String COL_VENDOR_BIRTH_DATE = "VENDOR_BIRTH_DATE";
	public static final String COL_VENDOR_ADDRESS = "VENDOR_ADDRESS";
	public static final String COL_VENDOR_ADDRESS2 = "VENDOR_ADDRESS2";
	public static final String COL_VENDOR_COUNTRY = "VENDOR_COUNTRY";
	public static final String COL_VENDOR_CITY = "VENDOR_CITY";
	public static final String COL_VENDOR_STATE = "VENDOR_STATE";
	public static final String COL_VENDOR_ZIP = "VENDOR_ZIP";
	
	
	public static final String COL_BANK_ACCOUNT_ID = "BANK_ACCOUNT_ID";
	public static final String COL_BANK_ACCOUNT_IBAN = "BANK_ACCOUNT_IBAN";
	public static final String COL_BANK_ACCOUNT_HOLDER_NAME = "BANK_ACCOUNT_HOLDER_NAME";
	public static final String COL_BANK_ACCOUNT_CLASS = "BANK_ACCOUNT_CLASS";
	public static final String COL_BANK_ACCOUNT_TYPE = "BANK_ACCOUNT_TYPE";
	
	public static final String COL_BANK_NAME = "BANK_NAME";
	public static final String COL_BANK_ID = "BANK_ID";
	public static final String COL_BANK_COUNTRY = "BANK_COUNTRY";
	public static final String COL_BANK_STATE = "BANK_STATE";
	public static final String COL_BANK_SWIFT_BIC = "BANK_SWIFT_BIC";
			
	public PayoutMethodRowMapper() {
		this("PO");
	}
	
	public PayoutMethodRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
		
	@Override
	public PayoutMethod mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		PayoutMethod payoutMethod = new PayoutMethod();		
		
		payoutMethod.setId(super.getLong(rs, COL_ID));		
		payoutMethod.setUserId(super.getLong(rs, COL_USER_ID));
		payoutMethod.setExternalSystemId(super.getInteger(rs, COL_EXTERNAL_SYSTEM_ID));
		payoutMethod.setBlueSnapVendorId(super.getLong(rs, COL_BLUESNAP_VENDOR_ID));
		payoutMethod.setStripeAccountId(super.getString(rs, COL_STRIPE_ACCOUNT_ID));
		
		payoutMethod.setVendorEntityType(super.getString(rs, COL_VENDOR_ENTITY_TYPE));
		payoutMethod.setVendorNationalId(super.getLong(rs, COL_VENDOR_NATIONAL_ID));
		payoutMethod.setVendorTaxId(super.getLong(rs, COL_VENDOR_TAXID));
		payoutMethod.setVendorFirstName(super.getString(rs, COL_VENDOR_FIRST_NAME));
		payoutMethod.setVendorLastName(super.getString(rs, COL_VENDOR_LAST_NAME));
		payoutMethod.setVendorEmail(super.getString(rs, COL_VENDOR_EMAIL));
		payoutMethod.setVendorEmail(super.getString(rs, COL_VENDOR_EMAIL));
		payoutMethod.setVendorBirthDate(super.getTimestamp(rs, COL_VENDOR_BIRTH_DATE));
		payoutMethod.setVendorAddress(super.getString(rs, COL_VENDOR_ADDRESS));
		payoutMethod.setVendorAddress2(super.getString(rs, COL_VENDOR_ADDRESS2));
		payoutMethod.setVendorCountry(super.getString(rs, COL_VENDOR_COUNTRY));
		payoutMethod.setVendorCity(super.getString(rs, COL_VENDOR_CITY));
		payoutMethod.setVendorState(super.getString(rs, COL_VENDOR_STATE));
		payoutMethod.setVendorZip(super.getString(rs, COL_VENDOR_ZIP));
		
		payoutMethod.setBankAccountId(super.getLong(rs, COL_BANK_ACCOUNT_ID));
		payoutMethod.setBankAccountIban(super.getString(rs, COL_BANK_ACCOUNT_IBAN));
		payoutMethod.setBankAccountHolderName(super.getString(rs, COL_BANK_ACCOUNT_HOLDER_NAME));
		payoutMethod.setBankAccountHolderName(super.getString(rs, COL_BANK_ACCOUNT_HOLDER_NAME));
		payoutMethod.setBankAccountClass(super.getString(rs, COL_BANK_ACCOUNT_CLASS));
		payoutMethod.setBankAccountType(super.getString(rs, COL_BANK_ACCOUNT_TYPE));
		
		payoutMethod.setBankName(super.getString(rs, COL_BANK_NAME));
		payoutMethod.setBankId(super.getLong(rs, COL_BANK_ID));
		payoutMethod.setBankCountry(super.getString(rs, COL_BANK_COUNTRY));
		payoutMethod.setBankState(super.getString(rs, COL_BANK_STATE));
		payoutMethod.setBankSwiftBic(super.getString(rs, COL_BANK_SWIFT_BIC));
		
		payoutMethod.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		payoutMethod.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		payoutMethod.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		payoutMethod.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		payoutMethod.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		payoutMethod.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return payoutMethod;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
			
	}
	
}
