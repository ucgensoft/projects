package com.ucgen.letserasmus.library.log.enumeration;

public enum EnmOperation {

	CREATE_USER(1),
	UPDATE_USER(2),
	LIST_USER_PLACE(3),
	EDIT_PLACE(4),
	CREATE_PLACE(5),
	TRUST_AND_VERIFICATION(6),
	VERIFICATION(7),
	CREATE_RESERVATION(8),
	FINISH_RESERVATION(9),
	CONFIRM_EMAIL(10),
	LOGIN(11),
	RESET_PASSWORD(12),
	SEND_VERIFICATION_SMS(13),
	BLUESNAP_GET_PAYMENT_FIELD_TOKEN(30),
	BLUESNAP_CREATE_VENDOR_DRAFT(31);
	
	private final int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmOperation(int id) {
		this.id = id;
	}
	
	public static EnmOperation getOperation(int id) {
		for (EnmOperation operation : EnmOperation.values()) {
			if (operation.getId() == id) {
				return operation;
			}
		}
		return null;
	}
 	
}