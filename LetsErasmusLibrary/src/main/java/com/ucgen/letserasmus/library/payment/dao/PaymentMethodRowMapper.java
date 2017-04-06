package com.ucgen.letserasmus.library.payment.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;

public class PaymentMethodRowMapper extends BaseRowMapper<PaymentMethod> {

	public static final String TABLE_NAME = "PAYMENT_METHOD";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_CARD_NUMBER = "CARD_NUMBER";
	public static final String COL_CARD_TYPE = "CARD_TYPE";
	public static final String COL_EXP_DATE = "EXP_DATE";
	public static final String COL_BLUESNAP_ID = "BLUESNAP_ID";
		
	public PaymentMethodRowMapper() {
		this("PM");
	}
	
	public PaymentMethodRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
		
	@Override
	public PaymentMethod mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		PaymentMethod paymentMethod = new PaymentMethod();		
		
		paymentMethod.setId(super.getLong(rs, COL_ID));		
		paymentMethod.setUserId(super.getLong(rs, COL_USER_ID));
		paymentMethod.setCardNumber(super.getString(rs, COL_CARD_NUMBER));
		paymentMethod.setCardType(super.getString(rs, COL_CARD_TYPE));
		paymentMethod.setExpDate(super.getString(rs, COL_EXP_DATE));
		paymentMethod.setBlueSnapId(super.getLong(rs, COL_BLUESNAP_ID));
		
		paymentMethod.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		paymentMethod.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		paymentMethod.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		paymentMethod.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		paymentMethod.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		paymentMethod.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return paymentMethod;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_CARD_NUMBER);
		super.addColumn(COL_CARD_TYPE);
		super.addColumn(COL_EXP_DATE);		
		super.addColumn(COL_BLUESNAP_ID);
		super.addColumn(COL_CREATED_BY);
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
