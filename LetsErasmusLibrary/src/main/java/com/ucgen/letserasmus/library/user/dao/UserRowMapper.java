package com.ucgen.letserasmus.library.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.file.dao.FileRowMapper;
import com.ucgen.letserasmus.library.file.model.File;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class UserRowMapper extends BaseRowMapper<User> {

	public static final String TABLE_NAME = "USER";
	
	public static final String COL_ID = "ID";
	public static final String COL_EMAIL = "EMAIL";
	public static final String COL_PASSWORD = "PASSWORD";	
	public static final String COL_MSISDN = "MSISDN";
	public static final String COL_FIRST_NAME = "FIRST_NAME";
	public static final String COL_LAST_NAME = "LAST_NAME";
	public static final String COL_GENDER = "GENDER";
	public static final String COL_STATUS = "STATUS";	
	public static final String COL_EMAIL_VERIFIED = "EMAIL_VERIFIED";
	public static final String COL_MSISDN_VERIFIED = "MSISDN_VERIFIED";
	public static final String COL_USER_ACTIVATION_KEY_EMAIL = "USER_ACTIVATION_KEY_EMAIL";
	public static final String COL_USER_ACTIVATION_KEY_MSISDN = "USER_ACTIVATION_KEY_MSISDN";
	public static final String COL_PROFILE_PHOTO_ID = "PROFILE_PHOTO_ID";
	public static final String COL_FACEBOOK_TOKEN_ID = "FACEBOOK_TOKEN_ID";
	public static final String COL_IP = "IP";	
	public static final String COL_CREATED_BY = "CREATED_BY";
	public static final String COL_CREATED_DATE = "CREATED_DATE";
	public static final String COL_CREATED_DATE_GMT = "CREATED_DATE_GMT";
	public static final String COL_MODIFIED_BY = "MODIFIED_BY";
	public static final String COL_MODIFIED_DATE = "MODIFIED_DATE";
	public static final String COL_MODIFIED_DATE_GMT = "MODIFIED_DATE_GMT";
	public static final String COL_GOOGLE_ID = "GOOGLE_ID";
	public static final String COL_FACEBOOK_ID = "FACEBOOK_ID";
	public static final String COL_JOB_TITLE = "JOB_TITLE";
	public static final String COL_SCHOOL_NAME = "SCHOOL_NAME";
	public static final String COL_BIRTH_DATE = "BIRTH_DATE";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_RESIDENCE_LOCATION_NAME = "RESIDENCE_LOCATION_NAME";
	public static final String COL_LANGUAGES = "LANGUAGES";
	
	public static final String FKEY_FILE = "FILE";

	public void addFKey(String keyName) {		
		if (FKEY_FILE.equals(keyName)) {
			FileRowMapper fileRowMapper = new FileRowMapper("F");
			ForeignKey<UserRowMapper, FileRowMapper> fKeyFile = new ForeignKey<UserRowMapper, FileRowMapper>(this, fileRowMapper, EnmJoinType.LEFT);
			fKeyFile.addFieldPair(COL_PROFILE_PHOTO_ID, FileRowMapper.COL_ID);
			this.addFKey(FKEY_FILE, fKeyFile);
		}
	}
	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		User user = new User();		
		user.setId(super.getLong(rs, COL_ID));
		user.setEmail(super.getString(rs, COL_EMAIL));
		user.setPassword(super.getString(rs, COL_PASSWORD));
		user.setMsisdn(super.getString(rs, COL_MSISDN));
		user.setFirstName(super.getString(rs, COL_FIRST_NAME));
		user.setLastName(super.getString(rs, COL_LAST_NAME));
		user.setGender(super.getString(rs, COL_GENDER));
		user.setStatus(super.getInteger(rs, COL_STATUS));
		user.setEmailVerified(super.getString(rs, COL_EMAIL_VERIFIED));
		user.setMsisdnVerified(super.getString(rs, COL_MSISDN_VERIFIED));
		user.setUserActivationKeyEmail(super.getString(rs, COL_USER_ACTIVATION_KEY_EMAIL));
		user.setUserActivationKeyMsisdn(super.getString(rs, COL_USER_ACTIVATION_KEY_MSISDN));
		user.setProfilePhotoId(super.getLong(rs, COL_PROFILE_PHOTO_ID));
		user.setFacebookTokenId(super.getString(rs, COL_FACEBOOK_TOKEN_ID));
		user.setIp(super.getString(rs, COL_IP));
		user.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		user.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		user.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		user.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		user.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		user.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		user.setGoogleId(super.getString(rs, COL_GOOGLE_ID));
		user.setFacebookId(super.getString(rs, COL_FACEBOOK_ID));
		user.setJobTitle(super.getString(rs, COL_JOB_TITLE));
		user.setSchoolName(super.getString(rs, COL_SCHOOL_NAME));
		user.setBirthDate(super.getTimestamp(rs, COL_BIRTH_DATE));
		user.setDescription(super.getString(rs, COL_DESCRIPTION));
		user.setResidenceLocationName(super.getString(rs, COL_RESIDENCE_LOCATION_NAME));
		user.setLanguages(super.getString(rs, COL_LANGUAGES));
		
		if (user.getProfilePhotoId() != null && this.getfKeyMap().containsKey(FKEY_FILE)) {
			ForeignKey<PlaceRowMapper, FileRowMapper> fKey = this.getfKeyMap().get(FKEY_FILE);
			File photo = fKey.getDestMapper().mapRow(rs, rowNum);
			user.setProfilePhoto(photo);
		}
		
		return user;
	}

	public UserRowMapper() {
		this(null);
	}
	
	public UserRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public void fillFieldMaps() {		
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_CREATED_BY);
		super.addColumn(COL_CREATED_DATE);
		super.addColumn(COL_CREATED_DATE_GMT);
		super.addColumn(COL_EMAIL);
		super.addColumn(COL_EMAIL_VERIFIED);
		super.addColumn(COL_FACEBOOK_TOKEN_ID);
		super.addColumn(COL_PROFILE_PHOTO_ID);
		super.addColumn(COL_FIRST_NAME);
		super.addColumn(COL_IP);
		super.addColumn(COL_LAST_NAME);
		super.addColumn(COL_MODIFIED_BY);
		super.addColumn(COL_MODIFIED_DATE);
		super.addColumn(COL_MODIFIED_DATE_GMT);
		super.addColumn(COL_MSISDN);
		super.addColumn(COL_MSISDN_VERIFIED);
		super.addColumn(COL_PASSWORD);
		super.addColumn(COL_STATUS);
		super.addColumn(COL_USER_ACTIVATION_KEY_EMAIL);
		super.addColumn(COL_USER_ACTIVATION_KEY_MSISDN);
		super.addColumn(COL_GENDER);
		super.addColumn(COL_FACEBOOK_ID);
		super.addColumn(COL_GOOGLE_ID);
		super.addColumn(COL_JOB_TITLE);
		super.addColumn(COL_SCHOOL_NAME);
		super.addColumn(COL_BIRTH_DATE);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_RESIDENCE_LOCATION_NAME);
		super.addColumn(COL_LANGUAGES);
		super.addColumn(COL_ID);
	}

}
