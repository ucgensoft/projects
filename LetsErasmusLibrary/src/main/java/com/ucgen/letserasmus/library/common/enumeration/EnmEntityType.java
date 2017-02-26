package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmEntityType {

	USER(1),
	PLACE(2),
	EVENT(3);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	private EnmEntityType(Integer id) {
		this.id = id;
	}
	
}
