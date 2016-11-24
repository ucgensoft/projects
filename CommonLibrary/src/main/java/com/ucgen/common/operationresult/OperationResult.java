package com.ucgen.common.operationresult;


public class OperationResult {

	private Integer resultCode;
	private String resultDesc;
	private Object resultObject;
	
	
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
	public Object getResultObject() {
		return resultObject;
	}
	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
	
	public OperationResult() {
		this(null, null, null);
	}
	
	public OperationResult (Integer argResultCode, String argResultDesc) {
		this(argResultCode, argResultDesc, null);
	}
	
	public OperationResult (Integer argResultCode, String argResultDesc, Object argResultObject) {
		this.resultCode = argResultCode;
		this.resultDesc = argResultDesc;
		this.resultObject = argResultObject;
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
