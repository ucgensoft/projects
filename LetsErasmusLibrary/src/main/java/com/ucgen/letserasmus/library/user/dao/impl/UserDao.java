package com.ucgen.letserasmus.library.user.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

@Repository
public class UserDao extends JdbcDaoSupport implements IUserDao {

	private static final String GET_USER_SQL = " SELECT * FROM USER WHERE 1 = 1 ";
	
	private static final String INSERT_USER_SQL = " INSERT INTO USER(EMAIL, PASSWORD, MSISDN, FIRST_NAME, LAST_NAME, GENDER,  STATUS, EMAIL_VERIFIED, MSISDN_VERIFIED, USER_ACTIVATION_KEY_EMAIL, USER_ACTIVATION_KEY_MSISDN, PROFILE_PHOTO_ID, FACEBOOK_TOKEN_ID, IP, CREATED_BY, CREATED_DATE, CREATED_DATE_GMT, MODIFIED_BY, MODIFIED_DATE, MODIFIED_DATE_GMT, GOOGLE_ID, FACEBOOK_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private static final String UPDATE_USER_SQL = " UPDATE USER SET $1 WHERE ID = ? ";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public UserDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public User getUser(User user) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<>();
		
		UserRowMapper userRowMapper = new UserRowMapper("U");
		userRowMapper.addFKey(UserRowMapper.FKEY_FILE);
		
		sqlBuilder.append(userRowMapper.getSelectSqlWithForeignKeys());
		
		if (user.getId() != null) {
			sqlBuilder.append(" AND " + userRowMapper.getCriteriaColumnName(PlaceRowMapper.COL_ID) + " = ? ");
			argList.add(user.getId());
		}
		
		if (user.getEmail() != null) {
			sqlBuilder.append(" AND " + userRowMapper.getCriteriaColumnName(UserRowMapper.COL_EMAIL) + " = ? ");
			argList.add(user.getEmail());
		}
		
		if (user.getPassword() != null) {
			sqlBuilder.append(" AND " + userRowMapper.getCriteriaColumnName(UserRowMapper.COL_PASSWORD) + " = ? ");
			argList.add(user.getPassword());
		}
		
		if (user.getGoogleId() != null) {
			sqlBuilder.append(" AND " + userRowMapper.getCriteriaColumnName(UserRowMapper.COL_GOOGLE_ID) + " = ? ");
			argList.add(user.getGoogleId());
		}
		
		if (user.getFacebookId() != null) {
			sqlBuilder.append(" AND " + userRowMapper.getCriteriaColumnName(UserRowMapper.COL_FACEBOOK_ID) + " = ? ");
			argList.add(user.getFacebookId());
		}
		
