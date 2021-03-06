package com.ucgen.letserasmus.library.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.file.dao.FileRowMapper;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class UserRowMapper extends BaseRowMapper<User> {

	public static final String TABLE_NAME = "USER";
	
	public static final String COL_ID = "ID";
	public static final String COL_EMAIL = "EMAIL";
	public static final String COL_PASSWORD = "PASSWORD";	
	public static final String COL_MSISDN = "MSISDN";
	public static final String COL_MSISDN_COUNTRY_CODE = "MSISDN_COUNTRY_CODE";
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
	public static final String COL_GOOGLE_ID = "GOOGLE_ID";
	public static final String COL_FACEBOOK_ID = "FACEBOOK_ID";
	public static final String COL_JOB_TITLE = "JOB_TITLE";
	public static final String COL_SCHOOL_NAME = "SCHOOL_NAME";
	public static final String COL_BIRTH_DATE = "BIRTH_DATE";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_RESIDENCE_LOCATION_NAME = "RESIDENCE_LOCATION_NAME";
	public static final String COL_LANGUAGES = "LANGUAGES";
	public static final String COL_GOOGLE_EMAIL = "GOOGLE_EMAIL";
	public static final String COL_FACEBOOK_EMAIL = "FACEBOOK_EMAIL";
	public static final String COL_RESET_PASSWORD_TOKEN = "RESET_PASSWORD_TOKEN";
	public static final String COL_PROFILE_TYPE_ID = "PROFILE_TYPE_ID";
	public static final String COL_HOME_COUNTRY_ID = "HOME_COUNTRY_ID";
	public static final String COL_HOME_CITY_ID = "HOME_CITY_ID";
	public static final String COL_HOME_UNIVERSITY_ID = "HOME_UNIVERSITY_ID";
	public static final String COL_ERASMUS_COUNTRY_ID = "ERASMUS_COUNTRY_ID";
	public static final String COL_ERASMUS_CITY_ID = "ERASMUS_CITY_ID";
	public static final String COL_ERASMUS_UNIVERSITY_ID = "ERASMUS_UNIVERSITY_ID";	
	public static final String COL_ADMIN_FLAG = "ADMIN_FLAG";
	
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
		user.setMsisdnCountryCode(super.getString(rs, COL_MSISDN_COUNTRY_CODE));
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
		user.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		user.setGoogleEmail(super.getString(rs, COL_GOOGLE_EMAIL));
		user.setFacebookEmail(super.getString(rs, COL_FACEBOOK_EMAIL));
		user.setResetPasswordToken(super.getString(rs, COL_RESET_PASSWORD_TOKEN));
		user.setProfileTypeId(super.getInteger(rs, COL_PROFILE_TYPE_ID));
		user.setHomeCountryId(super.getInteger(rs, COL_HOME_COUNTRY_ID));
		user.setHomeCityId(super.getInteger(rs, COL_HOME_CITY_ID));
		user.setHomeUniversityId(super.getInteger(rs, COL_HOME_UNIVERSITY_ID));
		user.setErasmusCountryId(super.getInteger(rs, COL_ERASMUS_COUNTRY_ID));
		user.setErasmusCityId(super.getInteger(rs, COL_ERASMUS_CITY_ID));
		user.setErasmusUniversityId(super.getInteger(rs, COL_ERASMUS_UNIVERSITY_ID));
		user.setAdminFlag(super.getString(rs, COL_ADMIN_FLAG));
		
		if (user.getProfilePhotoId() != null && 
				this.getfKeyMap() != null && this.getfKeyMap().containsKey(FKEY_FILE)) {
			ForeignKey<PlaceRowMapper, FileRowMapper> fKey = this.getfKeyMap().get(FKEY_FILE);
			FileModel photo = fKey.getDestMapper().mapRow(rs, rowNum);
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
		super.addColumn(COL_LAST_NAME);
		super.addColumn(COL_MODIFIED_BY);
		super.addColumn(COL_MODIFIED_DATE);
		super.addColumn(COL_MODIFIED_DATE_GMT);
		super.addColumn(COL_MSISDN);
		super.addColumn(COL_MSISDN_COUNTRY_CODE);
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
		super.addColumn(COL_GOOGLE_EMAIL);
		super.addColumn(COL_FACEBOOK_EMAIL);
		super.addColumn(COL_RESET_PASSWORD_TOKEN);
		super.addColumn(COL_PROFILE_TYPE_ID);
		super.addColumn(COL_HOME_COUNTRY_ID);
		super.addColumn(COL_HOME_CITY_ID);
		super.addColumn(COL_HOME_UNIVERSITY_ID);
		super.addColumn(COL_ERASMUS_COUNTRY_ID);
		super.addColumn(COL_ERASMUS_CITY_ID);
		super.addColumn(COL_ERASMUS_UNIVERSITY_ID);
		super.addColumn(COL_ADMIN_FLAG);
	}

}
