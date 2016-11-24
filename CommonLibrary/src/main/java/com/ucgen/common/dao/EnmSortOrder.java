package com.ucgen.common.dao;

public enum EnmSortOrder {

	ASC("ASC"), DESC("DESC");
	
	private String value;
	
	public String getValue() {
		return this.value;
	}
	
	EnmSortOrder(String value) {
		this.value = value;
	}
}
