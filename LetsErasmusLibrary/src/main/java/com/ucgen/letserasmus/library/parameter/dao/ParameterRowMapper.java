package com.ucgen.letserasmus.library.parameter.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.parameter.model.Parameter;

public class ParameterRowMapper extends BaseRowMapper<Parameter> {

	public static final String TABLE_NAME = "PARAMETER";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_VALUE = "VALUE";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	
	@Override
	public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Parameter parameter = new Parameter();
		
		parameter.setId(super.getInteger(rs, COL_ID));
		parameter.setName(super.getString(rs, COL_NAME));
		parameter.setValue(super.getString(rs, COL_VALUE));
		
		return parameter;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		// TODO Auto-generated method stub
		
	}

}
