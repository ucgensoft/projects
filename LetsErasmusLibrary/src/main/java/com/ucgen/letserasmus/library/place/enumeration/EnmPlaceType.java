package com.ucgen.letserasmus.library.place.enumeration;

public enum EnmPlaceType {

	ENTIRE_PLACE(1, "Entire Place"),
	PRIVATE_ROOM(2, "Private Room"),
	SHARED_PLACE(3, "Shared Place");
	
	private Integer id;
	private String text;
	
	public Integer getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}

	EnmPlaceType(Integer id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public static EnmPlaceType getPlaceType(Integer id) {
		EnmPlaceType placeType = null;
		if (id != null) {
			for (EnmPlaceType tmpPlaceType : EnmPlaceType.values()) {
				if (id.equals(tmpPlaceType.getId())) {
					placeType = tmpPlaceType;
					break;
				}
			}
		}
		return placeType;
	}
	
}
