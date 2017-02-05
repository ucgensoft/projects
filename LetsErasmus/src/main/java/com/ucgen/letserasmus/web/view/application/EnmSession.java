package com.ucgen.letserasmus.web.view.application;

public enum EnmSession {

	USER("USER"),
	ACTIVE_OPERATION("ACTIVE_OPERATION"),
	ACTIVE_PLACE("ACTIVE_PLACE");
	
	private final String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmSession(String id) {
		this.id = id;
	}
 	
}
