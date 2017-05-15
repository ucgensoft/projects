package com.ucgen.letserasmus.library.log.service;

import java.util.List;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.model.TransactionLog;

public interface ILogService {

	OperationResult insertTransactionLog(TransactionLog transactionLog);
	
	List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag);
	
	OperationResult insertIntegrationLog(IntegrationLog integrationLog);
	
}
