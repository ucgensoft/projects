package com.ucgen.common.exception.operation;

import com.ucgen.common.operationresult.OperationResult;

public class OperationResultException extends OperationException {

	private static final long serialVersionUID = -3252624442772145747L;

	private OperationResult operationResult;

	public OperationResult getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(OperationResult operationResult) {
		this.operationResult = operationResult;
	}
	
	public OperationResultException(OperationResult operationResult) {
		this.operationResult = operationResult;
	}
	
}
