package com.ucgen.letserasmus.library.transactionlog.dao.impl;

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
import com.ucgen.letserasmus.library.review.dao.ReviewRowMapper;
import com.ucgen.letserasmus.library.transactionlog.dao.ILogDao;
import com.ucgen.letserasmus.library.transactionlog.dao.TransactionLogRowMapper;
import com.ucgen.letserasmus.library.transactionlog.model.TransactionLog;

@Repository
public class LogDao extends JdbcDaoSupport implements ILogDao {
		
	private static final String INSERT_REVIEW_SQL = "INSERT INTO TRANSACTION_LOG (USER_ID, OPERATION_ID, OPERATION_DATE, ENTITY_TYPE, ENTITY_ID, " 
			+ " CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
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
		
		this.getJdbcTemplate().update(INSERT_REVIEW_SQL, argList.toArray());
		
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

}
