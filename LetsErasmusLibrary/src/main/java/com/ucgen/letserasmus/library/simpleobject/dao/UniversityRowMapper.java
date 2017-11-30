package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.University;

public class UniversityRowMapper extends BaseRowMapper<University> {

	public static final String TABLE_NAME = "UNIVERSITY";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COUNTRY_ID = "COUNTRY_ID";
	
	public UniversityRowMapper() {
		this(null);
	}
	
	public UniversityRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public University mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		University university = new University();
		
		university.setId(super.getLong(rs, COL_ID));
		university.setName(super.getString(rs, COL_NAME));
		university.setCountryId(super.getInteger(rs, COL_COUNTRY_ID));
		
		return university;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_NAME);
		super.addColumn(COL_COUNTRY_ID);
	}
	
}
