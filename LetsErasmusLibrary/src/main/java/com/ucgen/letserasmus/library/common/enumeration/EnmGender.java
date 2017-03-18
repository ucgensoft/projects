package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmGender {

	FEMALE("F"),
	MALE("M"),
	ALL("A");
	
	private String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmGender(String id) {
		this.id = id;
	}

	public static EnmGender getGender(String id) {
		for (EnmGender gender : EnmGender.values()) {
			if (gender.getId().equals(id)) {
				return gender;
			}
		}
		return null;
	}
	
}
