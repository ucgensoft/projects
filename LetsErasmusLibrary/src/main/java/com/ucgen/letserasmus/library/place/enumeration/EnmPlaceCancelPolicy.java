package com.ucgen.letserasmus.library.place.enumeration;

public enum EnmPlaceCancelPolicy {

	FLEXIBLE(1),
	MODERATE(2),
	STRICT(3);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	EnmPlaceCancelPolicy(Integer id) {
		this.id = id;
	}
	
	public static EnmPlaceCancelPolicy getPlaceCancelPolicy(Integer id) {
		if (id != null) {
			for (EnmPlaceCancelPolicy enmPlaceCancelPolicy : EnmPlaceCancelPolicy.values()) {
				if (enmPlaceCancelPolicy.getId().equals(id)) {
					return enmPlaceCancelPolicy;
				}
			}
		}
		return null;
	}
	
}
