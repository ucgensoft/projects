package com.ucgen.letserasmus.library.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.log.dao.ILogDao;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.log.service.ILogService;

@Service
public class LogService implements ILogService {

	private ILogDao logDao;
	
	@Autowired
	public void setLogDao(ILogDao logDao) {
		this.logDao = logDao;
	}

	@Override
	public OperationResult insertTransactionLog(TransactionLog transactionLog) throws OperationResultException {
		return this.logDao.insertTransactionLog(transactionLog);
	}

	@Override
	public List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag) {
		return this.logDao.listTransactionLog(transactionLog, entityFlag, userFlag);
	}

	@Override
	public OperationResult insertIntegrationLog(IntegrationLog integrationLog) {
		return this.logDao.insertIntegrationLog(integrationLog);
	}
	
}