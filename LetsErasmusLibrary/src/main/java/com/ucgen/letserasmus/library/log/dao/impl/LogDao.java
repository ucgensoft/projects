package com.ucgen.letserasmus.library.log.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.log.dao.ILogDao;
import com.ucgen.letserasmus.library.log.dao.TransactionLogRowMapper;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.review.dao.ReviewRowMapper;

@Repository
public class LogDao extends JdbcDaoSupport implements ILogDao {
		
	private static final String INSERT_TRANSACTION_SQL = "INSERT INTO TRANSACTION_LOG (USER_ID, OPERATION_ID, OPERATION_DATE, ENTITY_TYPE, ENTITY_ID, " 
			+ " CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_INTEGRATION_SQL = "INSERT INTO INTEGRATION_LOG (USER_ID, EXT_SYSTEM_ID, OPERATION_ID, OPERATION_DATE, DURATION, " 
			+ " REQUEST, RESPONSE, RESPONSE_CODE, CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public LogDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insertTransactionLog(TransactionLog transactionLog) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(transactionLog.getUserId());
		argList.add(transactionLog.getOperationId());
		argList.add(transactionLog.getOperationDate());
		argList.add(transactionLog.getEntityType());
		argList.add(transactionLog.getEntityId());
		argList.add(transactionLog.getCreatedBy());
		argList.add(transactionLog.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_TRANSACTION_SQL, argList.toArray());
		
		transactionLog.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
	@Override
	public List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		TransactionLogRowMapper transactionLogRowMapper = new TransactionLogRowMapper();
		
		if (userFlag) {
			transactionLogRowMapper.addFKey(TransactionLogRowMapper.FKEY_USER);
		}
		
		sqlBuilder.append(transactionLogRowMapper.getSelectSqlWithForeignKeys());
		
		if (transactionLog != null) {
			if (transactionLog.getId() != null) {
				sqlBuilder.append(" AND " + transactionLogRowMapper.getCriteriaColumnName(TransactionLogRowMapper.COL_ID) + " = ? ");
				argList.add(transactionLog.getId());
			}
			if (transactionLog.getUserId() != null) {
				sqlBuilder.append(" AND " + transactionLogRowMapper.getCriteriaColumnName(TransactionLogRowMapper.COL_USER_ID) + " = ? ");
				argList.add(transactionLog.getUserId());
			}
			if (transactionLog.getOperationId() != null) {
				sqlBuilder.append(" AND " + transactionLogRowMapper.getCriteriaColumnName(TransactionLogRowMapper.COL_OPERATION_ID) + " = ? ");
				argList.add(transactionLog.getOperationId());
			}
			if (transactionLog.getEntityType() != null) {
				sqlBuilder.append(" AND " + transactionLogRowMapper.getCriteriaColumnName(TransactionLogRowMapper.COL_ENTITY_TYPE) + " = ? ");
				argList.add(transactionLog.getEntityType());
			}
			if (transactionLog.getEntityId() != null) {
				sqlBuilder.append(" AND " + transactionLogRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_ENTITY_ID) + " = ? ");
				argList.add(transactionLog.getEntityId());
			}
			
		}
		
		sqlBuilder.append(" ORDER BY " + transactionLogRowMapper.getCriteriaColumnName(TransactionLogRowMapper.COL_OPERATION_DATE) + " ASC");
				
		List<TransactionLog> transactionLogList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), transactionLogRowMapper);		
				
		return transactionLogList;
	}

	@Override
	public OperationResult insertIntegrationLog(IntegrationLog integrationLog) {
OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(integrationLog.getUserId());
		argList.add(integrationLog.getExtSystemId());
		argList.add(integrationLog.getOperationId());
		argList.add(integrationLog.getOperationDate());
		argList.add(integrationLog.getDuration());
		argList.add(integrationLog.getRequest());
		argList.add(integrationLog.getResponse());
		argList.add(integrationLog.getResponseCode());
		argList.add(integrationLog.getCreatedBy());
		argList.add(integrationLog.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_INTEGRATION_SQL, argList.toArray());
		
		integrationLog.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

}
