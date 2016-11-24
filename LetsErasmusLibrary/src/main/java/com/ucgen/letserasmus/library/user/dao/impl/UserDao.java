package com.ucgen.letserasmus.library.user.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

@Repository
public class UserDao extends JdbcDaoSupport implements IUserDao {

	private static final String GET_USER_WITH_ID_SQL = " SELECT `ID`, `EMAIL`, `PASSWORD`, `MSISDN`, `FIRST_NAME`, `LAST_NAME`, `STATUS`, `EMAIL_VERIFIED`, `MSISDN_VERIFIED`, `USER_ACTIVATION_KEY_EMAIL`, `USER_ACTIVATION_KEY_MSISDN`, `FILE_ID`, `FACEBOOK_TOKEN_ID`, `IP`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT` FROM `USER` WHERE ID = ? ";
	private static final String INSERT_USER_SQL = " INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `MSISDN`, `FIRST_NAME`, `LAST_NAME`, `STATUS`, `EMAIL_VERIFIED`, `MSISDN_VERIFIED`, `USER_ACTIVATION_KEY_EMAIL`, `USER_ACTIVATION_KEY_MSISDN`, `FILE_ID`, `FACEBOOK_TOKEN_ID`, `IP`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	private static final String UPDATE_USER_SQL = " UPDATE `USER` SET `EMAIL`=?,`PASSWORD`=?,`MSISDN`=?,`FIRST_NAME`=?,`LAST_NAME`=?,`STATUS`=?,`EMAIL_VERIFIED`=?,`MSISDN_VERIFIED`=?,`USER_ACTIVATION_KEY_EMAIL`=?,`USER_ACTIVATION_KEY_MSISDN`=?,`FILE_ID`=?,`FACEBOOK_TOKEN_ID`=?,`IP`=?,`CREATED_BY`=?,`CREATED_DATE`=?,`CREATED_DATE_GMT`=?,`MODIFIED_BY`=?,`MODIFIED_DATE`=?,`MODIFIED_DATE_GMT`=? WHERE `ID` = ? ";
	
	@Autowired
	public UserDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public ListOperationResult<User> getUser(Long id) {
		
		
		ListOperationResult<User> listOperationResult = new ListOperationResult<User>();
		List<User> userList = super.getJdbcTemplate().query(GET_USER_WITH_ID_SQL, new Object[] { id }, new UserRowMapper());		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(userList);
		
		return listOperationResult;
		
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
		argList.add(user.getStatus());
		argList.add(user.getEmailVerified());
		argList.add(user.getMsisdnVerified());
		argList.add(user.getUserActivationKeyEmail());
		argList.add(user.getUserActivationKeyMsisdn());		
		argList.add(user.getFileId());
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
	public ValueOperationResult<Integer> insertUser(User user) {
		

		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
		
		List<Object> argList = new ArrayList<Object>();
		argList.add(user.getEmail());
		argList.add(user.getPassword());
		argList.add(user.getMsisdn());
		argList.add(user.getFirstName());
		argList.add(user.getLastName());
		argList.add(user.getStatus());
		argList.add(user.getEmailVerified());
		argList.add(user.getMsisdnVerified());
		argList.add(user.getUserActivationKeyEmail());
		argList.add(user.getUserActivationKeyMsisdn());		
		argList.add(user.getFileId());
		argList.add(user.getFacebookTokenId());
		argList.add(user.getIp());
		argList.add(user.getCreatedBy());
		argList.add(user.getCreatedDate());		
		argList.add(user.getCreatedDateGmt());
		argList.add(user.getModifiedBy());
		argList.add(user.getModifiedDate());
		argList.add(user.getModifiedDateGmt());
		
		
		int i = this.getJdbcTemplate().update(INSERT_USER_SQL, argList.toArray());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(i);				
		return operationResult;
	}

}
