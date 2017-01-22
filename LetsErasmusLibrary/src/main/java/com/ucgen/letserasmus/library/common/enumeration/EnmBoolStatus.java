package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmBoolStatus {
	
	YES("Y"),
	NO("N");
	
	private String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmBoolStatus(String id) {
		this.id = id;
	}
	
}
