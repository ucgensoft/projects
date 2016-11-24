package com.ucgen.common.concurrency;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;

public abstract class BatchTask {

	private EnmResultCode resultCode;
	private OperationResult operationResult;
	
	public OperationResult getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(OperationResult operationResult) {
		if (operationResult != null && operationResult.getResultCode() != null) {
			this.operationResult = operationResult;
			this.resultCode = EnmResultCode.getResultCode(operationResult.getResultCode());
		}
	}

	public EnmResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(EnmResultCode resultCode) {
		if (resultCode != null) {
			this.resultCode = resultCode;
			this.operationResult = new OperationResult(resultCode.getValue(), "");
		}
	}
	
}
