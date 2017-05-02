package com.ucgen.letserasmus.library.payment.enumeration;

public enum EnmVendorEntityType {

	PERSONAL("P"),
	BUSINESS("B");
	
	private String id;
	
	public String getId() {
		return this.id;
	}
	
	EnmVendorEntityType(String id) {
		this.id = id;
	}
	
	public static EnmVendorEntityType getEntityType(String id) {
		if (id != null) {
			for (EnmVendorEntityType entityType : EnmVendorEntityType.values()) {
				if (entityType.getId().equalsIgnoreCase(id)) {
					return entityType;
				}
			}			
		}
		return null;
	}
	
}
