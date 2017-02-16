package com.ucgen.common.operationresult;

public enum EnmResultCode {

	SUCCESS(0), WARNING(1), ERROR(2), EXCEPTION(3);

	private int value;

	public int getValue() {
		return value;
	}

	EnmResultCode(int argvalue) {
		this.value = argvalue;
	}

	public static EnmResultCode getResultCode(int argValue) {
		for (EnmResultCode resultCode : EnmResultCode.values()) {
			if (resultCode.getValue() == argValue) {
				return resultCode;
			}
		}
		return null;
	}

}
