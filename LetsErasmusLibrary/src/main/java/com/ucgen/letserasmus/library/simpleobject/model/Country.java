package com.ucgen.letserasmus.library.simpleobject.model;

public class Country {

	private Long id;
	private String name;
	private String code;
	private String isoCodeThreeDigit;
	private String isoCodeTwoDigit;
	private String stripeSupportFlag;
	
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
	public String getIsoCodeThreeDigit() {
		return isoCodeThreeDigit;
	}
	public void setIsoCodeThreeDigit(String isoCodeThreeDigit) {
		this.isoCodeThreeDigit = isoCodeThreeDigit;
	}
	public String getIsoCodeTwoDigit() {
		return isoCodeTwoDigit;
	}
	public void setIsoCodeTwoDigit(String isoCodeTwoDigit) {
		this.isoCodeTwoDigit = isoCodeTwoDigit;
	}
	public String getStripeSupportFlag() {
		return stripeSupportFlag;
	}
	public void setStripeSupportFlag(String stripeSupportFlag) {
		this.stripeSupportFlag = stripeSupportFlag;
	}
	
	
}
