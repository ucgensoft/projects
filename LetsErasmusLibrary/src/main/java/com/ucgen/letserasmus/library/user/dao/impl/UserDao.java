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
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

@Repository
public class UserDao extends JdbcDaoSupport implements IUserDao {

	private static final String GET_USER_SQL = " SELECT * FROM USER WHERE 1 = 1 ";
	
	private static final String INSERT_USER_SQL = " INSERT INTO USER(EMAIL, PASSWORD, MSISDN, FIRST_NAME, LAST_NAME, GENDER,  STATUS, EMAIL_VERIFIED, MSISDN_VERIFIED, USER_ACTIVATION_KEY_EMAIL, USER_ACTIVATION_KEY_MSISDN, PROFILE_PHOTO_ID, FACEBOOK_TOKEN_ID, IP, CREATED_BY, CREATED_DATE, CREATED_DATE_GMT, MODIFIED_BY, MODIFIED_DATE, MODIFIED_DATE_GMT, GOOGLE_ID, FACEBOOK_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private static final String UPDATE_USER_SQL = " UPDATE USER SET EMAIL=?,PASSWORD=?,MSISDN=?,FIRST_NAME=?,LAST_NAME=?,GENDER=?,STATUS=?,EMAIL_VERIFIED=?,MSISDN_VERIFIED=?,USER_ACTIVATION_KEY_EMAIL=?,USER_ACTIVATION_KEY_MSISDN=?,PROFILE_PHOTO_ID=?,FACEBOOK_TOKEN_ID=?,IP=?,CREATED_BY=?,CREATED_DATE=?,CREATED_DATE_GMT=?,MODIFIED_BY=?,MODIFIED_DATE=?,MODIFIED_DATE_GMT=? WHERE ID = ? ";
	
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
		StringBuilder sqlBuilder = new StringBuilder(GET_USER_SQL);
		List<Object> argList = new ArrayList<>();
		
		if (user.getId() != null) {
			sqlBuilder.append(" AND ID = ?");
			argList.add(user.getId());
		}
		
		if (user.getEmail() != null) {
			sqlBuilder.append(" AND EMAIL = ?");
			argList.add(user.getEmail());
		}
		
		if (user.getPassword() != null) {
			sqlBuilder.append(" AND PASSWORD = ?");
			argList.add(user.getPassword());
		}
		
		if (user.getGoogleId() != null) {
			sqlBuilder.append(" AND GOOGLE_ID = ?");
			argList.add(user.getGoogleId());
		}
		
		if (user.getFacebookId() != null) {
			sqlBuilder.append(" AND FACEBOOK_ID = ?");
			argList.add(user.getFacebookId());
		}
		
		List<User> userList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), new UserRowMapper());		
		
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		} else {
			return null;
		}		
	}

	@Override
	public ValueOperationResult<Integer> updateUser(User user) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
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
		argList.add(user.getId());
		
		int updatedRowCount =  this.getJdbcTemplate().update(UPDATE_USER_SQL, argList.toArray() );
		
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

}
