package com.ucgen.letserasmus.library.location.model;

import java.math.BigDecimal;

public class LocationSearchCriteria {

	private BigDecimal lat1;
	private BigDecimal lat2;
	private BigDecimal lng1;
	private BigDecimal lng2;
	
	public BigDecimal getLat1() {
		return lat1;
	}
	public void setLat1(BigDecimal lat1) {
		this.lat1 = lat1;
	}
	public BigDecimal getLat2() {
		return lat2;
	}
	public void setLat2(BigDecimal lat2) {
		this.lat2 = lat2;
	}
	public BigDecimal getLng1() {
		return lng1;
	}
	public void setLng1(BigDecimal lng1) {
		this.lng1 = lng1;
	}
	public BigDecimal getLng2() {
		return lng2;
	}
	public void setLng2(BigDecimal lng2) {
		this.lng2 = lng2;
	}
	
}
