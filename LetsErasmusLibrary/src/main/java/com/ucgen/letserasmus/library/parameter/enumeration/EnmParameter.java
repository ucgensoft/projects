package com.ucgen.letserasmus.library.parameter.enumeration;

public enum EnmParameter {

	LETSERASMUS_URL_PREFIX(1, "LETSERASMUS_URL_PREFIX"),
	PLACE_IMAGE_SUB_URL(2, "PLACE_IMAGE_SUB_URL"),
	USER_IMAGE_SUB_URL(3, "USER_IMAGE_SUB_URL"),
	LOCAL_APP_ROOT_PATH(4, "LOCAL_APP_ROOT_PATH"),
	PLACE_PHOTO_NAME_TEMPLATE(5, "PLACE_PHOTO_NAME_TEMPLATE"),
	USER_PHOTO_NAME_TEMPLATE(6, "USER_PHOTO_NAME_TEMPLATE"),
	IMAGE_SUFFIX(7, "IMAGE_SUFFIX"),
	DEFAULT_USER_IMAGE_NAME_TEMPLATE(8, "DEFAULT_USER_IMAGE_NAME_TEMPLATE"),
	SMALL_USER_PHOTO_SIZE(9, "SMALL_USER_PHOTO_SIZE"),
	MEDIUM_USER_PHOTO_SIZE(10, "MEDIUM_USER_PHOTO_SIZE"),
	SMALL_PLACE_PHOTO_SIZE(11, "SMALL_PLACE_PHOTO_SIZE"),
	MEDIUM_PLACE_PHOTO_SIZE(12, "MEDIUM_PLACE_PHOTO_SIZE"),
	FACEBOOK_APP_ID(13, "FACEBOOK_APP_ID"),
	GOOGLE_APP_ID(14, "GOOGLE_APP_ID"),
	PLACE_SERVICE_FEE_RATE(15, "PLACE_SERVICE_FEE_RATE"),
	PLACE_COMMISSION_FEE_RATE(16, "PLACE_COMMISSION_FEE_RATE"),
	CHANGE_LOCATION_DISTANCE(17, "CHANGE_LOCATION_DISTANCE"),
	PLACE_SEARCH_PAGE_SIZE(18, "PLACE_SEARCH_PAGE_SIZE"),
	LOG_CONFIG_FILE_PATH(19, "LOG_CONFIG_FILE_PATH"),
	MIN_USER_PASSWORD_LENGTH(20, "MIN_USER_PASSWORD_LENGTH");
	
	int id;
	String name;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	EnmParameter(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static EnmParameter getParameter(String name) {
		if (name != null) {
			for (EnmParameter parameter : EnmParameter.values()) {
				if (parameter.getName().equals(name)) {
					return parameter;
				}
			}
		}
		return null;
	}
	
}
