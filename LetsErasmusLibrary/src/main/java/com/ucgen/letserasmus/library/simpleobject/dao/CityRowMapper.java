package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.City;

public class CityRowMapper extends BaseRowMapper<City> {

	public static final String TABLE_NAME = "CITY";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COUNTRY_ID = "COUNTRY_ID";
	
	public CityRowMapper() {
		this(null);
	}
	
	public CityRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public City mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		City city = new City();
		
		city.setId(super.getInteger(rs, COL_ID));
		city.setName(super.getString(rs, COL_NAME));
		city.setCountryId(super.getInteger(rs, COL_COUNTRY_ID));
		
		return city;
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
