package com.ucgen.letserasmus.library.place.enumeration;

public enum EnmPlaceStatus {

	INITIAL(0),
	ACTIVE(1),
	DEACTIVE(2);
	
	private Integer value;
	
	public Integer getValue() {
		return this.value;
	}
	
	private EnmPlaceStatus(Integer value) {
		this.value = value;
	}
	
}
