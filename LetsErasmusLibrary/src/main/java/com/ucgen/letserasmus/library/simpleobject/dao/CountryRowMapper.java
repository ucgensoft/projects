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
	public static final String COL_ISO_CODE_3_DIGITS = "ISO_CODE_3_DIGITS";
	public static final String COL_ISO_CODE_2_DIGITS = "ISO_CODE_2_DIGITS";
	public static final String COL_STRIPE_SUPPORT_FLAG = "STRIPE_SUPPORT_FLAG";
	
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
		country.setIsoCodeTwoDigit(super.getString(rs, COL_ISO_CODE_2_DIGITS));
		country.setIsoCodeThreeDigit(super.getString(rs, COL_ISO_CODE_3_DIGITS));
		country.setStripeSupportFlag(super.getString(rs, COL_STRIPE_SUPPORT_FLAG));
		
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
		super.addColumn(COL_ISO_CODE_2_DIGITS);
		super.addColumn(COL_ISO_CODE_3_DIGITS);
		super.addColumn(COL_STRIPE_SUPPORT_FLAG);
	}
	
}
