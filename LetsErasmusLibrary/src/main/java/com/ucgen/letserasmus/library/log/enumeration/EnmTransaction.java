package com.ucgen.letserasmus.library.log.enumeration;

public enum EnmTransaction {

	RESERVATION_SEND_INQUIRY(1),
	RESERVATION_SEND_REQUEST(2),
	RESERVATION_ACCEPT(3),
	RESERVATION_DECLINE(4),
	RESERVATION_EXPIRED(5),
	RESERVATION_RECALL(6),
	RESERVATION_HOST_CANCELLED(7),
	RESERVATION_CLIENT_CANCELLED(8),
	RESERVATION_PAYOUT_SUCCESS(9),
	RESERVATION_PAYOUT_FAILED(10),
	RESERVATION_REVIEW(11),
	USER_CREATE(21);
	
	private final int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmTransaction(int id) {
		this.id = id;
	}
	
	public static EnmTransaction getTransaction(int id) {
		for (EnmTransaction enmTransaction : EnmTransaction.values()) {
			if (enmTransaction.getId() == id) {
				return enmTransaction;
			}
		}
		return null;
	}
 	
}
