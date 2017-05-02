package com.ucgen.letserasmus.library.log.enumeration;

public enum EnmExternalSystem {

	TWILIO(1),
	BLUESNAP(2),
	STRIPE(3);
	
	private final int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmExternalSystem(int id) {
		this.id = id;
	}
	
	public static EnmExternalSystem getTransaction(int id) {
		for (EnmExternalSystem enmExternalSystem : EnmExternalSystem.values()) {
			if (enmExternalSystem.getId() == id) {
				return enmExternalSystem;
			}
		}
		return null;
	}
 	
}
