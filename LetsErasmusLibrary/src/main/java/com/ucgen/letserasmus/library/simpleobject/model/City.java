package com.ucgen.letserasmus.library.simpleobject.model;

public class City {

	private Integer id;
	private String name;
	private Integer countryId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	public City() {
		this(null);
	}
	
	public City(Integer countryId) {
		this.countryId = countryId;
	}
	
}
