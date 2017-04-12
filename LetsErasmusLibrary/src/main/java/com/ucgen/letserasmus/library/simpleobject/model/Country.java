package com.ucgen.letserasmus.library.simpleobject.model;

public class Country {

	private Long id;
	private String name;
	private String code;
	private String isoCode;
	private String blueSnapCountryCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsoCode() {
		return isoCode;
	}
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	public String getBlueSnapCountryCode() {
		return blueSnapCountryCode;
	}
	public void setBlueSnapCountryCode(String blueSnapCountryCode) {
		this.blueSnapCountryCode = blueSnapCountryCode;
	}
	
}