		List<User> userList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), userRowMapper);		
		
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		} else {
			return null;
		}		
	}

	@Override
	public ValueOperationResult<Integer> updateUser(User user, boolean setNull) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
		List<Object> argList = new ArrayList<Object>();
		
		String updateSql = new String(UPDATE_USER_SQL);
		StringBuilder updateFields = new StringBuilder();
		
		if (user.getFirstName() != null) {
			StringUtil.append(updateFields, " FIRST_NAME = ?", ",");
			argList.add(user.getFirstName());
		}
		
		if (user.getLastName() != null) {
			StringUtil.append(updateFields, " LAST_NAME = ?", ",");
			argList.add(user.getLastName());
		}
		
		if (user.getEmail() != null) {
			StringUtil.append(updateFields, " EMAIL = ?", ",");
			argList.add(user.getEmail());
		}
		
		if (user.getPassword() != null) {
			StringUtil.append(updateFields, " PASSWORD = ?", ",");
			argList.add(user.getPassword());
		}
		
		if (setNull || user.getMsisdn() != null) {
			StringUtil.append(updateFields, " MSISDN = ?", ",");
			argList.add(user.getMsisdn());
		}
		
		if (user.getGender() != null) {
			StringUtil.append(updateFields, " GENDER = ?", ",");
			argList.add(user.getGender());
		}
		
		if (user.getStatus() != null) {
			StringUtil.append(updateFields, " STATUS = ?", ",");
			argList.add(user.getStatus());
		}
		
		if (user.getEmailVerified() != null) {
			StringUtil.append(updateFields, " EMAIL_VERIFIED = ?", ",");
			argList.add(user.getEmailVerified());
		}
		
		if (user.getMsisdnVerified() != null) {
			StringUtil.append(updateFields, " MSISDN_VERIFIED = ?", ",");
			argList.add(user.getMsisdnVerified());
		}
		
		if (user.getUserActivationKeyEmail() != null) {
			StringUtil.append(updateFields, " USER_ACTIVATION_KEY_EMAIL = ?", ",");
			argList.add(user.getUserActivationKeyEmail());
		}
		
		if (user.getUserActivationKeyMsisdn() != null) {
			StringUtil.append(updateFields, " USER_ACTIVATION_KEY_MSISDN = ?", ",");
			argList.add(user.getUserActivationKeyMsisdn());
		}
		
		if (user.getProfilePhotoId() != null) {
			StringUtil.append(updateFields, " PROFILE_PHOTO_ID = ?", ",");
			argList.add(user.getProfilePhotoId());
		}
		
		if (user.getUserActivationKeyMsisdn() != null) {
			StringUtil.append(updateFields, " USER_ACTIVATION_KEY_MSISDN = ?", ",");
			argList.add(user.getUserActivationKeyMsisdn());
		}
		
		if (user.getGoogleId() != null) {
			StringUtil.append(updateFields, " GOOGLE_ID = ?", ",");
			argList.add(user.getGoogleId());
		}
		
		if (user.getFacebookId() != null) {
			StringUtil.append(updateFields, " FACEBOOK = ?", ",");
			argList.add(user.getFacebookId());
		}
		
		if (user.getJobTitle() != null) {
			StringUtil.append(updateFields, " JOB_TITLE = ?", ",");
			argList.add(user.getJobTitle());
		}
		
		if (user.getSchoolName() != null) {
			StringUtil.append(updateFields, " SCHOOL_NAME = ?", ",");
			argList.add(user.getSchoolName());
		}
		
		if (user.getBirthDate() != null) {
			StringUtil.append(updateFields, " BIRTH_DATE = ?", ",");
			argList.add(user.getBirthDate());
		}
		
		if (user.getDescription() != null) {
			StringUtil.append(updateFields, " DESCRIPTION = ?", ",");
			argList.add(user.getDescription());
		}
		
		if (user.getResidenceLocationName() != null) {
			StringUtil.append(updateFields, " RESIDENCE_LOCATION_NAME = ?", ",");
			argList.add(user.getResidenceLocationName());
		}
		
		if (user.getLanguages() != null) {
			StringUtil.append(updateFields, " LANGUAGES = ?", ",");
			argList.add(user.getLanguages());
		}
		
		StringUtil.append(updateFields, " MODIFIED_BY = ?", ",");
		argList.add(user.getModifiedBy());
		
		argList.add(user.getId());
		
		updateSql = updateSql.replace("$1", updateFields);
		
		int updatedRowCount =  this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}
	
	@Override
	public OperationResult insertUser(User user) {
		
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
		argList.add(user.getEmail());
		argList.add(user.getPassword());
		argList.add(user.getMsisdn());
		argList.add(user.getFirstName());
		argList.add(user.getLastName());
		argList.add(user.getGender());
		argList.add(user.getStatus());
		argList.add(user.getEmailVerified());
		argList.add(user.getMsisdnVerified());
		argList.add(user.getUserActivationKeyEmail());
		argList.add(user.getUserActivationKeyMsisdn());		
		argList.add(user.getProfilePhotoId());
		argList.add(user.getFacebookTokenId());
		argList.add(user.getIp());
		argList.add(user.getCreatedBy());
		argList.add(user.getCreatedDate());		
		argList.add(user.getCreatedDateGmt());
		argList.add(user.getModifiedBy());
		argList.add(user.getModifiedDate());
		argList.add(user.getModifiedDateGmt());
		argList.add(user.getGoogleId());
		argList.add(user.getFacebookId());
		
		int i = this.getJdbcTemplate().update(INSERT_USER_SQL, argList.toArray());
		
		if (i > 0) {
			user.setId(this.utilityDao.getLastIncrementId());
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} else {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc("User insert operation failed.");
		}
		
		return operationResult;
	}

	@Override
	public User getUserForLogin(User user) {
		StringBuilder sqlBuilder = new StringBuilder(GET_USER_SQL);
		List<Object> argList = new ArrayList<>();
		
		UserRowMapper userRowMapper = new UserRowMapper();
				
		sqlBuilder.append(" AND ((EMAIL = IFNULL(?, EMAIL) AND PASSWORD = IFNULL(?, PASSWORD)) " 
				+ " OR (GOOGLE_ID = IFNULL(?, GOOGLE_ID) AND GOOGLE_EMAIL = IFNULL(?, GOOGLE_EMAIL)) " 
				+ " OR (FACEBOOK_ID = IFNULL(?, FACEBOOK_ID) AND FACEBOOK_EMAIL = IFNULL(?, FACEBOOK_EMAIL)))");
		
		argList.add(user.getEmail());
		argList.add(user.getPassword());
		argList.add(user.getGoogleId());
		argList.add(user.getGoogleEmail());
		argList.add(user.getFacebookId());
		argList.add(user.getFacebookEmail());
				
		List<User> userList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), userRowMapper);
		if (userList != null && userList.size() >0) {
			return userList.get(0);
		} else {
			return null;
		}
	}

}
