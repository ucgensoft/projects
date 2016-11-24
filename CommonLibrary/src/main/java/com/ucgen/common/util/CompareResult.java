package com.ucgen.common.util;

public enum CompareResult {
	LOWER(-1), EQUAL(0), BIGGER(1);
	private int value;

	public int getValue() {
		return this.value;
	}

	private CompareResult(int value) {
		this.value = value;
	}

}
