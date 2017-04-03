package com.ucgen.letserasmus.library.log.dao;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.model.TransactionLog;

public interface ILogDao {

	OperationResult insertTransactionLog(TransactionLog transactionLog) throws OperationResultException;
	
	List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag);
	
	OperationResult insertIntegrationLog(IntegrationLog integrationLog);
	
}
