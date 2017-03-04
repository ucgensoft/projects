package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.Country;

public class CountryRowMapper extends BaseRowMapper<Country> {

	public static final String TABLE_NAME = "ENUMERATION";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_CODE = "CODE";
	public static final String COL_ISO_CODE = "ISO_CODE";
	
	public CountryRowMapper() {
		this(null);
	}
	
	public CountryRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Country country = new Country();
		
		country.setId(super.getLong(rs, COL_ID));
		country.setName(super.getString(rs, COL_NAME));
		country.setCode(super.getString(rs, COL_CODE));
		country.setIsoCode(super.getString(rs, COL_ISO_CODE));
		
		return country;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_NAME);
		super.addColumn(COL_CODE);
		super.addColumn(COL_ISO_CODE);
	}
	
}
