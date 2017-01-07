package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmEntityType {

	USER(1),
	PLACE(2),
	EVENT(3);
	
	private Integer value;
	
	public Integer getValue() {
		return this.value;
	}
	
	private EnmEntityType(Integer value) {
		this.value = value;
	}
	
}
