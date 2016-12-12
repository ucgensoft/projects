package com.ucgen.common.operationresult;

import java.util.List;

public class ListOperationResult<T> extends OperationResult {

	private List<T> objectList;
	private Integer totalSize;
	
	public List<T> getObjectList() {
		return objectList;
	}
	public void setObjectList(List<T> objectList) {
		this.objectList = objectList;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public ListOperationResult() {
		this(null, null);
	}
	
	public ListOperationResult(Integer argResultCode, String argResultDesc) {
		super(argResultCode, argResultDesc);
	}

}
