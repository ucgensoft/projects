package com.ucgen.common.operationresult;


public class OperationResult {

	private Integer resultCode;
	private String resultDesc;
	private Integer errorCode;
	
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public OperationResult() {
		this(null, null);
	}
	
	public OperationResult (Integer argResultCode, String argResultDesc) {
		this.resultCode = argResultCode;
		this.resultDesc = argResultDesc;
	}
	
	public static boolean isResultSucces(OperationResult operationResult) {
		if (operationResult != null && operationResult.getResultCode()!= null && 
				operationResult.getResultCode().equals(EnmResultCode.SUCCESS.getValue())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getResultDesc(OperationResult operationResult) {
		if (operationResult != null && operationResult.getResultDesc()!= null) {
			return operationResult.getResultDesc();
		} else {
			return "";
		}
	}
	
}
