package com.ucgen.letserasmus.library.payment.enumeration;

public enum EnmPaymentStatus {

	INITIAL(0),
	AUTH(1),
	AUTH_REVERSAL(2),
	CAPTURE(3),
	REFUND(4);
	
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	EnmPaymentStatus(Integer id) {
		this.id = id;
	}
	
}
