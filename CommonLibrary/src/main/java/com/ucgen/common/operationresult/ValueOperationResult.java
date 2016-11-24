package com.ucgen.common.operationresult;

public class ValueOperationResult<T> extends OperationResult {

	private T resultValue;

	public T getResultValue() {
		return resultValue;
	}

	public void setResultValue(T resultValue) {
		this.resultValue = resultValue;
	}
	
}
