package com.ucgen.letserasmus.library.transactionlog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.transactionlog.dao.ILogDao;
import com.ucgen.letserasmus.library.transactionlog.model.TransactionLog;
import com.ucgen.letserasmus.library.transactionlog.service.ILogService;

@Service
public class LogService implements ILogService {

	private ILogDao transactionLogDao;
	
	@Autowired
	public void setTransactionLogDao(ILogDao transactionLogDao) {
		this.transactionLogDao = transactionLogDao;
	}

	@Override
	public OperationResult insertTransactionLog(TransactionLog transactionLog) throws OperationResultException {
		return this.transactionLogDao.insertTransactionLog(transactionLog);
	}

	@Override
	public List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag) {
		return this.transactionLogDao.listTransactionLog(transactionLog, entityFlag, userFlag);
	}
	
}