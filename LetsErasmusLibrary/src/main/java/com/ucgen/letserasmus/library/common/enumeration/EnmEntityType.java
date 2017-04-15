package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmEntityType {

	USER(1),
	PLACE(2),
	EVENT(3),
	STAFF(4),
	RESERVATION(5),
	MESSAGE(6);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	private EnmEntityType(Integer id) {
		this.id = id;
	}
	
	public static EnmEntityType getEntityType(Integer id) {
		if (id != null) {
			for (EnmEntityType entityType : EnmEntityType.values()) {
				if (entityType.getId().equals(id)) {
					return entityType;
				}
			}
		}
		return null;
	}
	
}
