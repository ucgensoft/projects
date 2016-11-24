package com.ucgen.letserasmus.library.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class UserRowMapper extends BaseRowMapper<User> {

	public static final String COL_EMAIL = "EMAIL";
	public static final String COL_PASSWORD = "PASSWORD";	
	public static final String COL_MSISDN = "MSISDN";
	public static final String COL_FIRST_NAME = "FIRST_NAME";
	public static final String COL_LAST_NAME = "LAST_NAME";
	public static final String COL_STATUS = "STATUS";	
	public static final String COL_EMAIL_VERIFIED = "EMAIL_VERIFIED";
	public static final String COL_MSISDN_VERIFIED = "MSISDN_VERIFIED";
	public static final String COL_USER_ACTIVATION_KEY_EMAIL = "USER_ACTIVATION_KEY_EMAIL";
	public static final String COL_USER_ACTIVATION_KEY_MSISDN = "USER_ACTIVATION_KEY_MSISDN";
	public static final String COL_FILE_ID = "USER_FILE_ID";
	public static final String COL_FACEBOOK_TOKEN_ID = "FACEBOOK_TOKEN_ID";
	public static final String COL_IP = "IP";	
	public static final String COL_CREATED_BY = "CREATED_BY";
	public static final String COL_CREATED_DATE = "CREATED_DATE";
	public static final String COL_CREATED_DATE_GMT = "CREATED_DATE_GMT";
	public static final String COL_MODIFIED_BY = "MODIFIED_BY";
	public static final String COL_MODIFIED_DATE = "MODIFIED_DATE";
	public static final String COL_MODIFIED_DATE_GMT = "MODIFIED_DATE_GMT";


	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		User user = new User();		
		user.setEmail(super.getString(rs, COL_EMAIL));
		user.setPassword(super.getString(rs, COL_PASSWORD));
		user.setMsisdn(super.getString(rs, COL_MSISDN));
		user.setFirstName(super.getString(rs, COL_FIRST_NAME));
		user.setLastName(super.getString(rs, COL_LAST_NAME));
		user.setStatus(super.getInteger(rs, COL_STATUS));
		user.setEmailVerified(super.getInteger(rs, COL_EMAIL_VERIFIED));
		user.setMsisdnVerified(super.getInteger(rs, COL_MSISDN_VERIFIED));
		user.setUserActivationKeyEmail(super.getString(rs, COL_USER_ACTIVATION_KEY_EMAIL));
		user.setUserActivationKeyMsisdn(super.getString(rs, COL_USER_ACTIVATION_KEY_MSISDN));
		user.setFileId(super.getLong(rs, COL_FILE_ID));
		user.setFacebookTokenId(super.getString(rs, COL_FACEBOOK_TOKEN_ID));
		user.setIp(super.getString(rs, COL_IP));
		user.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		user.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		user.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		user.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		user.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		user.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		return user;
	}

	@Override
	public void fillFieldMaps() {
		
		
	}

}
