package com.ucgen.common.util.enumeration;

public enum EnmCompareResult {

	SMALLER(-1), EQUAL(0), GREATER(1);
	
	private int value;
	
	public int getValue() {
		return this.value;
	}
	
	EnmCompareResult(int value) {
		this.value = value;
	}
	
}
