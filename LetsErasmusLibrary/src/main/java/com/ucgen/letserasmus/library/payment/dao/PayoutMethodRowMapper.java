package com.ucgen.letserasmus.library.payment.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public class PayoutMethodRowMapper extends BaseRowMapper<PayoutMethod> {

	public static final String TABLE_NAME = "PAYMENT_METHOD";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_EMAIL = "EMAIL";
	public static final String COL_BLUESNAP_COUNTRY_CODE = "BLUESNAP_COUNTRY_CODE";
	public static final String COL_BLUESNAP_VENDOR_ID = "BLUESNAP_VENDOR_ID";
	public static final String COL_IBAN = "IBAN";
		
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
		payoutMethod.setEmail(super.getString(rs, COL_EMAIL));
		payoutMethod.setBlueSnapCountryCode(super.getString(rs, COL_BLUESNAP_COUNTRY_CODE));
		payoutMethod.setBlueSnapVendorId(super.getLong(rs, COL_BLUESNAP_VENDOR_ID));
		payoutMethod.setIban(super.getString(rs, COL_IBAN));
		
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
		super.addColumn(COL_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_EMAIL);
		super.addColumn(COL_BLUESNAP_COUNTRY_CODE);
		super.addColumn(COL_BLUESNAP_VENDOR_ID);		
		super.addColumn(COL_IBAN);
		super.addColumn(COL_CREATED_BY);
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
