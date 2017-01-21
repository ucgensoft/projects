package com.ucgen.letserasmus.library.user.enumeration;

public enum EnmUserStatus {

	ACTIVE(0), DEACTIVE(1);
	
	private Integer value;
	
	public Integer getValue() {
		return this.value;
	}
	
	EnmUserStatus(Integer value) {
		this.value = value;
	}
	
}
