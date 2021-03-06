package com.ucgen.letserasmus.library.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	public OperationResult insertTransactionLog(TransactionLog transactionLog) {
		return this.logDao.insertTransactionLog(transactionLog);
	}

	@Override
	public List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag) {
		return this.logDao.listTransactionLog(transactionLog, entityFlag, userFlag);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public OperationResult insertIntegrationLog(IntegrationLog integrationLog) {
		return this.logDao.insertIntegrationLog(integrationLog);
	}
	
}