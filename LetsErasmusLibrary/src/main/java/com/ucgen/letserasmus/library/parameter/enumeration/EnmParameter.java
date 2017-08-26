package com.ucgen.letserasmus.library.parameter.enumeration;

import java.util.HashMap;
import java.util.Map;

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
	MIN_USER_PASSWORD_LENGTH(20, "MIN_USER_PASSWORD_LENGTH"),
	HELP_DEFAULT_GROUP(21, "HELP_DEFAULT_GROUP"),
	MAP_MIN_DISPLAY_DISTANCE(22, "MAP_MIN_DISPLAY_DISTANCE"),
	TWILIO_ACCOUNT_SID(23, "TWILIO_ACCOUNT_SID"),
	TWILIO_AUTH_TOKEN(24, "TWILIO_AUTH_TOKEN"),
	TWILIO_VERIFICATION_MESSAGE_TEMPLATE(25, "TWILIO_VERIFICATION_MESSAGE_TEMPLATE"),
	TWILIO_FROM_PHONE_NUMBER(26, "TWILIO_FROM_PHONE_NUMBER"),
	BLUESNAP_USER(27, "BLUESNAP_USER"),
	BLUESNAP_PASSWORD(28, "BLUESNAP_PASSWORD"),
	BLUESNAP_DOMAIN(29, "BLUESNAP_DOMAIN"),
	OPERATIN_TOKEN_LENGTH(30, "OPERATIN_TOKEN_LENGTH"),
	TRANSACTION_ID_LENGTH(31, "TRANSACTION_ID_LENGTH"),
	STRIPE_PRIVATE_KEY(32, "STRIPE_PRIVATE_KEY"),
	STRIPE_PUBLIC_KEY(33, "STRIPE_PUBLIC_KEY"),
	RESERVATION_PENDING_DURATION(34, "RESERVATION_PENDING_DURATION"),
	STATIC_FILE_VERSION(35, "STATIC_FILE_VERSION"),
	GOOGLE_API_KEY(36, "GOOGLE_API_KEY"),
	ADMIN_EMAIL(37, "ADMIN_EMAIL"),
	ADMIN_EMAIL_PASSWORD(38, "ADMIN_EMAIL_PASSWORD"),
	SMTP_SERVER(39, "SMTP_SERVER"),
	MAX_PLACE_PHOTO_COUNT(40, "MAX_PLACE_PHOTO_COUNT"),
	AWS_CREDENTIALS(41, "AWS_CREDENTIALS"),
	AWS_USER_FILES_BUCKET_NAME(42, "AWS_USER_FILES_BUCKET_NAME"),
	TMP_FILE_PATH(43, "TMP_FILE_PATH"),
	STATIC_FILE_URL_PREFIX(44, "STATIC_FILE_URL_PREFIX"),
	LETSERASMUS_RESPONSIBLE_EMAIL(45, "LETSERASMUS_RESPONSIBLE_EMAIL");
	
	int id;
	String name;
	private static Map<String, EnmParameter> parameterNameMap;
	
	static {
		parameterNameMap = new HashMap<String, EnmParameter>();
		for (EnmParameter parameter : EnmParameter.values()) {
			parameterNameMap.put(parameter.getName(), parameter);
		}
	}
	
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
		if (name != null && parameterNameMap.containsKey(name)) {
			return parameterNameMap.get(name);
		}
		return null;
	}
	
}
