package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmGender {

	FEMALE("F"),
	MALE("M"),
	OTHER("O");
	
	private String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmGender(String id) {
		this.id = id;
	}
	
}
