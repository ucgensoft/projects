package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmSize {

	SMALL("small"), MEDIUM("medium"), LARGE("large");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	EnmSize(String value) {
		this.value = value;
	}
	
	public static EnmSize getSize(String value) {
		for (EnmSize enmSize : EnmSize.values()) {
			if (enmSize.getValue().equalsIgnoreCase(value)) {
				return enmSize;
			}
		}
		return null;
	}
	
}
