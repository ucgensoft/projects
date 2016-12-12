package com.ucgen.letserasmus.library.location.model;

import java.math.BigDecimal;

import com.ucgen.common.model.BaseModel;

public class Location extends BaseModel {

	private Long id;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longitude;
	
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
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	
}
