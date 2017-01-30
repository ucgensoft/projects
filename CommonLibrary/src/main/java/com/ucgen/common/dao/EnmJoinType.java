package com.ucgen.common.dao;

public enum EnmJoinType {

	LEFT("LEFT JOIN"), RIGHT("RIGHT JOIN"), INNER("INNER JOIN");
	
	private String sqlString;
	
	public String getSqlString() {
		return this.sqlString;
	}
	
	EnmJoinType(String sqlString) {
		this.sqlString = sqlString;
	}
	
}
