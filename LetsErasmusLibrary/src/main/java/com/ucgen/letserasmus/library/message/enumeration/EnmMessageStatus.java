package com.ucgen.letserasmus.library.message.enumeration;

public enum EnmMessageStatus {

	NOT_READ(0),
	READ(1);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	private EnmMessageStatus(Integer id) {
		this.id = id;
	}
	
}
