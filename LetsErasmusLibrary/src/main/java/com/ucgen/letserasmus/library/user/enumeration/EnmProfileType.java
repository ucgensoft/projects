package com.ucgen.letserasmus.library.user.enumeration;

public enum EnmProfileType {

	ERASMUS_STUDENT(1),
	LOCAL(2);
	
	private int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmProfileType(int id) {
		this.id = id;
	}
	
	public static EnmProfileType getProfileType(int id) {
		for (EnmProfileType profileType : EnmProfileType.values()) {
			if (profileType.getId() == id) {
				return profileType;
			}
		}
		return null;
	}
	
}
