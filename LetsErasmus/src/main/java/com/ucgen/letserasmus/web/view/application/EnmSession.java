package com.ucgen.letserasmus.web.view.application;

public enum EnmSession {

	USER("USER"),
	ACTIVE_OPERATION("ACTIVE_OPERATION"),
	ACTIVE_PLACE("ACTIVE_PLACE"),
	PLACE_PHOTO_LIST("PLACE_PHOTO_LIST"),
	USER_PHOTO("USER_PHOTO"),
	TMP_PHOTO_PLACE_ID("TMP_PHOTO_PLACE_ID"),
	TMP_USER_PLACE_ID("TMP_USER_PLACE_ID"),
	MSISDN_VERIFICATION_CODE("MSISDN_VERIFICATION_CODE"),
	LOGIN_TYPE("LOGIN_TYPE"),
	RESERVATION_PLACE("RESERVATION_PLACE"),
	ACTIVE_RESERVATION("ACTIVE_RESERVATION");
	
	private final String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmSession(String id) {
		this.id = id;
	}
 	
}
