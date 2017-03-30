package com.ucgen.letserasmus.library.transactionlog.enumeration;

public enum EnmTransaction {

	RESERVATION_SEND_INQUIRY(1),
	RESERVATION_SEND_REQUEST(2),
	RESERVATION_ACCEPT(3),
	RESERVATION_DECLINE(4),
	RESERVATION_RECALL(5),
	RESERVATION_CANCEL(6),
	RESERVATION_EXPIRE(7),
	
	REVIEW(11);
	
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
