package com.ucgen.letserasmus.library.bluesnap.enumeration;

public enum ExtEnmTransactionType {

	AUTH_ONLY("AUTH_ONLY"),
	AUTH_REVERSAL("AUTH_REVERSAL"),
	CAPTURE("CAPTURE");
	
	private String name;
	
	public String getName() {
		return this.name;
	}
	
	ExtEnmTransactionType(String name) {
		this.name = name;
	}
	
}
