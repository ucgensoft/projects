package com.ucgen.letserasmus.library.log.enumeration;

public enum EnmDirection {

	OUTGOING(1),
	INCOMING(2);
	
	private final int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmDirection(int id) {
		this.id = id;
	}
 	
}
