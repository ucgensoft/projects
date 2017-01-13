package com.ucgen.letserasmus.library.location.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.location.model.Location;

public class LocationRowMapper extends BaseRowMapper<Location> {

	public static final String TABLE_NAME = "LOCATION";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COUNTRY = "COUNTRY";
	public static final String COL_STATE = "STATE";
	public static final String COL_STREET = "STREET_NUMBER";
	public static final String COL_LATITUDE = "LATITUDE";
	public static final String COL_LONGITUDE = "LONGITUDE";

	public LocationRowMapper() {
		this(null);
	}
	
	public LocationRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Location location = new Location();
		
		location.setId(super.getLong(rs, COL_ID));
		location.setName(super.getString(rs, COL_NAME));
		location.setLatitude(super.getBigDecimal(rs, COL_LATITUDE));
		location.setLongitude(super.getBigDecimal(rs, COL_LONGITUDE));
		location.setCountry(super.getString(rs, COL_COUNTRY));
		location.setState(super.getString(rs, COL_STATE));
		location.setStreet(super.getString(rs, COL_STREET));
		
		return location;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_NAME);
		super.addColumn(COL_LATITUDE);
		super.addColumn(COL_LONGITUDE);
		super.addColumn(COL_COUNTRY);
		super.addColumn(COL_STATE);
		super.addColumn(COL_STREET);
	}

}
