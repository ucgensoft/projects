package com.ucgen.letserasmus.library.place.enumeration;

public enum EnmHomeType {

	HOUSE(1, "House"),
	APARTMENT(2, "Apartment"),
	HOSTEL(3, "Hostel");
	
	private Integer id;
	private String text;
	
	public Integer getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}

	EnmHomeType(Integer id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public static EnmHomeType getHomeType(Integer id) {
		EnmHomeType homeType = null;
		if (id != null) {
			for (EnmHomeType tmpHomeType : EnmHomeType.values()) {
				if (id.equals(tmpHomeType.getId())) {
					homeType = tmpHomeType;
					break;
				}
			}
		}
		return homeType;
	}
	
}
