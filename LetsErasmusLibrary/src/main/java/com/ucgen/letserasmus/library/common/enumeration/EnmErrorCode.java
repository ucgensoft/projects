package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmErrorCode {

	SYSTEM_ERROR(-2),
	UNDEFINED_ERROR(-1),
	UNAUTHORIZED_OPERATION(1), 
	USER_NOT_FOUND(2),
	MSISDN_VERIFICATION_CODE_INCORRECT(3),
	USER_NOT_LOGGED_IN(4);

	private int id;

	public int getId() {
		return id;
	}

	EnmErrorCode(int id) {
		this.id = id;
	}

	public static EnmErrorCode getErrorCode(int id) {
		switch (id) {
		case 1:
			return EnmErrorCode.UNAUTHORIZED_OPERATION;
		case 2:
			return EnmErrorCode.USER_NOT_FOUND;
		default:
			return null;
		}
	}

}
