package com.ucgen.letserasmus.library.transactionlog.service;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.transactionlog.model.TransactionLog;

public interface ILogService {

	OperationResult insertTransactionLog(TransactionLog transactionLog) throws OperationResultException;
	
	List<TransactionLog> listTransactionLog(TransactionLog transactionLog, boolean entityFlag, boolean userFlag);
	
}
