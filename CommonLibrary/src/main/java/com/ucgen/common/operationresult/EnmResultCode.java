package com.ucgen.common.operationresult;

public enum EnmResultCode {

	SUCCESS(0), ERROR(1), WARNING(2), EXCEPTION(3);

	private int value;

	public int getValue() {
		return value;
	}

	EnmResultCode(int argvalue) {
		this.value = argvalue;
	}

	public static EnmResultCode getResultCode(int argValue) {
		switch (argValue) {
		case 0:
			return EnmResultCode.SUCCESS;
		case 1:
			return EnmResultCode.ERROR;
		case 2:
			return EnmResultCode.WARNING;
		case 3:
			return EnmResultCode.EXCEPTION;
		default:
			return null;
		}
	}

}
