package com.ucgen.letserasmus.web.enumeration;

public enum EnmUriParameter {

	OPERATION("op"),
	OPERATION_TOKEN("opToken"),
	PLACE_ID("placeId");
	
	String name;

	public String getName() {
		return name;
	}

	EnmUriParameter(String name) {
		this.name = name;
	}
	
}
