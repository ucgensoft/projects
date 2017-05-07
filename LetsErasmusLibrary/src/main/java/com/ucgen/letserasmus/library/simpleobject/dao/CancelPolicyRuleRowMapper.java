package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;

public class CancelPolicyRuleRowMapper extends BaseRowMapper<CancelPolicyRule> {

	public static final String TABLE_NAME = "CANCEL_POLICY_RULE";
	
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_POLICY_ID = "POLICY_ID";
	public static final String COL_REMAINING_DAYS = "REMAINING_DAYS";
	public static final String COL_REFUND_RATE = "REFUND_RATE";
	
	public CancelPolicyRuleRowMapper() {
		this(null);
	}
	
	public CancelPolicyRuleRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public CancelPolicyRule mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		CancelPolicyRule cancelPolicyRule = new CancelPolicyRule();
		
		cancelPolicyRule.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		cancelPolicyRule.setPolicyId(super.getInteger(rs, COL_POLICY_ID));
		cancelPolicyRule.setRemainingDays(super.getInteger(rs, COL_REMAINING_DAYS));
		cancelPolicyRule.setRefundRate(super.getBigDecimal(rs, COL_REFUND_RATE));
		
		return cancelPolicyRule;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		
	}
	
}

