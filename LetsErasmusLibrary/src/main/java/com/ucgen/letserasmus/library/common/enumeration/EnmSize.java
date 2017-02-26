package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmSize {

	SMALL("small"), MEDIUM("medium"), LARGE("large");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	EnmSize(String value) {
		this.value = value;
	}
	
}
